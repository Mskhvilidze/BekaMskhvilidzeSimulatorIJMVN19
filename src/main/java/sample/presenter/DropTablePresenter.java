package sample.presenter;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.presenter.database.DatabaseAutomatonStore;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class DropTablePresenter extends AbstractPresenter implements Initializable {

    public static final String FXML = "/fxml/loeschen.fxml";
    @FXML
    private ComboBox<Object> tables;
    @FXML
    private Button delete;
    private Stage stage;
    private DatabaseAutomatonStore dataBaseConnection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        delete.disableProperty().bind(tables.getSelectionModel().selectedItemProperty().isNull());
    }

    public void setDropTableStage(Stage stage) {
        this.stage = stage;
        for (Map.Entry<String , Stage> entry:AbstractPresenter.map.entrySet()){
            if (stage == entry.getValue()){
                AbstractPresenter.map.remove(entry.getKey());
            }
        }
    }

    public void setDropTableService(Service service) {
        setService(service);
    }

    public void setDataBaseConnection(DatabaseAutomatonStore dataBaseConnection) {
        this.dataBaseConnection = dataBaseConnection;
    }

    @FXML
    private void onDeleteTable() {
        String tableName = (String) tables.getValue();
        dataBaseConnection.dropTable(tableName);
        tables.getItems().remove(tableName);
        service.onPlatformExit(stage);
    }

    @FXML
    public void onStageCancel() {
        stage.close();
        for (Map.Entry<String , Stage> entry:AbstractPresenter.map.entrySet()){
            if (stage == entry.getValue()){
                AbstractPresenter.map.remove(entry.getKey());
            }
        }
    }

    @FXML
    private void onShowAllTables(ActionEvent event) {
        FadeTransition transition = new FadeTransition(Duration.millis(1000));
        Button button = (Button) event.getSource();
        transition.setNode(button);
        transition.setFromValue(30);
        transition.setToValue(0);
        transition.setOnFinished(evt -> {
            Service.toggleNodeVisible(button, false);
            Service.toggleNodeVisible(tables,true);
        });
        transition.play();
        List<String> list = dataBaseConnection.getAllTables();
        tables.setItems(FXCollections.observableArrayList(list));
    }
}
