package sample.presenter;

import automata.Test;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.model.AbstractAutomaton;
import sample.model.GameOfLifeAutomaton;
import sample.model.KruemelmonsterAutomaten;
import sample.util.AudioCache;
import sample.util.Simulation;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Presenter extends AbstractPresenter implements Initializable {
    public static final String FXML = "/fxml/view.fxml";
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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(automaton.getRows());
        setCanvas(canvas);
        Platform.runLater(() -> {
            this.rowSize.setText(String.valueOf(automaton.getRows()));
            this.colSize.setText(String.valueOf(automaton.getColumns()));
            this.scrollPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> populationPanel.center(newValue));
            initPopulationView(automaton);
            setTooltip();
            setHgrowAndVgrow();
        });
        automaton.randomPopulation();
        this.slider.valueProperty().addListener((a, b, c) -> this.simulation.setSpeed(c.intValue()));
        System.out.println(this.toggleGroup.getUserData());
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
        for (Map.Entry<String, Stage> entry : map.entrySet()) {
            if (this.beenden.getId().equals(entry.getKey().substring(0, entry.getKey().length() - 1))) {
                this.service.onPlatformExit(map.get(entry.getKey()));
            }
        }
    }

    //Menu
    @FXML
    private void onNewGameWindow() {
        service.onNewGameWindow();
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
        this.service.togglePaneVisible(dialogWindow, true);
    }

    @FXML
    private void onChangeSize() {
        Platform.runLater(() -> {
            if (Integer.parseInt(rowSize.getText()) > 100 || Integer.parseInt(colSize.getText()) > 100) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Maximale Größe ist 100");
                alert.showAndWait();
            } else if (Integer.parseInt(rowSize.getText()) < 5 || Integer.parseInt(colSize.getText()) < 5) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Minimale Größe ist 5");
                alert.showAndWait();
            } else {
                automaton.changeSize(Integer.parseInt(rowSize.getText()), Integer.parseInt(colSize.getText()));
                populationPanel.paintPopulation();
            }
        });
        if (automaton instanceof GameOfLifeAutomaton){
            System.out.println("A");
        }

        if (automaton instanceof KruemelmonsterAutomaten){
            System.out.println("B");
        }

        if (automaton instanceof Test){
            System.out.println("V");
        }
        this.service.togglePaneVisible(dialogWindow, false);
    }

    @FXML
    private void onDialogWindowClose() {
        this.service.togglePaneVisible(dialogWindow, false);
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
    }

    @FXML
    private void onStop() {
        this.simulation.stop();
        service.toggleNodeDisable(this.startSimulation, false);
        service.toggleNodeDisable(this.stepSimulation, false);
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
    }

    @FXML
    private void onClassLoad() {
        final FileChooser chooser = new FileChooser();
        chooser.setTitle("Open my File");
        chooser.setInitialDirectory(new File(EditorPresenter.PATH));
        File selectedFile = chooser.showOpenDialog(null);
        if (selectedFile != null) {
            this.service.onLoadNewAutomaton(service.loadProgram(selectedFile.getName().split("\\.")[0]));
        }
    }
}