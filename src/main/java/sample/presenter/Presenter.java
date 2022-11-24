package sample.presenter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sample.AudioCache;
import sample.StatesColorMapping;
import sample.model.AbstractAutomaton;
import sample.model.KruemelmonsterAutomaten;
import sample.view.PopulationPanelImpl;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;


public class Presenter extends AbstractPresenter implements Initializable {
    public static final String FXML = "/fxml/view.fxml";
    public static final String SOUND_PATH = "src/main/resources/sound/";
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
    private PopulationPanelImpl populationPanel;
    private AbstractAutomaton automaton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        automaton = new KruemelmonsterAutomaten(45, 45, true);
        this.rowSize.setText(String.valueOf(automaton.getRows()));
        this.colSize.setText(String.valueOf(automaton.getColumns()));
        this.scrollPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> this.populationPanel.center(newValue));
        Platform.runLater(() -> {
            initPopulationPanel(automaton);
            setTooltip();
            setHgrowAndVgrow();
        });
        automaton.randomPopulation();
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

    private void initPopulationPanel(AbstractAutomaton automaton) {
        StatesColorMapping mapping = new StatesColorMapping(automaton.getNumberOfStates());
        populationPanel = new PopulationPanelImpl(automaton, canvas, mapping);
    }

    //Menu
    @FXML
    private void onNewGameWindow() {
        service.onNewGameWindow();
    }

    @FXML
    private void onPlatformExit() {
        this.service.onPlatformExit(map.get(this.beenden.getId()));
    }

    //Button
    @FXML
    private void onZoomIn() {
        this.populationPanel.incZoom();
        Platform.runLater(() -> {
            this.stackPane.setPrefWidth(this.populationPanel.getMinStackPaneWidth());
            this.stackPane.setPrefHeight(this.populationPanel.getMinStackPaneHeight());
        });
        this.service.toggleButtonDisable(zoomIn, this.zoomIn, this.zoomOut, this.populationPanel.isDisableZoomIn());
    }

    @FXML
    private void onZoomOut() {
        this.populationPanel.decZoom();
        Platform.runLater(() -> {
            this.stackPane.setPrefWidth(this.populationPanel.getMinStackPaneWidth());
            this.stackPane.setPrefHeight(this.populationPanel.getMinStackPaneHeight());

            if (this.stackPane.getPrefHeight() <= 746.0) {
                this.stackPane.setPrefSize(-1, -1);
            }
        });
        this.service.toggleButtonDisable(zoomIn, this.zoomIn, this.zoomOut, this.populationPanel.isDisableZoomIn());
    }

    @FXML
    private void onCreateRandomPopulation() throws ExecutionException {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    automaton.randomPopulation();
                    initPopulationPanel(automaton);
                });
            }
        }, 1000);
        AudioCache.getAudio("s.mp3").play();
    }

    @FXML
    private void onResetPopulation() throws ExecutionException {
        AudioCache.getAudio("clear.mp3").play();
        Platform.runLater(() -> {
            this.automaton.clearPopulation();
            initPopulationPanel(automaton);
        });
    }

    @FXML
    private void onDialogWindowOpen() {
        this.service.togglePaneVisible(dialogWindow, true);
    }

    @FXML
    private void onChangeSize() {
        Platform.runLater(() -> {
            if (Integer.parseInt(rowSize.getText()) > 50 || Integer.parseInt(colSize.getText()) > 50) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Maximale Größe ist 50");
                alert.showAndWait();
            } else if (Integer.parseInt(rowSize.getText()) < 15 || Integer.parseInt(colSize.getText()) < 15) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Minimale Größe ist 15");
                alert.showAndWait();
            } else {
                automaton.changeSize(Integer.parseInt(rowSize.getText()), Integer.parseInt(colSize.getText()));
                initPopulationPanel(automaton);
            }
        });
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
        automaton.nextGeneration();
        initPopulationPanel(automaton);
    }

    @FXML
    private void onStateGenerate(ActionEvent event) {
        Platform.runLater(() -> {
            RadioButton radioButton = (RadioButton) event.getSource();
            activeCell = Integer.parseInt(radioButton.getText());
            automaton.setNumberOfStates(Integer.parseInt(radioButton.getText()) + 1);
            initPopulationPanel(automaton);
        });
    }

    @FXML
    private void onMousePressed(MouseEvent mouseEvent) {
        int x = (int) ((mouseEvent.getX() - 15) / 15);
        int y = (int) ((mouseEvent.getY() - 15) / 15);
        a = x;
        b = y;
        automaton.setState(y, x, activeCell);
        initPopulationPanel(automaton);
    }

    @FXML
    private void onTest(MouseEvent mouseEvent) {
        int x = (int) ((mouseEvent.getX() - 15) / 15);
        int y = (int) ((mouseEvent.getY() - 15) / 15);
        automaton.setState(b, a, y, x, activeCell);
        initPopulationPanel(automaton);
    }
}