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

    @FXML
    private TextField tableName;
    @FXML
    private Button create;
    private DatabaseAutomatonStore dataBaseConnection;
    private Stage stage;
    private Map<Stage, List<Double>> map = new HashMap<>();
    private List<Double> list = new ArrayList<>();
    Map<String, Double> columns = new HashMap<>();
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

    public void addFields(Map<String, Double> columns, int size, Stage stage) {
        list.add(columns.get("width"));
        list.add(columns.get("height"));
        list.add(columns.get("panelWidth"));
        list.add(columns.get("panelHeight"));
        list.add(columns.get("slider"));
        list.add((double) size);
        list.add(columns.get("x"));
        list.add(columns.get("y"));
        map.put(stage, list);
    }

    @FXML
    private void onCreateTable() {
        columns.put("width", map.get(stage).get(0));
        columns.put("height", map.get(stage).get(1));
        columns.put("panelWidth", map.get(stage).get(2));
        columns.put("panelHeight", map.get(stage).get(3));
        columns.put("slider", map.get(stage).get(4));
        columns.put("size", map.get(stage).get(5));
        columns.put("x", map.get(stage).get(6));
        columns.put("y", map.get(stage).get(7));
        dataBaseConnection.createTable(tableName.getText().toUpperCase(), columns);
        this.service.onPlatformExit(this.stage);
    }

    @FXML
    private void onStageCancel() {
        this.service.onPlatformExit(this.stage);
    }
}
