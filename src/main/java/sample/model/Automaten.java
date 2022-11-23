package sample.model;

import java.util.*;

public abstract class Automaten {

    private int ROWS;
    private int COLUMNS;
    private int numberOfStates;
    private boolean isMooreNeighborHood;
    private boolean isTorus;
    private Cell[][] cells;

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
     * @param isTorus             true, falls die Zellen als
     *                            Torus betrachtet werden
     */
    public Automaten(int rows, int columns, int numberOfStates, boolean isMooreNeighborHood, boolean isTorus) {
        this.ROWS = rows;
        this.COLUMNS = columns;
        this.numberOfStates = numberOfStates;
        this.isMooreNeighborHood = isMooreNeighborHood;
        this.isTorus = isTorus;
        this.cells = new Cell[rows][columns];
        initMatrix(this.cells);
    }

    public void initMatrix(Cell[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                this.cells[i][j] = new Cell(0);
            }
        }
    }

    public abstract Cell transform(Cell cell, Cell[] neighbors) throws Throwable;

    /**
     * Liefert die Anzahl an Zuständen des Automaten; gültige Zustände sind
     * int-Werte zwischen 0 und Anzahl-1
     *
     * @return die Anzahl an Zuständen des Automaten
     */
    public int getNumberOfStates() {
        return this.numberOfStates;
    }

    /**
     * Liefert die Anzahl an Reihen
     *
     * @return die Anzahl an Reihen
     */
    public int getROWS() {
        return this.ROWS;
    }

    /**
     * Liefert die Anzahl an Spalten
     *
     * @return die Anzahl an Spalten
     */
    public int getCOLUMNS() {
        return this.COLUMNS;
    }

    /**
     * Ändert die Größe des Automaten; Achtung: aktuelle Belegungen nicht
     * gelöschter Zellen sollen beibehalten werden; neue Zellen sollen im
     * Zustand 0 erzeugt werden
     *
     * @param rows    die neue Anzahl an Reihen
     * @param columns die neue Anzahl an Spalten
     */
    public void changeSize(int rows, int columns) {
        Cell[][] temp = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i < this.ROWS && j < this.COLUMNS) {
                    temp[i][j] = this.cells[i][j];
                } else {
                    temp[i][j] = new Cell(0);
                }
            }
        }
        this.cells = temp;
        this.ROWS = rows;
        this.COLUMNS = columns;
    }

    /**
     * Liefert Informationen, ob der Automat als Torus betrachtet wird
     *
     * @return true, falls der Automat als Torus betrachtet wird; false
     * sonst
     */
    public boolean isTorus() {
        return this.isTorus;
    }

    /**
     * Ändert die Torus-Eigenschaft des Automaten
     *
     * @param isTorus true, falls der Automat als Torus betrachtet wird;
     *                false sonst
     */
    public void setTorus(boolean isTorus) {
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
    public boolean isMooreNeighborHood() {
        return this.isMooreNeighborHood;
    }

    /**
     * setzt alle Zellen in den Zustand 0
     */
    public void clearPopulation() {
        for (int r = 0; r < this.ROWS; r++) {
            for (int c = 0; c < this.COLUMNS; c++) {
                this.cells[r][c].setState(0);
            }
        }
    }

    /**
     * setzt für jede Zelle einen zufällig erzeugten Zustand
     */
    public void randomPopulation() {
        Random rand = new Random();
        for (int r = 0; r < this.ROWS; r++) {
            for (int c = 0; c < this.COLUMNS; c++) {
                this.cells[r][c].setState(rand.nextInt(numberOfStates));
            }
        }
    }

    /**
     * Liefert eine Zelle des Automaten
     *
     * @param row    Reihe der Zelle
     * @param column Spalte der Zelle
     * @return Cell-Objekt an Position row/column
     */
    public Cell getCell(int row, int column) {
        return this.cells[row][column];
    }

    /**
     * Aendert den Zustand einer Zelle
     *
     * @param row    Reihe der Zelle
     * @param column Spalte der Zelle
     * @param state  neuer Zustand der Zelle
     */
    public void setState(int row, int column, int state) {
        boolean swap = false;
        if (this.cells[row][column].getState() != state) {
            this.cells[row][column].setState(state);
            swap = true;
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
    public void setState(int fromRow, int fromColumn, int toRow, int toColumn, int state) {
        boolean swap = false;
        for (int r = fromRow; r <= toRow; r++) {
            for (int c = fromColumn; c <= toColumn; c++) {
                if (this.cells[r][c].getState() != state) {
                    this.cells[r][c].setState(state);
                    swap = true;
                }
            }
        }
    }

    /**
     * überführt den Automaten in die nächste Generation; ruft dabei die
     * abstrakte Methode "transform" für alle Zellen auf; Hinweis: zu
     * berücksichtigen sind die Nachbarschaftseigenschaft und die
     * Torus-Eigenschaft des Automaten
     *
     * @throws Throwable Exceptions der transform-Methode werden
     *                   weitergeleitet
     */
    public void nextGeneration() throws Throwable {
        Cell[][] temp = new Cell[this.cells.length][this.cells[0].length];
        if (this.isMooreNeighborHood) {
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
        } else {
            for (int r = 0; r < this.cells.length; r++) {
                for (int c = 0; c < this.cells[r].length; c++) {
                    temp[r][c] = new Cell(this.transform(getCell(r, c), this.getNeumannNeighbors(r, c)));
                }
            }
        }
        this.cells = temp;
    }

    private Cell[] getTorusMoorNeighbors(int row, int col) {
        List<Cell> list = new ArrayList<>();
        int result_Of_tempRow_And_Rows =
                (row + this.ROWS - 1) > this.ROWS ? (row + this.ROWS - 1) - this.ROWS : this.ROWS - (row + this.ROWS - 1);
        int result_Of_tempCol_And_Columns =
                (col + this.COLUMNS - 1) > this.COLUMNS ? (col + this.COLUMNS - 1) - this.COLUMNS : this.COLUMNS - (col + this.COLUMNS - 1);
        int result_Of_tempRow_And_Rows1 = (row + 1) > this.ROWS ? (row + 1) - this.ROWS : this.ROWS - (row + 1);
        int result_Of_tempCol_And_Columns1 = (col + 1) > this.COLUMNS ? (col + 1) - this.COLUMNS : this.COLUMNS - (col + 1);

        list.add(this.cells[result_Of_tempRow_And_Rows1][result_Of_tempCol_And_Columns]);
        list.add(this.cells[result_Of_tempRow_And_Rows1][col]);
        list.add(this.cells[result_Of_tempRow_And_Rows1][result_Of_tempCol_And_Columns1]);
        list.add(this.cells[result_Of_tempRow_And_Rows][result_Of_tempCol_And_Columns]);
        list.add(this.cells[result_Of_tempRow_And_Rows][col]);
        list.add(this.cells[result_Of_tempRow_And_Rows][result_Of_tempCol_And_Columns1]);
        list.add(this.cells[row][result_Of_tempCol_And_Columns]);
        list.add(this.cells[row][result_Of_tempCol_And_Columns1]);
        return list.toArray(new Cell[8]);
    }

    private boolean aLive(int i, int j) {
        boolean valid = false;
        if (i < ROWS && j < COLUMNS && i >= 0 && j >= 0 && this.cells[i][j].getState() == 1) {
            valid = true;
        }
        return valid;
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
        list.add(row < this.ROWS - 1 ? this.cells[row + 1][col] : null);
        list.add(col < this.COLUMNS - 1 ? this.cells[row][col + 1] : null);

        return list.stream().filter(Objects::nonNull).toArray(Cell[]::new);
    }

    public void setNumberOfStates(int numberOfStates) {
        this.numberOfStates = numberOfStates;
    }

    public void print() {
        for (Cell[] cell : this.cells) {
            for (Cell value : cell) {
                System.out.print(value.getState() + "  ");
            }
            System.out.println(" ");
        }
    }

    public static class Cell {
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
        Cell(Cell cell) {
            this.state = cell.state;
        }

        /**
         * Liefert den Zustand der Zelle
         *
         * @return Zustand der Zelle
         */
        public int getState() {
            return state;
        }

        /**
         * Ändert den Zustand der Zelle
         *
         * @param state der neue Zustand der Zelle
         */
        public void setState(int state) {
            this.state = state;
        }

    }
}
