package sample;

import com.google.common.eventbus.EventBus;
import javafx.stage.Stage;
import sample.view.View;
import sample.view.ViewManagerFactory;

@SuppressWarnings("UnstableApiUsage")
public class App extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewManagerFactory manager = View::new;
        EventBus eventBus = new EventBus();
        eventBus.register(this);
        View view = manager.create(primaryStage, eventBus);
        view.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
