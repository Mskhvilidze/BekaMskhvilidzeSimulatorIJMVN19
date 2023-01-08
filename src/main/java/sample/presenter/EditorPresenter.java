package sample.presenter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EditorPresenter extends AbstractPresenter implements Initializable {
    public static final String FXML = "/fxml/editor.fxml";
    public static final String SOURCE = "text/";
    public static final String PATH = "automata/";
    @FXML
    private Button load;
    @FXML
    private MenuBar menu;
    @FXML
    private AnchorPane anchor;
    @FXML
    private TextArea area;
    private Stage stage;

    public EditorPresenter() {
        super();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox.setVgrow(this.area, Priority.ALWAYS);
        Text text =
                new Text(LoaderPresenter.getNameOfGame() == null ? automaton.getClass().getSimpleName() : LoaderPresenter.getNameOfGame());
        load.disableProperty().bind(text.textProperty().isEmpty());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void simulatorPresenter(Service service) {
        setService(service);
    }

    @FXML
    private void onCompile() {
        String className = Service.getMap().get(stage);
        File file = new File(PATH + className + ".java");
        if (file.exists()) {
            service.compile(file.getPath());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "File muss erst gespeichert werden!", ButtonType.OK);
            alert.setTitle("Compilierergebnis");
            alert.showAndWait();
        }
    }

    @FXML
    private void onClose() {
        this.service.onPlatformExit(this.stage);
    }

    @FXML
    private void onSaveCode() {
        String className = Service.getMap().get(stage);
        this.service.save(this.area.getText(), PATH + className + ".java");
    }

    @FXML
    private void onFileLoad() {
        Service.toggleNodeVisible(anchor, false);
        menu.setDisable(false);
        boolean isExists = Service.isFileExists(Service.getMap().get(stage));
        String className = Service.getMap().get(stage);
        this.area.setText(Service.getCode(className,
                LoaderPresenter.getNameOfGame() == null ? automaton.getClass().getSimpleName() + ".txt" : LoaderPresenter.getNameOfGame(),
                isExists));
    }
}
