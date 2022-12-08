package sample.presenter;

import com.google.common.eventbus.EventBus;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.message.request.RequestExitStage;
import sample.message.request.RequestNewStage;

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
}
