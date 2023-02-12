package sample.presenter;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.message.AbstractNewAutomatonMessage;
import sample.message.request.RequestRestoreAutomaton;
import sample.util.AudioCache;
import sample.util.Simulation;
import sample.view.PopulationContextMenu;
import sample.view.PopulationPanelImpl;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("UnstableApiUsage")
public class Presenter extends AbstractPresenter implements Initializable {
    public static final String FXML = "/fxml/view.fxml";
    @FXML
    public Pane paneForNewLoader;
    @FXML
    private Menu pop;
    @FXML
    private Menu sim;
    @FXML
    private Menu setting;
    @FXML
    private Menu automata;
    @FXML
    private MenuItem menuStop;
    @FXML
    private MenuItem menuStart;
    @FXML
    private Slider slider;
    @FXML
    private Button ok;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private DialogPane dialogWindow;
    @FXML
    private TextField rowSize;
    @FXML
    private TextField colSize;
    @FXML
    private MenuItem beenden;
    @FXML
    private Pane stackPane;
    @FXML
    private Canvas canvas;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    @FXML
    private VBox pane;
    @FXML
    private HBox hbox;
    @FXML
    private HBox hBoxForSimul;
    @FXML
    private Button stopSimulation;
    @FXML
    private Button startSimulation;
    @FXML
    private Button stepSimulation;
    @FXML
    private Button zoomOut;
    @FXML
    private Button zoomIn;
    @FXML
    private Button print;
    @FXML
    private Button createRandomPopulation;
    @FXML
    private Button torus;
    @FXML
    private Button breakUp;
    @FXML
    private Button sizeChange;
    @FXML
    private Button laden;
    @FXML
    private Button generate;

