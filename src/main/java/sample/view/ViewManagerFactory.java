package sample.view;

import com.google.common.eventbus.EventBus;
import javafx.stage.Stage;

import java.io.IOException;

@SuppressWarnings("UnstableApiUsage")
public interface ViewManagerFactory {
    View create(Stage primaryStage, EventBus eventBus) throws IOException;
}
