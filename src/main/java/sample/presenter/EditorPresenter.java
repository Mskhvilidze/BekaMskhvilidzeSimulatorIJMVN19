package sample.presenter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditorPresenter extends AbstractPresenter implements Initializable {
    public static final String FXML = "/fxml/editor.fxml";
    public static final String FILENAME = "automaton.txt";
    public static final char SWITCH = 's';
    @FXML
    private TextArea area;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox.setVgrow(this.area, Priority.ALWAYS);
        this.area.setText(Service.getCodeAndSetAutomaton(automaton));
    }

    public void setStage(Stage stage){
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
        this.service.saveCodeAndSetAutomaton(automaton, this.area.getText());
    }
}
