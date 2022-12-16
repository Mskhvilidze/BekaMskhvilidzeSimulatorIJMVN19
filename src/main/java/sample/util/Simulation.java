package sample.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import sample.model.AbstractAutomaton;
import sample.view.PopulationPanel;

public class Simulation {
    public static final int MIN_SPEED = 0;
    public static final int MAX_SPEED = 100;
    public static final int DEF_SPEED = 50;
    private BooleanProperty running = new SimpleBooleanProperty(false);
    private SimulationThread thread;
    private AbstractAutomaton automaton;
    private PopulationPanel populationPanel;
    private volatile int speed;

    public Simulation(AbstractAutomaton automaton, PopulationPanel populationPanel) {
        this.automaton = automaton;
        this.populationPanel = populationPanel;
        this.thread = null;
        this.speed = DEF_SPEED;
    }


    public void start() {
        thread = new SimulationThread();
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return this.speed;
    }

    public BooleanProperty isRunning() {
        return running;
    }

    class SimulationThread extends Thread {

        @Override
        public void run() {
            try {
                running.set(true);
                while (!isInterrupted()) {
                    automaton.nextGeneration();
                    populationPanel.paintPopulation();
                    try {
                        Thread.sleep(20 + MAX_SPEED * 5 - getSpeed() * 5);
                    } catch (InterruptedException e) {
                        interrupt();
                    }
                }
            } finally {
                thread = null;
                running.set(false);
            }
        }
    }
}
