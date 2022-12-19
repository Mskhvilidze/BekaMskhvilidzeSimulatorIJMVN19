package sample.presenter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
        Text text = new Text(LoaderPresenter.getNameOfGame());
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
        final FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files (*.java)", "*.java");
        chooser.getExtensionFilters().add(extFilter);
        chooser.setTitle("Open my File");
        chooser.setInitialDirectory(new File(PATH));
        File selectedFile = chooser.showOpenDialog(stage);
        if (selectedFile != null) {
            service.compile(PATH + selectedFile.getName());
        }
    }

    @FXML
    private void onClose() {
        this.service.onPlatformExit(this.stage);
    }

    @FXML
    private void onSaveCode() {
        //TODO Bug mus gefixt werden
        this.service.save(this.area.getText(), PATH + LoaderPresenter.getTextName() + ".java");
    }

    @FXML
    private void onFileLoad() {
        Service.toggleNodeVisible(anchor, false);
        menu.setDisable(false);
        this.area.setText(Service.getCode(Service.getMap().get(stage), LoaderPresenter.getNameOfGame()));
    }
}
