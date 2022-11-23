package sample.model;

import java.util.Scanner;

public class GameOfLifeAutomaton extends Automaten {

    public GameOfLifeAutomaton() {
        this(50, 50, true);
    }

    public GameOfLifeAutomaton(int rows, int columns, boolean isTorus) {
        super(rows, columns, 2, true, isTorus);
    }

    @Override
    public Cell transform(Cell cell, Cell[] neighbors) throws Throwable {
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

    public static void main(String[] args) throws Throwable {
        Automaten automaten = new GameOfLifeAutomaton(8, 8, true);
        //Automaten automaten = new KruemelmonsterAutomaten(8, 8, true);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Random");
        automaten.randomPopulation();
        scanner.next();
        automaten.print();
        while (true) {
            System.out.println("newGeneration");
            scanner.next();
            automaten.nextGeneration();
            automaten.print();
        }
    }
}