package sample.presenter;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.presenter.database.DatabaseAutomatonStore;
import java.net.URL;
import java.util.*;

public class CreateTablePresenter extends AbstractPresenter implements Initializable {

    public TextField tableName;
    public Button create;
    private DatabaseAutomatonStore dataBaseConnection;
    private Stage stage;
    private Map<Stage, List<Double>> map = new HashMap<>();
    private List<Double> list = new ArrayList<>();

    public static final String FXML = "/fxml/save_table.fxml";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        create.disableProperty().bind(tableName.textProperty().isEmpty());
    }

    public void setDataBaseConnection(DatabaseAutomatonStore dataBaseConnection) {
        this.dataBaseConnection = dataBaseConnection;
    }

    public void setSaveTableStage(Stage stage) {
        this.stage = stage;
    }

    public void setPresenterService(Service service) {
        setService(service);
    }

    public void addFields(double width, double height, double panelWidth, double panelHeight, double speed) {
        list.add(width);
        list.add(height);
        list.add(panelWidth);
        list.add(panelHeight);
        list.add(speed);
        map.put(stage, list);
    }

    @FXML
    private void onCreateTable() {
        double width = map.get(stage).get(0);
        double height = map.get(stage).get(1);
        double panelWidth = map.get(stage).get(2);
        double panelHeight = map.get(stage).get(3);
        double speed = map.get(stage).get(4);
        dataBaseConnection.createTable(tableName.getText().toUpperCase(), width, height, panelWidth, panelHeight, speed);
        this.service.onPlatformExit(this.stage);
    }

    @FXML
    private void onStageCancel() {
        this.service.onPlatformExit(this.stage);
    }
}
