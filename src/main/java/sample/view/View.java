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
import sample.model.KruemelmonsterAutomaton;
import sample.presenter.*;
import sample.presenter.database.DatabaseAutomatonStore;

import java.io.IOException;

@SuppressWarnings("UnstableApiUsage")
public class View {
    private static final String SIMULATOR = "Simulator";
    private Stage primaryStage;
    private Scene scene;
    private Service service;
    private EventBus eventBus;
    private DatabaseAutomatonStore store;

    public View(Stage stage, EventBus eventBus) {
        this.eventBus = eventBus;
        this.primaryStage = stage;
        this.store = new DatabaseAutomatonStore();
    }

    public void show() throws IOException {
        eventBus.register(this);
        this.service = new Service(eventBus, null);
        initView();
        setMinAndMaxSizeOfStage();
    }

    private void initView() throws IOException {
        showScene();
    }

    private void showScene() throws IOException {
        AbstractAutomaton automaton = new KruemelmonsterAutomaton(45, 45, false);
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
        if (AbstractPresenter.map.size() < 2) {
            this.store.shutdown();
        }

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

    @Subscribe
    public void onSaveTable(RequestSaveTableStage saveTableStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(CreateTablePresenter.FXML));
        Parent rootParent = loader.load();
        CreateTablePresenter presenter = loader.getController();
        presenter.setDataBaseConnection(this.store);
        presenter.setPresenterService(this.service);
        presenter.setSaveTableStage(saveTableStage.getStage());
        presenter.addFields(saveTableStage.getMap(), saveTableStage.getSize(), saveTableStage.getStage());
        Platform.runLater(() -> {
            saveTableStage.getStage().setTitle("Speichern");
            scene = new Scene(rootParent, 589.0, 198.0);
            saveTableStage.getStage().setScene(scene);
            saveTableStage.getStage().show();
        });
    }

    @Subscribe
    public void onDeleteTable(RequestDeleteTableStage deleteTableStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(DropTablePresenter.FXML));
        Parent rootParent = loader.load();
        DropTablePresenter presenter = loader.getController();
        presenter.setDropTableStage(deleteTableStage.getStage());
        presenter.setDataBaseConnection(this.store);
        presenter.setDropTableService(this.service);
        Platform.runLater(() -> {
            deleteTableStage.getStage().setTitle("Löschen");
            scene = new Scene(rootParent, 501.0, 198.0);
            deleteTableStage.getStage().setScene(scene);
            deleteTableStage.getStage().show();
        });
    }

    @Subscribe
    public void onRestoreTable(RequestRestoreTableStage requestRestoreTableStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(RestorePresenter.FXML));
        Parent rootParent = loader.load();
        RestorePresenter presenter = loader.getController();
        presenter.setRestoredTableStage(requestRestoreTableStage.getStage());
        presenter.setDataBaseConnection(this.store);
        presenter.setRestoredTableService(this.service);
        Platform.runLater(() -> {
            requestRestoreTableStage.getStage().setTitle("Wiederherstellen");
            scene = new Scene(rootParent, 533.0, 198.0);
            requestRestoreTableStage.getStage().setScene(scene);
            requestRestoreTableStage.getStage().show();
        });
    }

    private void setMinAndMaxSizeOfStage() {
        this.primaryStage.setMinHeight(170);
        this.primaryStage.setMinWidth(250);
    }
}
