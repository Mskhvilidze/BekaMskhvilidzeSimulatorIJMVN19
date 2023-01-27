package sample.message.request;

import javafx.stage.Stage;
import sample.message.AbstractStageMessage;

import java.util.Map;

public class RequestSaveTableStage extends AbstractStageMessage {

    private Map<String, Double> map;
    private int size;

    public RequestSaveTableStage(Stage stage, Map<String, Double> map, int size) {
        super(stage);
        this.map = map;
        this.size = size;
    }

    public Map<String, Double> getMap() {
        return map;
    }

    public int getSize() {
        return size;
    }
}
