package sample;

import com.google.common.eventbus.EventBus;
import javafx.stage.Stage;
@SuppressWarnings("UnstableApiUsage")
public class App extends javafx.application.Application {
    private EventBus eventBus;
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.eventBus = new EventBus();
        this.eventBus.register(this);
        new View(primaryStage, this.eventBus);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
