package sample.model;

import sample.model.Automaten;
import sample.model.Cell;

public class KruemelmonsterAutomaten extends Automaten {
    public KruemelmonsterAutomaten() {
        this(8, 8, true);
    }

    public KruemelmonsterAutomaten(int rows, int columns, boolean isTorus) {
        super(rows, columns, 8, false, isTorus);
    }

    public Cell transform(Cell cell, Cell[] neighbors) {
        int state = 0;
        for (Cell neighbor : neighbors) {
            state = (cell.getState() + 1) < getNumberOfStates() ? cell.getState() + 1 : 0;
            if (state == neighbor.getState()) {
                return new Cell(neighbor.getState());
            }
        }
        return new Cell(cell);
    }
}