package sample;

import javafx.stage.Stage;

import java.util.Objects;

public class RequestExitStage {
    private Stage stage;

    public RequestExitStage(Stage stage) {
        this.stage = stage;
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
        RequestExitStage that = (RequestExitStage) o;
        return Objects.equals(stage, that.stage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stage);
    }
}
