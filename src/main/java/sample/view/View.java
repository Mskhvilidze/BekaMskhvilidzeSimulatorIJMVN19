package sample.view;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.RequestExitStage;
import sample.RequestNewStage;
import sample.presenter.Presenter;
import sample.presenter.Service;

import java.io.IOException;

@SuppressWarnings("UnstableApiUsage")
public class View {
    private Stage primaryStage;
    private Scene scene;
    private Service service;
    private EventBus eventBus;

    public View(Stage stage, EventBus eventBus)  {
        this.eventBus = eventBus;
        this.primaryStage = stage;

    }

    public void show() throws IOException {
        eventBus.register(this);
        this.service = new Service(eventBus);
        initView();
        setMinAndMaxSizeOfStage();
    }

    private void initView() throws IOException {
        showScene();
    }

    private void showScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(Presenter.FXML));
        Parent rootParent = loader.load();
        Presenter presenter = loader.getController();
        presenter.simulatorPresenter(this.service);
        Platform.runLater(() -> {
            primaryStage.initStyle(StageStyle.UTILITY);
            primaryStage.setTitle("Simulator");
            scene = new Scene(rootParent, 815, 815);
            primaryStage.setScene(scene);
            primaryStage.show();
        });
        presenter.setStage(primaryStage);
    }

    @Subscribe
    private void newShowScene(RequestNewStage newStage) throws IOException {
        Stage stage = newStage.getStage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(Presenter.FXML));
        Parent rootParent = loader.load();
        Presenter presenter = loader.getController();
        presenter.simulatorPresenter(this.service);
        Platform.runLater(() -> {
            stage.setTitle("Simulator");
            scene = new Scene(rootParent, 815, 815);
            stage.setScene(scene);
            stage.show();
        });
        presenter.setStage(stage);
    }

    @Subscribe
    private void closeStage(RequestExitStage exitStage) {
       Stage stage = (Stage) exitStage.getStage().getScene().getWindow();
       stage.close();
    }

    private void setMinAndMaxSizeOfStage() {
        this.primaryStage.setMinHeight(470);
        this.primaryStage.setMinWidth(250);
        this.primaryStage.setMaxWidth(950);
        this.primaryStage.setMaxHeight(950);
    }
}
