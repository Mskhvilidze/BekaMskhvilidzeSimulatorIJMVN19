package sample.view;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.message.request.*;
import sample.model.AbstractAutomaton;
import sample.model.KruemelmonsterAutomaten;
import sample.presenter.*;
import java.io.IOException;

@SuppressWarnings("UnstableApiUsage")
public class View {
    private static final String SIMULATOR = "Simulator";
    private Stage primaryStage;
    private Scene scene;
    private Service service;
    private EventBus eventBus;
    public View(Stage stage, EventBus eventBus) {
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
        AbstractAutomaton automaton = new KruemelmonsterAutomaten(45, 45, false);
        AbstractPresenter abstractPresenter = new AbstractPresenter();
        abstractPresenter.setAutomaton(automaton);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(Presenter.FXML));
        Parent rootParent = loader.load();
        Presenter presenter = loader.getController();
        presenter.simulatorPresenter(this.service);
        Platform.runLater(() -> {
            primaryStage.initStyle(StageStyle.UTILITY);
            primaryStage.setTitle(SIMULATOR);
            scene = new Scene(rootParent, 815, 850);
            primaryStage.setScene(scene);
            primaryStage.show();
            presenter.setStage(primaryStage);
        });
        primaryStage.setOnCloseRequest(event -> presenter.closeStage());
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
            stage.setTitle(LoaderPresenter.getTextName());
            scene = new Scene(rootParent, 815, 850);
            stage.setScene(scene);
            stage.show();
            presenter.setStage(stage);
        });
        stage.setOnCloseRequest(event -> presenter.closeStage());
    }

    @Subscribe
    private void newEditorScene(RequestEditorStage newStage) throws IOException {
        Stage stage = newStage.getStage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(EditorPresenter.FXML));
        Parent rootParent = loader.load();
        EditorPresenter presenter = loader.getController();
        presenter.simulatorPresenter(this.service);
        Platform.runLater(() -> {
            stage.setTitle(Service.getMap().get(stage));
            scene = new Scene(rootParent, 600, 400);
            stage.setScene(scene);
            stage.show();
            presenter.setStage(stage);
        });

    }

    @Subscribe
    private void closeStage(RequestExitStage exitStage) {
        Stage stage = (Stage) exitStage.getStage().getScene().getWindow();
        stage.close();
    }

    @Subscribe
    public void onLoadNewAutomaton(RequestNewAutomaton newAutomaton) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(Presenter.FXML));
        Parent rootParent = loader.load();
        Presenter presenter = loader.getController();
        presenter.simulatorPresenter(this.service);
        presenter.setAutomaton(newAutomaton.getAutomaton());
        Platform.runLater(() -> {
            newAutomaton.getStage().setTitle(newAutomaton.getName());
            scene = new Scene(rootParent, 815, 850);
            newAutomaton.getStage().setScene(scene);
            newAutomaton.getStage().show();
        });
        presenter.setStage(newAutomaton.getStage());
        newAutomaton.getStage().setOnCloseRequest(event -> presenter.closeStage());
    }

    private void setMinAndMaxSizeOfStage() {
        this.primaryStage.setMinHeight(170);
        this.primaryStage.setMinWidth(250);
    }
}
