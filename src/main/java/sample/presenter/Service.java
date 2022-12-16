package sample.presenter;

import com.google.common.eventbus.EventBus;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.message.request.RequestEditorStage;
import sample.message.request.RequestExitStage;
import sample.message.request.RequestNewAutomaton;
import sample.message.request.RequestNewStage;
import sample.model.AbstractAutomaton;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class Service {

    private EventBus eventBus;

    public Service(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(true);
    }

    public void onNewGameWindow() {
        RequestNewStage newStage = new RequestNewStage();
        this.eventBus.post(newStage);
    }

    public void onNewEditorStage(Stage stage) {
        RequestEditorStage requestEditorStage = new RequestEditorStage(stage);
        this.eventBus.post(requestEditorStage);
    }

    public void onLoadNewAutomaton(AbstractAutomaton automaton){
        RequestNewAutomaton newAutomaton = new RequestNewAutomaton(automaton);
        this.eventBus.post(newAutomaton);
    }
    public void onPlatformExit(Stage stageID) {
        this.eventBus.post(new RequestExitStage(stageID));
    }

    /**
     * ZoomButtons deaktivieren und aktivieren
     *
     * @param button   button
     * @param disabled button disabled
     */
    public void toggleZoomButtonDisable(Button button, Button zoomIn, Button zoomOut, boolean disabled) {
        if (button.getId().equals(zoomIn.getId()) && !disabled) {
            zoomOut.disableProperty().set(false);
        }

        if (button.getId().equals(zoomOut.getId()) && !disabled) {
            zoomIn.disableProperty().set(false);
        }
        button.disableProperty().set(disabled);
    }

    public void toggleNodeDisable(Node button, boolean visible) {
        button.setDisable(visible);
    }

    public void togglePaneVisible(Pane pane, boolean visible) {
        pane.setVisible(visible);
    }

    public boolean isToggleTorus(boolean isTorus) {
        return !isTorus;
    }

    public void save(String code, String path) {
        code = code.replace("\n", System.lineSeparator());
        ArrayList<String> lines = new ArrayList<>();
        lines.add(code);
        Path createdFile = Paths.get(path);
        try {
            if (!Files.exists(createdFile)) {
                Files.createFile(createdFile);
                Path pathTo = Paths.get(createdFile.toUri());
                Files.write(pathTo, lines, StandardCharsets.UTF_8);
            } else {
                alert("File existiert schon");
            }
        } catch (Exception exc) {
            alert("Ups, da ist was schief gelaufen!");
        }
    }

    public static String getCode(String name, String filename) {
        try {
            Path file = Paths.get(EditorPresenter.SOURCE + filename);
            StringBuilder text = new StringBuilder();
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (int l = 0; l < lines.size(); l++) {
                text.append(lines.get(l));
                if (l < lines.size() - 1) {
                    text.append(System.lineSeparator());
                }
            }
            return text.toString().replaceAll("Automata", name);
        } catch (Exception exc) {
            return "";
        }
    }

    public void compile(String name) {
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        boolean success = javac.run(null, null, err, name) == 0;
        if (!success) {
            String msg = err.toString();
            Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
            alert.setTitle("Compilierergebnis");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Compilieren erfolgreich!", ButtonType.OK);
            alert.setTitle("Compilierergebnis");
            alert.showAndWait();
        }
    }

    public AbstractAutomaton loadProgram(String name) {
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(".").toURI().toURL()})) {
            return (AbstractAutomaton) classLoader.loadClass("automata." + name).getConstructor().newInstance();
        } catch (IOException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException |
                 SecurityException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void alert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text, ButtonType.OK);
        alert.showAndWait();
    }


}
