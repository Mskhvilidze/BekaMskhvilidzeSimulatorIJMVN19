package sample.model;

public interface Automaton {

    void initMatrix(AbstractAutomaton.Cell[][] cells);
    int getNumberOfStates();
    int getRows();
    int getColumns();
    void changeSize(int rows, int columns);
    boolean isTorus();
    void setTorus(boolean isTorus);
    boolean isMooreNeighborHood();
    void clearPopulation();
    void randomPopulation();
    void nextGeneration();
    void setNumberOfStates(int numberOfStates);
}
