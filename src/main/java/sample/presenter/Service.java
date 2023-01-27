package sample.presenter;

import com.google.common.eventbus.EventBus;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import sample.message.request.*;
import sample.model.AbstractAutomaton;
import sample.util.AutomatonHelper;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class Service {

    private static EventBus eventBus;
    private static EventBus event;
    private static Map<Stage, String> map = new HashMap<>();

    public Service(EventBus eventBus, EventBus bus) {
        if (eventBus != null) {
            setEventBus(eventBus);
            eventBus.register(true);
        }
        if (bus != null) {
            setEventBusForPresenter(bus);
            bus.register(true);
        }
    }

    public static void setEventBus(EventBus bus) {
        eventBus = bus;
    }

    public static void setEventBusForPresenter(EventBus bus) {
        event = bus;
    }

    public static void onNewGameWindow() {
        RequestNewStage newStage = new RequestNewStage();
        eventBus.post(newStage);
    }

    public void onNewEditorStage(Stage stage) {
        RequestEditorStage requestEditorStage = new RequestEditorStage(stage);
        eventBus.post(requestEditorStage);
    }

    public void onSaveTable(Stage stage, Map<String, Double> columns, int size) {
        RequestSaveTableStage saveTableStage = new RequestSaveTableStage(stage, columns, size);
        eventBus.post(saveTableStage);
    }

    public void onDeleteTable(Stage stage) {
        RequestDeleteTableStage deleteTableStage = new RequestDeleteTableStage(stage);
        eventBus.post(deleteTableStage);
    }

    public void onRestoreTable(Stage stage) {
        RequestRestoreTableStage requestRestoreTableStage = new RequestRestoreTableStage(stage);
        eventBus.post(requestRestoreTableStage);
    }

    public void onRestoreAutomaton(List<String> list) {
        RequestRestoreAutomaton<String> requestRestoreAutomaton = new RequestRestoreAutomaton<>(list);
        event.post(requestRestoreAutomaton);
    }

    public static Map<Stage, String> getMap() {
        return map;
    }

    public static void onLoadNewAutomaton(AbstractAutomaton automaton, String name) {
        if (map.size() < 1) {
            RequestNewAutomaton newAutomaton = new RequestNewAutomaton(automaton, new Stage(), name);
            eventBus.post(newAutomaton);
        }
        for (Map.Entry<Stage, String> entry : map.entrySet()) {
            if (entry.getValue().equals(name)) {
                RequestNewAutomaton newAutomaton = new RequestNewAutomaton(automaton, entry.getKey(), name);
                eventBus.post(newAutomaton);
            }
        }
    }

    public void onPlatformExit(Stage stageID) {
        eventBus.post(new RequestExitStage(stageID));
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

    public void toggleNodeDisable(Node node, boolean visible) {
        node.setDisable(visible);
    }

    public static void toggleNodeVisible(Node node, boolean visible) {
        node.setVisible(visible);
    }

    public boolean isToggleTorus(boolean isTorus) {
        return !isTorus;
    }

    public static void toggleRadioButton(ToggleGroup group, AbstractAutomaton automaton) {
        for (int i = 0; i < automaton.getNumberOfStates(); i++) {
            Node node = (Node) group.getToggles().get(i);
            node.setDisable(false);
        }
    }

    public static void add(Stage stage, String name) {
        map.put(stage, name);
    }

    public void save(String code, String path) {
        code = code.replace("\n", System.lineSeparator());
        ArrayList<String> lines = new ArrayList<>();
        lines.add(code);
        Path createdFile = Paths.get(path);
        try (FileOutputStream outputStream = new FileOutputStream(new File(createdFile.toUri()))) {
            if (!Files.exists(createdFile)) {
                Files.createFile(createdFile);
                Path pathTo = Paths.get(createdFile.toUri());
                Files.write(pathTo, lines, StandardCharsets.UTF_8);
            } else {
                outputStream.write(code.getBytes());
            }
        } catch (Exception exc) {
            alert("Ups, da ist was schief gelaufen!", "Information");
        }
    }

    public static String getCode(String name, String filename, boolean isExist) {
        try {
            Path file = Paths.get(isExist ? EditorPresenter.PATH + name + ".java" : EditorPresenter.SOURCE + filename);
            File searchFile = new File(file.toUri());
            if (!searchFile.exists()) {
                AutomatonHelper automatonHelper = new AutomatonHelper(searchFile);
                automatonHelper.createAutomaton(filename);
            } else {
                StringBuilder text = new StringBuilder();
                List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
                for (int l = 0; l < lines.size(); l++) {
                    text.append(lines.get(l));
                    if (l < lines.size() - 1) {
                        text.append(System.lineSeparator());
                    }
                }
                return text.toString().replaceAll("Automata", name);
            }
        } catch (Exception exc) {
            return "";
        }
        return "";
    }

    public void compile(String name) {
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        boolean success = javac.run(null, null, err, name) == 0;
        if (!success) {
            String msg = err.toString();
            alert(msg, "Compilierergebnis");
        } else {
            alert("Compilieren erfolgreich!", "Compilierergebnis");
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

    public static boolean isFileExists(String filename) {
        File file = new File(EditorPresenter.PATH);
        File[] files = file.listFiles();
        assert files != null;
        for (File name : files) {
            String foundFile = name.getName().substring(0, name.getName().length() - 5);
            if (foundFile.equals(filename)) {
                return true;
            }
        }
        return false;
    }

    public static void alert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }


}
