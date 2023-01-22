package sample.message.request;

import javafx.stage.Stage;
import sample.message.AbstractStageMessage;

public class RequestSaveTableStage extends AbstractStageMessage {

    private double width;
    private double height;
    private double paneWidth;
    private double paneHeight;
    private double speed;

    public RequestSaveTableStage(Stage stage, double width, double height, double paneWidth, double paneHeight, double speed) {
        super(stage);
        this.width = width;
        this.height = height;
        this.paneWidth = paneWidth;
        this.paneHeight = paneHeight;
        this.speed = speed;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getPaneWidth() {
        return paneWidth;
    }

    public double getPaneHeight() {
        return paneHeight;
    }

    public double getSpeed() {
        return speed;
    }
}
