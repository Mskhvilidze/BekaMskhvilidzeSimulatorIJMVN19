package sample;

import javafx.stage.Stage;

import java.util.Objects;

public class RequestNewStage {
    private Stage stage;

    public RequestNewStage() {
        this.stage = new Stage();
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestNewStage newStage = (RequestNewStage) o;
        return Objects.equals(stage, newStage.stage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stage);
    }
}