    public Presenter() {
        super();
        EventBus bus = new EventBus();
        bus.register(this);
        new Service(null, bus);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCanvas(canvas);
        Platform.runLater(() -> {
            this.rowSize.setText(String.valueOf(automaton.getRows()));
            this.colSize.setText(String.valueOf(automaton.getColumns()));
            this.scrollPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> populationPanel.center(newValue));
            Service.toggleRadioButton(this.toggleGroup, automaton);
            initPopulationView(automaton);
            setTooltip();
            setHgrowAndVgrow();
        });
        automaton.randomPopulation();
        this.slider.valueProperty().addListener((a, b, c) -> this.simulation.setSpeed(c.intValue()));
    }

    public void simulatorPresenter(Service service) {
        setService(service);
    }

    public void setStage(Stage stage) {
        this.beenden.setId("" + random.nextInt());
        map.put(this.beenden.getId(), stage);
    }

    private void setTooltip() {
        this.generate.setTooltip(new Tooltip("Neuen Automaten erzeugen und laden"));
        this.laden.setTooltip(new Tooltip("Existierenden Automaten laden"));
        this.sizeChange.setTooltip(new Tooltip("Größe der Population ändern"));
        this.breakUp.setTooltip(new Tooltip("Alle Zellen in den Zustand 0 versetzen"));
        this.createRandomPopulation.setTooltip(new Tooltip("Zufällige Population erzeugen"));
        this.torus.setTooltip(new Tooltip("Population als Torus betrachten"));
        this.print.setTooltip(new Tooltip("Print"));
        this.zoomIn.setTooltip(new Tooltip("Zoom In"));
        this.zoomOut.setTooltip(new Tooltip("Zoom Out"));
        this.stepSimulation.setTooltip(new Tooltip("Einen einzelnen Simulationszyklus ausführen"));
        this.startSimulation.setTooltip(new Tooltip("Simulation starten"));
        this.stopSimulation.setTooltip(new Tooltip("Simulation anhalten"));
    }

    private void setHgrowAndVgrow() {
        HBox.setHgrow(this.hBoxForSimul, Priority.ALWAYS);
        VBox.setVgrow(this.hBoxForSimul, Priority.ALWAYS);
        HBox.setHgrow(this.pane, Priority.ALWAYS);
        VBox.setVgrow(this.hbox, Priority.ALWAYS);
        VBox.setVgrow(this.vBox, Priority.ALWAYS);
        VBox.setVgrow(this.pane, Priority.ALWAYS);
        HBox.setHgrow(this.stackPane, Priority.ALWAYS);
        HBox.setHgrow(this.scrollPane, Priority.ALWAYS);
    }

    public void closeStage() {
        this.service.onPlatformExit(map.get(this.beenden.getId()));
        String stage = "";
        for (Map.Entry<String, Stage> entry : map.entrySet()) {
            if (this.beenden.getId().equals(entry.getKey().substring(0, entry.getKey().length() - 1))) {
                this.service.onPlatformExit(map.get(entry.getKey()));
            }
            if (entry.getValue() == map.get(this.beenden.getId())) {
                stage = entry.getKey();
            }
        }
        map.remove(stage);
    }

    //Menu
    @FXML
    private void onNewGameWindow() {
        Service.toggleNodeVisible(paneForNewLoader, true);
    }

    @FXML
    private void onPlatformExit() {
        closeStage();
    }

    //Button
    @FXML
    private void onZoomIn() {
        populationPanel.incZoom();
        Platform.runLater(() -> stackPane.setPrefSize(populationPanel.getMinStackPaneWidth(), populationPanel.getMinStackPaneHeight()));
        this.service.toggleZoomButtonDisable(zoomIn, this.zoomIn, this.zoomOut, populationPanel.isDisableZoomIn());
    }

    @FXML
    private void onZoomOut() {
        populationPanel.decZoom();
        Platform.runLater(() -> stackPane.setPrefSize(populationPanel.getMinStackPaneWidth(), populationPanel.getMinStackPaneHeight()));
        this.service.toggleZoomButtonDisable(zoomOut, this.zoomOut, this.zoomIn, populationPanel.isDisableZoomOut());
    }

    @FXML
    private void onCreateRandomPopulation() throws ExecutionException {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    automaton.randomPopulation();
                    populationPanel.paintPopulation();
                });
            }
        }, 1000);
        AudioCache.getAudio("s.mp3").play();
    }

    @FXML
    private void onResetPopulation() throws ExecutionException {
        AudioCache.getAudio("clear.mp3").play();
        Platform.runLater(() -> {
            automaton.clearPopulation();
            populationPanel.paintPopulation();
        });
    }

    @FXML
    private void onDialogWindowOpen() {
        this.ok.disableProperty().bind(rowSize.textProperty().greaterThan("\\d+$").or(rowSize.textProperty().isEmpty())
                .or(colSize.textProperty().greaterThan("\\d+$")).or(colSize.textProperty().isEmpty()));
        Service.toggleNodeVisible(dialogWindow, true);
    }

    @FXML
    private void onChangeSize() {
        Platform.runLater(() -> {
            if (Integer.parseInt(rowSize.getText()) > 100 || Integer.parseInt(colSize.getText()) > 100) {
                Service.alert("Maximale Größe ist 100", "");
            } else if (Integer.parseInt(rowSize.getText()) < 5 || Integer.parseInt(colSize.getText()) < 5) {
                Service.alert("Minimale Größe ist 5", "");
            } else {
                automaton.changeSize(Integer.parseInt(rowSize.getText()), Integer.parseInt(colSize.getText()));
                initPopulationView(automaton);
            }
        });
        Service.toggleNodeVisible(dialogWindow, false);
    }

    @FXML
    private void onDialogWindowClose() {
        Service.toggleNodeVisible(dialogWindow, false);
    }

    @FXML
    private void onSetTorus() {
        automaton.setTorus(this.service.isToggleTorus(automaton.isTorus()));
    }

    @FXML
    private void onStart() {
        simulation = new Simulation(automaton, populationPanel);
        simulation.start();
        service.toggleNodeDisable(this.startSimulation, true);
        service.toggleNodeDisable(this.stepSimulation, true);
        service.toggleNodeDisable(this.stopSimulation, false);
        this.menuStart.setDisable(true);
        this.menuStop.setDisable(false);
    }

    @FXML
    private void onStop() {
        this.simulation.stop();
        service.toggleNodeDisable(this.startSimulation, false);
        service.toggleNodeDisable(this.stepSimulation, false);
        this.menuStart.setDisable(false);
        this.menuStop.setDisable(true);
    }

    @FXML
    private void onStateGenerate(ActionEvent event) {
        Platform.runLater(() -> {
            RadioButton radioButton = (RadioButton) event.getSource();
            activeCell = Integer.parseInt(radioButton.getText());
            automaton.setNumberOfStates(Integer.parseInt(radioButton.getText()) + 1);
            populationPanel.paintPopulation();
        });
    }

    @FXML
    private void onMousePressed(MouseEvent mouseEvent) {
        if (isPairNotNull(populationPanel.getCell(mouseEvent.getX(), mouseEvent.getY()))) {
            automaton.setState(pair.getValue2(), pair.getValue1(), activeCell);
            Platform.runLater(() -> populationPanel.paintPopulation());
        }
    }

    @FXML
    private void onMouseDragged(MouseEvent mouseEvent) {
        pair = populationPanel.getCell(mouseEvent.getX(), mouseEvent.getY());
        if (pair != null) {
            automaton.setState(oy, ox, pair.getValue2(), pair.getValue1(), activeCell);
            Platform.runLater(() -> populationPanel.paintPopulation());
        }
    }

    @FXML
    public void onEdit() {
        Stage stage = new Stage();
        map.put(this.beenden.getId() + "1", stage);
        service.onNewEditorStage(stage);
        Service.add(stage, map.get(this.beenden.getId()).getTitle());
    }

    @FXML
    private void onClassLoad() {
        final FileChooser chooser = new FileChooser();
        chooser.setTitle("Open my File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files (*.java)", "*.java");
        chooser.getExtensionFilters().add(extFilter);
        chooser.setInitialDirectory(new File(EditorPresenter.PATH));
        File selectedFile = chooser.showOpenDialog(null);
        if (selectedFile != null) {
            Service.onLoadNewAutomaton(service.loadProgram(selectedFile.getName().split("\\.")[0]), selectedFile.getName().split("\\.")[0]);
            for (Map.Entry<String, Stage> entry : map.entrySet()) {
                if (this.beenden.getId().equals(entry.getKey().substring(0, entry.getKey().length() - 1))) {
                    this.service.onPlatformExit(map.get(entry.getKey()));
                }
            }
        }
    }

    @FXML
    private void onStepSimulate() {
        automaton.nextGeneration();
        populationPanel.paintPopulation();
    }

    @FXML
    private void onMakeContextMenuEvent(ContextMenuEvent event) {
        if (pair != null) {
            PopulationContextMenu menu = new PopulationContextMenu(automaton, pair.getValue1(), pair.getValue2(), populationPanel);
            menu.show(stackPane.getScene().getWindow(), event.getScreenX(), event.getScreenY());
        }
    }

    @FXML
    private void onSaveAutomatonSerialize() {
        Serialization serialization = new Serialization();
        serialization.saveAutomaton(new AbstractNewAutomatonMessage(automaton), map.get(this.beenden.getId()));
    }

    @FXML
    private void onLoadAutomatonDeserialize() {
        Serialization serialization = new Serialization();
        setAutomaton(serialization.loadAutomaton(automaton, map.get(this.beenden.getId())));
        initPopulationView(automaton);
    }

    @FXML
    private void onSaveAutomatonXMLSerialize() {
        Serialization serialization = new Serialization();
        serialization.saveXML(new AbstractNewAutomatonMessage(automaton), map.get(this.beenden.getId()));
    }

    @FXML
    private void onLoadXMLAutomatonDeserialize() {
        Serialization serialization = new Serialization();
        setAutomaton(serialization.loadXML(automaton, map.get(this.beenden.getId())));
        initPopulationView(automaton);
    }

    @FXML
    private void onSaveTable() {
        Stage stage = new Stage();
        map.put(this.beenden.getId() + "2", stage);
        Map<String, Double> columns = new HashMap<>();
        columns.put("width", map.get(beenden.getId()).getWidth());
        columns.put("height", map.get(beenden.getId()).getHeight());
        columns.put("panelWidth", populationPanel.getMinStackPaneWidth());
        columns.put("panelHeight", populationPanel.getMinStackPaneHeight());
        columns.put("slider", slider.getValue());
        columns.put("x", map.get(beenden.getId()).getScene().getWindow().getX());
        columns.put("y", map.get(beenden.getId()).getScene().getWindow().getY());
        service.onSaveTable(stage, columns, PopulationPanelImpl.size);
    }

    @FXML
    private void onDeleteTable() {
        Stage stage = new Stage();
        map.put(this.beenden.getId() + "3", stage);
        service.onDeleteTable(stage);
    }

    @FXML
    private void onRestoreTable() {
        Stage stage = new Stage();
        map.put(this.beenden.getId() + "4", stage);
        service.onRestoreTable(stage);
    }

    @Subscribe
    public void onRestoreAutomaton(RequestRestoreAutomaton<String> requestRestoreAutomaton) {
        List<String> list = requestRestoreAutomaton.getList();
        Stage stage = map.get(beenden.getId());
        stage.setWidth(Double.parseDouble(list.get(0)));
        stage.setHeight(Double.parseDouble(list.get(1)));
        stackPane.setPrefSize(Double.parseDouble(list.get(2)), Double.parseDouble(list.get(3)));
        slider.setValue(Double.parseDouble(list.get(4)));
        populationPanel.repaint((int) Double.parseDouble(list.get(5)));
        stage.getScene().getWindow().setX(Double.parseDouble(list.get(6)));
        stage.getScene().getWindow().setY(Double.parseDouble(list.get(7)));
        map.remove(this.beenden.getId() + "4");
        Service.alert("" + map.size(), "Rest");
    }

    @FXML
    private void onChangeEnglishLanguage(){
        new PropertyPresenter(automata, pop, sim, setting, true, false);
    }

    @FXML
    private void onChangeGermanLanguage() {
        new PropertyPresenter(automata, pop, sim, setting, false, true);
    }

    @FXML
    private void onSetColor(ActionEvent event) {
        ColorPicker picker = (ColorPicker) event.getSource();
        final int state = Integer.parseInt(picker.getUserData().toString());
        mapping.setColor(state, picker.getValue());
        populationPanel.paintPopulation();
    }
}
