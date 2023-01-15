package sample.model;

import java.io.Serializable;

public interface Automaton extends Serializable {

    void initMatrix(AbstractAutomaton.Cell[][] cells);
    int getNumberOfStates();
    int getRows();
    int getColumns();
    void changeSize(int rows, int columns);
    boolean isTorus();
    void setTorus(boolean isTorus);
    boolean isMooreNeighborHood();
    void setRows(int rows);
    void setColumns(int columns);
    void setMooreNeighborHood(boolean mooreNeighborHood);
    void clearPopulation();
    void randomPopulation();
    void nextGeneration();
    void setNumberOfStates(int numberOfStates);
    void setNewState(int row, int columns, int state);
}
