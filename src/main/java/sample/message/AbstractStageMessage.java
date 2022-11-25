package sample.message;

import javafx.stage.Stage;

import java.util.Objects;

public abstract class AbstractStageMessage implements Message{
    private final Stage stage;

    protected AbstractStageMessage(Stage stage) {
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
        AbstractStageMessage that = (AbstractStageMessage) o;
        return Objects.equals(stage, that.stage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stage);
    }
}
