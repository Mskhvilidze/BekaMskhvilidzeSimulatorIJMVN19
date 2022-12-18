package sample.presenter;

import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import sample.model.KruemelmonsterAutomaten;
import sample.util.Simulation;
import sample.util.StatesColorMapping;
import sample.model.AbstractAutomaton;
import sample.util.Pair;
import sample.view.PopulationPanel;
import sample.view.PopulationPanelImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AbstractPresenter {
    public static final String SOUND_PATH = "src/main/resources/sound/";
    protected int ox = 0;
    protected int oy = 0;
    protected int activeCell = 0;
    protected Random random = new Random();
    protected Service service;
    protected Map<String, Stage> map = new HashMap<>();
    protected Pair<Integer> pair;
    protected Simulation simulation;
    protected AbstractAutomaton automaton;
    protected PopulationPanel populationPanel;
    private Canvas canvas;

    public AbstractPresenter() {
        automaton = new KruemelmonsterAutomaten(45, 45, false);
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void initPopulationView(AbstractAutomaton automaton) {
        StatesColorMapping mapping = new StatesColorMapping(automaton.getNumberOfStates());
        populationPanel = new PopulationPanelImpl(automaton, canvas, mapping);
    }

    public boolean isPairNotNull(Pair<Integer> pair) {
        this.pair = pair;
        if (this.pair != null) {
            ox = this.pair.getValue1();
            oy = this.pair.getValue2();
            return true;
        }
        return false;
    }

    public void setAutomaton(AbstractAutomaton newAutomaton) {
        automaton = newAutomaton;
    }
}
