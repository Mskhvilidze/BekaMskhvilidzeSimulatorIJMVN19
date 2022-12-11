package sample.presenter;

import com.google.common.eventbus.EventBus;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.message.request.RequestEditorStage;
import sample.message.request.RequestExitStage;
import sample.message.request.RequestNewStage;
import sample.model.AbstractAutomaton;
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

    public void toggleButtonDisable(Button button, boolean visible) {
        button.setDisable(visible);
    }

    public void togglePaneVisible(Pane pane, boolean visible) {
        pane.setVisible(visible);
    }

    public boolean isToggleTorus(boolean isTorus) {
        return !isTorus;
    }

    public void saveCodeAndSetAutomaton(AbstractAutomaton automaton, String code) {
        code = code.replace("\n", System.lineSeparator());
        ArrayList<String> lines = new ArrayList<>();
        lines.add(code);
        try {
            Path pathTo = Paths.get(EditorPresenter.FILENAME);
            Files.write(pathTo, lines, StandardCharsets.UTF_8);
        } catch (Exception exc) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, da ist was schief gelaufen!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        setAutomaton(automaton, code);
    }

    private static void setAutomaton(AbstractAutomaton automaton, String code) {
        code = code.replaceAll(System.lineSeparator(), "");
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == EditorPresenter.SWITCH) {
                int row = i / automaton.getColumns();
                int col = i % automaton.getColumns();
                if (row >= automaton.getRows()) {
                    return;
                }
                automaton.changeSize(row, col);
            }
        }
    }

    public static String getCodeAndSetAutomaton(AbstractAutomaton automaton) {
        try {
            Path file = Paths.get(EditorPresenter.FILENAME);
            StringBuilder text = new StringBuilder();
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (int l = 0; l < lines.size(); l++) {
                text.append(lines.get(l));
                if (l < lines.size() - 1) {
                    text.append(System.lineSeparator());
                }
                System.out.println(text);
            }
            setAutomaton(automaton, text.toString());
            return text.toString();
        } catch (Exception exc) {
            return "";
        }
    }
}
