package sample;

import com.google.common.eventbus.EventBus;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sample.model.Automaten;
import sample.model.KruemelmonsterAutomaten;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("UnstableApiUsage")
public class Controller implements Initializable {
    public static final String FXML = "/fxml/view.fxml";
    public static final String SOUND_PATH = "C:\\Beka\\OMP\\IDEA\\Übungen\\BekaMskhvilidzeSimulatorIJMVN19\\src\\main\\resources\\sound\\";
    @FXML
    private DialogPane dialogWindow;
    @FXML
    private TextField ROW_SIZE;
    @FXML
    private TextField COL_SIZE;
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
    private PopulationPanel populationPanel;
    private Automaten automaten;
    private StatesColorMapping mapping;
    private EventBus bus;
    Random random = new Random();
    private Map<String, Stage> map = new HashMap<>();
    private int activeCell = 0;
    int a = 0;
    int b = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        automaten = new KruemelmonsterAutomaten(45, 45, true);
        this.ROW_SIZE.setText(String.valueOf(automaten.getROWS()));
        this.COL_SIZE.setText(String.valueOf(automaten.getCOLUMNS()));
        this.scrollPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
            this.populationPanel.center(newValue);
        });
        Platform.runLater(() -> {
            initPopulationPanel(automaten);
            setTooltip();
            set_H_grow_And_V_grow();
        });
        automaten.randomPopulation();
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

    private void set_H_grow_And_V_grow() {
        HBox.setHgrow(this.hBoxForSimul, Priority.ALWAYS);
        VBox.setVgrow(this.hBoxForSimul, Priority.ALWAYS);
        HBox.setHgrow(this.pane, Priority.ALWAYS);
        VBox.setVgrow(this.hbox, Priority.ALWAYS);
        VBox.setVgrow(this.vBox, Priority.ALWAYS);
        VBox.setVgrow(this.pane, Priority.ALWAYS);
        HBox.setHgrow(this.stackPane, Priority.ALWAYS);
        HBox.setHgrow(this.scrollPane, Priority.ALWAYS);
    }

    private void initPopulationPanel(Automaten automaten) {
        mapping = new StatesColorMapping(automaten.getNumberOfStates());
        populationPanel = new PopulationPanel(automaten, canvas, mapping);
    }

    /**
     * ZoomButtons deaktivieren und aktivieren
     *
     * @param button   button
     * @param disabled button disabled
     */
    private void toggleButtonDisable(Button button, boolean disabled) {
        if (button.getId().equals(this.zoomIn.getId()) && !disabled) {
            this.zoomOut.disableProperty().set(false);
        }

        if (button.getId().equals(this.zoomOut.getId()) && !disabled) {
            this.zoomIn.disableProperty().set(false);
        }
        button.disableProperty().set(disabled);
    }

    private void togglePaneVisible(Pane pane, boolean visible) {
        pane.setVisible(visible);
    }

    private boolean isToggleTorus(boolean isTorus) {
        return !isTorus;
    }

    //Public
    public void setEventBus(EventBus eventBus) {
        this.bus = eventBus;
        eventBus.register(this);
    }

    public void setStage(Stage stage) {
        this.beenden.setId("" + random.nextInt());
        map.put(this.beenden.getId(), stage);
    }

    //Menu
    @FXML
    private void onNewGameWindow() {
        RequestNewStage newStage = new RequestNewStage();
        this.bus.post(newStage);
    }

    @FXML
    private void onPlatformExit() {
        this.bus.post(new RequestExitStage(map.get(this.beenden.getId())));
    }

    //Button
    @FXML
    private void onZoomIn() {
        this.populationPanel.incZoom();
        Platform.runLater(() -> {
            this.stackPane.setPrefWidth(this.populationPanel.getMinStackPaneWidth());
            this.stackPane.setPrefHeight(this.populationPanel.getMinStackPaneHeight());
        });
        toggleButtonDisable(zoomIn, this.populationPanel.isDisableZoomIn());
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
        toggleButtonDisable(zoomOut, this.populationPanel.isDisableZoomOut());
    }

    @FXML
    private void onCreateRandomPopulation() throws ExecutionException {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    automaten.randomPopulation();
                    initPopulationPanel(automaten);
                });
            }
        }, 1000);
        AudioCache.getAudio("s.mp3").play();
    }

    @FXML
    private void onResetPopulation() throws ExecutionException {
        AudioCache.getAudio("clear.mp3").play();
        Platform.runLater(() -> {
            this.automaten.clearPopulation();
            initPopulationPanel(automaten);
        });
    }

    @FXML
    private void onDialog_Window_Open() {
        this.togglePaneVisible(dialogWindow, true);
    }

    @FXML
    private void onChangeSize() {
        Platform.runLater(() -> {
            if (Integer.parseInt(ROW_SIZE.getText()) > 50 || Integer.parseInt(COL_SIZE.getText()) > 50) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Maximale Größe ist 50");
                alert.showAndWait();
            } else if (Integer.parseInt(ROW_SIZE.getText()) < 15 || Integer.parseInt(COL_SIZE.getText()) < 15) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Minimale Größe ist 15");
                alert.showAndWait();
            } else {
                automaten.changeSize(Integer.parseInt(ROW_SIZE.getText()), Integer.parseInt(COL_SIZE.getText()));
                initPopulationPanel(automaten);
            }
        });
        this.togglePaneVisible(dialogWindow, false);
    }

    @FXML
    private void onDialog_Window_Close() {
        this.togglePaneVisible(dialogWindow, false);
    }

    @FXML
    private void onSetTorus() {
        automaten.setTorus(isToggleTorus(automaten.isTorus()));
    }

    @FXML
    private void onStart() {
        try {
            automaten.nextGeneration();
            initPopulationPanel(automaten);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void onStateGenerate(ActionEvent event) {
        Platform.runLater(() -> {
            RadioButton radioButton = (RadioButton) event.getSource();
            activeCell = Integer.parseInt(radioButton.getText());
            automaten.setNumberOfStates(Integer.parseInt(radioButton.getText()) + 1);
            initPopulationPanel(automaten);
        });
    }

    public void onMousePressed(MouseEvent mouseEvent) {
        int x = (int) ((mouseEvent.getX() - 15) / 15);
        int y = (int) ((mouseEvent.getY() - 15) / 15);
        a = x;
        b = y;
        automaten.setState(y, x, activeCell);
        initPopulationPanel(automaten);
    }

    public void onTest(MouseEvent mouseEvent) {
        int x = (int) ((mouseEvent.getX() - 15) / 15);
        int y = (int) ((mouseEvent.getY() - 15) / 15);
        automaten.setState(b, a, y, x,activeCell);
        initPopulationPanel(automaten);
    }
}