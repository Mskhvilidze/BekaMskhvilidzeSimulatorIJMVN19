package sample.model;

public class KruemelmonsterAutomaton extends AbstractAutomaton {

    public KruemelmonsterAutomaton(int rows, int columns, boolean isTorus) {
        super(rows, columns, 8, isTorus);
    }

    public Cell transform(Cell cell, Cell[] neighbors) {
        int state;
        for (Cell neighbor : neighbors) {
            state = (cell.getState() + 1) < getNumberOfStates() ? cell.getState() + 1 : 0;
            if (state == neighbor.getState()) {
                return new Cell(neighbor.getState());
            }
        }
        return new Cell(cell);
    }
}