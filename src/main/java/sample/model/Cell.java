package sample.model;

/**
 * Klasse zur Repräsentation einer Zelle eines zellulären Automaten
 */
public class Cell {

    private int state;

    /**
     * Inizialisiert die Zelle im übergebenen Zustand
     * @param state  Zustand
     */
    public Cell(int state) {
        this.state = state;
    }

    /**
     * Copy-Konstruktor; initialisiert die Zelle mit dem Zustand der
     * übergebenen Zel
     * @param cell Zelle
     */
    public Cell(Cell cell) {
        this.state = cell.state;
    }

    /**
     * Liefert den Zustand der Zelle
     * @return Zustand der Zelle
     */
    public int getState() {
        return state;
    }

    /**
     *  Ändert den Zustand der Zelle
     * @param state der neue Zustand der Zelle
     */
    public void setState(int state) {
        this.state = state;
    }

}
