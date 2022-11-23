package sample;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

@SuppressWarnings("UnstableApiUsage")
public class View {
    private Stage primaryStage;
    private Scene scene;
    private EventBus bus;

    public View(Stage stage, EventBus eventBus) throws IOException {
        this.bus = eventBus;
        eventBus.register(this);
        this.primaryStage = stage;
        initView();
    }

    public View() {

    }

    private void initView() throws IOException {
        showScene();
    }


    private void showScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(Controller.FXML));
        Parent rootParent = loader.load();
        Controller controller = loader.getController();
        controller.setEventBus(this.bus);
        setMin_And_Max_Size_Of_Stage();
        Platform.runLater(() -> {
            primaryStage.initStyle(StageStyle.UTILITY);
            primaryStage.setTitle("Simulator");
            scene = new Scene(rootParent, 815, 815);
            primaryStage.setScene(scene);
            primaryStage.show();
        });
        controller.setStage(primaryStage);
    }

    @Subscribe
    private void newShowScene(RequestNewStage newStage) throws IOException {
        Stage stage = newStage.getStage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(Controller.FXML));
        Parent rootParent = loader.load();
        Controller controller = loader.getController();
        controller.setEventBus(this.bus);
        setMin_And_Max_Size_Of_Stage();
        Platform.runLater(() -> {
            stage.setTitle("Simulator");
            scene = new Scene(rootParent, 815, 815);
            stage.setScene(scene);
            stage.show();
        });
        controller.setStage(stage);
    }

    @Subscribe
    private void ExitStage(RequestExitStage exitStage) {
       Stage stage = (Stage) exitStage.getStage().getScene().getWindow();
       stage.close();
    }

    private void setMin_And_Max_Size_Of_Stage() {
        this.primaryStage.setMinHeight(470);
        this.primaryStage.setMinWidth(250);
        this.primaryStage.setMaxWidth(950);
        this.primaryStage.setMaxHeight(950);
    }
}
