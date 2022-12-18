package sample.presenter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.ResourceBundle;

public class LoaderPresenter extends AbstractPresenter implements Initializable {
    @FXML
    private Pane pane;
    @FXML
    private TextField name;
    @FXML
    private Button ja;
    @FXML
    private ToggleGroup toggle;
    private static String textName;
    private static String nameOfGame;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.ja.disableProperty()
                .bind(this.name.textProperty().isEmpty().or(this.name.textProperty().greaterThan("\\^([A-Z])([A-Za-z]*)$")));
    }

    public static void setTextName(String text){
        textName = text;
    }

    public static String getTextName(){
        return textName;
    }

    public static void setNameOfGame(String nameOfGame) {
        LoaderPresenter.nameOfGame = nameOfGame;
    }

    public static String getNameOfGame() {
        return nameOfGame;
    }

    @FXML
    private void onCreateClass() {
        RadioButton button = (RadioButton) toggle.getSelectedToggle();
        setTextName(name.getText());
        setNameOfGame(button.getText());
        Service.toggleNodeVisible(pane, false);
        Service.onNewGameWindow();
    }

    @FXML
    private void onStageClose() {
       Service.toggleNodeVisible(pane, false);
    }
}
