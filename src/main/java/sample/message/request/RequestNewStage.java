package sample.message.request;

import javafx.stage.Stage;
import sample.message.AbstractStageMessage;

public class RequestNewStage extends AbstractStageMessage {

    public RequestNewStage() {
        super(new Stage());
    }
}
