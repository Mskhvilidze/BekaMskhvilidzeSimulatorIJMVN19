package sample.model;

public class GameOfLifeAutomaton extends AbstractAutomaton {

    public GameOfLifeAutomaton(int rows, int columns, boolean isTorus) {
        super(rows, columns, 2, true, isTorus);
    }

    @Override
    public Cell transform(Cell cell, Cell[] neighbors) {
        int numberOfNeighbors = 0;
        for (Cell neighbor : neighbors) {
            if (neighbor.getState() == 1) {
                numberOfNeighbors++;
            }
        }
        if (cell.getState() == 0) {
            if (numberOfNeighbors == 3) {
                return new Cell(1);
            } else {
                return new Cell(cell);
            }
        } else {
            if (numberOfNeighbors < 2 || numberOfNeighbors > 3) {
                return new Cell(0);
            } else {
                return new Cell(cell);
            }
        }
    }
}