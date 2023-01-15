package sample.model;

import java.io.Serializable;
import java.util.*;

public abstract class AbstractAutomaton implements Automaton {

    private int rows;
    private int columns;
    private int numberOfStates;
    private boolean isMooreNeighborHood;
    private boolean isTorus;
    private Cell[][] cells;
    private Random rand = new Random();

    /**
     * Konstruktor
     *
     * @param rows                Anzahl an Reihen
     * @param columns             Anzahl an Spalten
     * @param numberOfStates      Anzahl an Zuständen; die Zustände
     *                            des Automaten
     *                            sind dann die Werte 0 bis
     *                            numberOfStates-1
     * @param isMooreNeighborHood true, falls der Automat die
     *                            Moore-Nachbarschaft
     *                            benutzt; false, falls der Automat die
     *                            von-Neumann-Nachbarschaft benutzt
     */
    public AbstractAutomaton(int rows, int columns, int numberOfStates, boolean isMooreNeighborHood) {
        this.rows = rows;
        this.columns = columns;
        this.numberOfStates = numberOfStates;
        this.isMooreNeighborHood = isMooreNeighborHood;
        this.cells = new Cell[rows][columns];
        initMatrix(this.cells);
    }

    public synchronized void initMatrix(Cell[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                this.cells[i][j] = new Cell(0);
            }
        }
    }

    public abstract Cell transform(Cell cell, Cell[] neighbors);

    /**
     * Liefert die Anzahl an Zuständen des Automaten; gültige Zustände sind
     * int-Werte zwischen 0 und Anzahl-1
     *
     * @return die Anzahl an Zuständen des Automaten
     */
    public synchronized int getNumberOfStates() {
        return this.numberOfStates;
    }

    public synchronized void setRows(int rows) {
        this.rows = rows;
    }

    public synchronized void setColumns(int columns) {
        this.columns = columns;
    }

    public void setCells(int rows, int columns) {
        this.cells = new Cell[rows][columns];
    }

    /**
     * Liefert die Anzahl an Reihen
     *
     * @return die Anzahl an Reihen
     */
    public synchronized int getRows() {
        return this.rows;
    }

    /**
     * Liefert die Anzahl an Spalten
     *
     * @return die Anzahl an Spalten
     */
    public synchronized int getColumns() {
        return this.columns;
    }

    /**
     * Ändert die Größe des Automaten; Achtung: aktuelle Belegungen nicht
     * gelöschter Zellen sollen beibehalten werden; neue Zellen sollen im
     * Zustand 0 erzeugt werden
     *
     * @param rows    die neue Anzahl an Reihen
     * @param columns die neue Anzahl an Spalten
     */
    public synchronized void changeSize(int rows, int columns) {
        Cell[][] temp = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i < this.rows && j < this.columns) {
                    temp[i][j] = this.cells[i][j];
                } else {
                    temp[i][j] = new Cell(0);
                }
            }
        }
        this.cells = temp;
        this.rows = rows;
        this.columns = columns;
    }

    /**
     * Liefert Informationen, ob der Automat als Torus betrachtet wird
     *
     * @return true, falls der Automat als Torus betrachtet wird; false
     * sonst
     */
    public synchronized boolean isTorus() {
        return this.isTorus;
    }

    /**
     * Ändert die Torus-Eigenschaft des Automaten
     *
     * @param isTorus true, falls der Automat als Torus betrachtet wird;
     *                false sonst
     */
    public synchronized void setTorus(boolean isTorus) {
        this.isTorus = isTorus;
    }

    /**
     * Liefert Informationen über die Nachbarschaft-Eigenschaft des
     * Automaten
     * (Hinweis: Die Nachbarschaftseigenschaft kann nicht verändert werden)
     *
     * @return true, falls der Automat die Moore-Nachbarschaft berücksicht;
     * false, falls er die von-Neumann-Nachbarschaft berücksichtigt
     */
    public synchronized boolean isMooreNeighborHood() {
        return this.isMooreNeighborHood;
    }

    /**
     * setzt alle Zellen in den Zustand 0
     */
    public synchronized void clearPopulation() {
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.columns; c++) {
                this.cells[r][c].setState(0);
            }
        }
    }

    /**
     * setzt für jede Zelle einen zufällig erzeugten Zustand
     */
    public synchronized void randomPopulation() {
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.columns; c++) {
                this.cells[r][c].setState(rand.nextInt(numberOfStates));
            }
        }
    }

    public synchronized void setMooreNeighborHood(boolean mooreNeighborHood) {
        isMooreNeighborHood = mooreNeighborHood;
    }

    /**
     * Liefert eine Zelle des Automaten
     *
     * @param row    Reihe der Zelle
     * @param column Spalte der Zelle
     * @return xv
     */
    public synchronized Cell getCell(int row, int column) {
        return this.cells[row][column];
    }

    /**
     * Aendert den Zustand einer Zelle
     *
     * @param row    Reihe der Zelle
     * @param column Spalte der Zelle
     * @param state  neuer Zustand der Zelle
     */
    public synchronized void setState(int row, int column, int state) {
        if (this.cells[row][column].getState() != state) {
            this.cells[row][column].setState(state);
        }
    }

    /**
     * Aendert den Zustand eines ganzen Bereichs von Zellen
     *
     * @param fromRow    Reihe der obersten Zelle
     * @param fromColumn Spalte der obersten Zelle
     * @param toRow      Reihe der untersten Zelle
     * @param toColumn   Spalte der untersten Zelle
     * @param state      neuer Zustand der Zellen
     */
    public synchronized void setState(int fromRow, int fromColumn, int toRow, int toColumn, int state) {
        for (int r = fromRow; r <= toRow; r++) {
            for (int c = fromColumn; c <= toColumn; c++) {
                if (this.cells[r][c].getState() != state) {
                    this.cells[r][c].setState(state);
                }
            }
        }
    }

    public synchronized void setNewState(int row, int columns, int state) {
        this.cells[row][columns] = new Cell(state);
    }

    /**
     * überführt den Automaten in die nächste Generation; ruft dabei die
     * abstrakte Methode "transform" für alle Zellen auf; Hinweis: zu
     * berücksichtigen sind die Nachbarschaftseigenschaft und die
     * Torus-Eigenschaft des Automaten
     */
    public synchronized void nextGeneration() {
        Cell[][] temp = new Cell[this.cells.length][this.cells[0].length];
        if (this.isMooreNeighborHood()) {
            torusMoorNeighborsChecker(temp);
        } else {
            neumannNeighborsChecker(temp);
        }
    }

    private void torusMoorNeighborsChecker(Cell[][] temp) {
        if (isTorus) {
            for (int r = 0; r < this.cells.length; r++) {
                for (int c = 0; c < this.cells[r].length; c++) {
                    temp[r][c] = new Cell(this.transform(getCell(r, c), this.getTorusMoorNeighbors(r, c)));
                }
            }
        } else {
            for (int r = 0; r < this.cells.length; r++) {
                for (int c = 0; c < this.cells[r].length; c++) {
                    temp[r][c] = new Cell(this.transform(getCell(r, c), this.getMoorNeighbors(r, c)));
                }
            }
        }
        this.cells = temp;
    }

    private void neumannNeighborsChecker(Cell[][] temp) {
        for (int r = 0; r < this.cells.length; r++) {
            for (int c = 0; c < this.cells[r].length; c++) {
                temp[r][c] = new Cell(this.transform(getCell(r, c), this.getNeumannNeighbors(r, c)));
            }
        }
        this.cells = temp;
    }

    private Cell[] getTorusMoorNeighbors(int row, int col) {
        List<Cell> list = new ArrayList<>();
        int resultOfTempRowAndRows =
                (row + this.rows - 1) > this.rows ? (row + this.rows - 1) - this.rows : this.rows - (row + this.rows - 1);
        int resultOfTempColAndColumns =
                (col + this.columns - 1) > this.columns ? (col + this.columns - 1) - this.columns : this.columns - (col + this.columns - 1);
        int resultOfTempRowAndRows1 = (row + 1) > this.rows ? (row + 1) - this.rows : this.rows - (row + 1);
        int resultOfTempColAndColumns1 = (col + 1) > this.columns ? (col + 1) - this.columns : this.columns - (col + 1);

        list.add(this.cells[resultOfTempRowAndRows1][resultOfTempColAndColumns]);
        list.add(this.cells[resultOfTempRowAndRows1][col]);
        list.add(this.cells[resultOfTempRowAndRows1][resultOfTempColAndColumns1]);
        list.add(this.cells[resultOfTempRowAndRows][resultOfTempColAndColumns]);
        list.add(this.cells[resultOfTempRowAndRows][col]);
        list.add(this.cells[resultOfTempRowAndRows][resultOfTempColAndColumns1]);
        list.add(this.cells[row][resultOfTempColAndColumns]);
        list.add(this.cells[row][resultOfTempColAndColumns1]);
        return list.toArray(new Cell[8]);
    }

    private boolean aLive(int i, int j) {
        return i < rows && j < columns && i >= 0 && j >= 0 && this.cells[i][j].getState() == 1;
    }

    private Cell[] getMoorNeighbors(int row, int col) {
        List<Cell> array = new ArrayList<>();
        if (aLive(row - 1, col - 1)) {
            array.add(this.cells[row - 1][col - 1]);
        }
        if (aLive(row - 1, col)) {
            array.add(this.cells[row - 1][col]);
        }

        if (aLive(row - 1, col + 1)) {
            array.add(this.cells[row - 1][col + 1]);
        }

        if (aLive(row, col - 1)) {
            array.add(this.cells[row][col - 1]);
        }

        if (aLive(row, col + 1)) {
            array.add(this.cells[row][col + 1]);
        }

        if (aLive(row + 1, col - 1)) {
            array.add(this.cells[row + 1][col - 1]);
        }

        if (aLive(row + 1, col)) {
            array.add(this.cells[row + 1][col]);
        }

        if (aLive(row + 1, col + 1)) {
            array.add(this.cells[row + 1][col + 1]);
        }
        return array.toArray(new Cell[0]);
    }

    private Cell[] getNeumannNeighbors(int row, int col) {
        List<Cell> list = new ArrayList<>();

        list.add(row > 0 ? this.cells[row - 1][col] : null);
        list.add(col > 0 ? this.cells[row][col - 1] : null);
        list.add(row < this.rows - 1 ? this.cells[row + 1][col] : null);
        list.add(col < this.columns - 1 ? this.cells[row][col + 1] : null);

        return list.stream().filter(Objects::nonNull).toArray(Cell[]::new);
    }

    public synchronized void setNumberOfStates(int numberOfStates) {
        this.numberOfStates = numberOfStates;
    }

    public static class Cell implements Serializable {
        private int state;

        /**
         * Inizialisiert die Zelle im übergebenen Zustand
         *
         * @param state Zustand
         */
        public Cell(int state) {
            this.state = state;
        }

        /**
         * Copy-Konstruktor; initialisiert die Zelle mit dem Zustand der
         * übergebenen Zel
         *
         * @param cell Zelle
         */
        public Cell(Cell cell) {
            this.state = cell.state;
        }

        /**
         * Liefert den Zustand der Zelle
         *
         * @return Zustand der Zelle
         */
        public synchronized int getState() {
            return state;
        }

        /**
         * Ändert den Zustand der Zelle
         *
         * @param state der neue Zustand der Zelle
         */
        public synchronized void setState(int state) {
            this.state = state;
        }

    }
}
