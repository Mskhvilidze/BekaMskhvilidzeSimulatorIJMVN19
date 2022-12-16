package sample.presenter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EditorPresenter extends AbstractPresenter implements Initializable {
    public static final String FXML = "/fxml/editor.fxml";
    public static final String SOURCE = "text/";
    public static final String PATH = "automata/";
    @FXML
    private MenuBar menu;
    @FXML
    private Pane pane;
    @FXML
    private TextField name;
    @FXML
    private Button ja;
    @FXML
    private TextArea area;
    private Stage stage;
    @FXML
    private ToggleGroup toggle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox.setVgrow(this.area, Priority.ALWAYS);
        this.ja.disableProperty()
                .bind(this.name.textProperty().isEmpty().or(this.name.textProperty().greaterThan("\\^([A-Z])([A-Za-z]*)$")));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void simulatorPresenter(Service service) {
        setService(service);
    }

    @FXML
    private void onClose() {
        this.service.onPlatformExit(this.stage);
    }

    @FXML
    private void onSaveCode() {
        this.service.save(this.area.getText().replaceAll("Automata", name.getText()), PATH + name.getText() + ".java");
    }

    @FXML
    private void onCreateClass() {
        RadioButton button = (RadioButton) toggle.getSelectedToggle();
        this.area.setText(Service.getCode(name.getText(), button.getText()));
        this.service.togglePaneVisible(pane, false);
        this.service.toggleNodeDisable(this.area, false);
        this.service.toggleNodeDisable(menu, false);
    }

    public void onStageClose() {
        this.service.onPlatformExit(stage);
    }

    public void onCompile() {
        final FileChooser chooser = new FileChooser();
        chooser.setTitle("Open my File");
        chooser.setInitialDirectory(new File(PATH));
        File selectedFile = chooser.showOpenDialog(stage);
        if (selectedFile != null) {
            service.compile(PATH + selectedFile.getName());
        }
    }
}
