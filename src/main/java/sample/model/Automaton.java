package sample.model;

public interface Automaton {

    void initMatrix(AbstractAutomaton.Cell[][] cells);
    int getNumberOfStates();
}
