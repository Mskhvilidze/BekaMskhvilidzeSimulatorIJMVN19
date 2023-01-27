package sample.view;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.util.StatesColorMapping;
import sample.model.AbstractAutomaton;
import sample.util.Pair;

public class PopulationPanelImpl implements PopulationPanel {
    private static final double BORDER_SIZE = 10;
    public static int size = 10;
    private static final double MAX_SIZE = 2.0;
    private static final double MIN_SIZE = 0.4;
    private final AbstractAutomaton automaton;
    private Canvas canvas;
    private final int rows;
    private final int cols;
    private StatesColorMapping mapping;
    private double sizeCell = 1.0;
    private boolean disableZoomIn;
    private boolean disableZoomOut;
    int cellSize;

    public PopulationPanelImpl(AbstractAutomaton automaton, Canvas canvas, StatesColorMapping mapping) {
        this.automaton = automaton;
        rows = this.automaton.getRows();
        cols = this.automaton.getColumns();
        multiplyCellSize();
        this.canvas = canvas;
        this.canvas.setHeight(getMinStackPaneHeight());
        this.canvas.setWidth(getMinStackPaneWidth());
        this.mapping = mapping;
        paintPopulation();
    }

    public void paintPopulation() {
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        g.setStroke(Color.BLACK);
        g.setLineWidth(1);
        g.setFill(Color.WHITE);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                g.setFill(mapping.setColor(automaton.getCell(r, c).getState()));
                g.fillRect(BORDER_SIZE + c * size, BORDER_SIZE + r * size, size, size);
                g.strokeRect(BORDER_SIZE + c * size, BORDER_SIZE + r * size, size, size);
            }
        }
    }


    public double getMinStackPaneWidth() {
        return Math.min(2 * BORDER_SIZE + automaton.getColumns() * cellSize, 3000);
    }

    public double getMinStackPaneHeight() {
        return Math.min(2 * BORDER_SIZE + automaton.getRows() * cellSize, 3000);
    }

    public void incZoom() {
        if (sizeCell < MAX_SIZE) {
            incSize();
            multiplyCellSize();
            sizeCell = sizeCell + 0.1;
            this.disableZoomIn = false;
            this.disableZoomOut = false;
            Platform.runLater(() -> {
                this.canvas.setHeight(getMinStackPaneHeight());
                this.canvas.setWidth(getMinStackPaneWidth());
            });
            paintPopulation();
        } else {
            this.disableZoomIn = true;
        }
    }

    @Override
    public void repaint(int newSize) {
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        g.setStroke(Color.BLACK);
        g.setLineWidth(1);
        g.setFill(Color.WHITE);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                g.setFill(mapping.setColor(automaton.getCell(r, c).getState()));
                g.fillRect(BORDER_SIZE + c * newSize, BORDER_SIZE + r * newSize, newSize, newSize);
                g.strokeRect(BORDER_SIZE + c * newSize, BORDER_SIZE + r * newSize, newSize, newSize);
            }
        }
    }

    public void decZoom() {
        if (sizeCell > MIN_SIZE) {
            decSize();
            multiplyCellSize();
            this.disableZoomOut = false;
            this.disableZoomIn = false;
            sizeCell = sizeCell - 0.1;
            Platform.runLater(() -> {
                this.canvas.setHeight(getMinStackPaneHeight());
                this.canvas.setWidth(getMinStackPaneWidth());
            });
            paintPopulation();
        } else {
            this.disableZoomOut = true;
        }
    }

    public void center(Bounds viewPortBounds) {
        double width = viewPortBounds.getWidth();
        double height = viewPortBounds.getHeight();
        if (width > this.getMinStackPaneWidth()) {
            this.canvas.setTranslateX((width - this.getMinStackPaneWidth()) / 2);
        } else {
            this.canvas.setTranslateX(0);
        }
        if (height > this.getMinStackPaneHeight()) {
            this.canvas.setTranslateY((height - this.getMinStackPaneHeight()) / 2);
        } else {
            this.canvas.setTranslateY(0);
        }
    }

    public Pair<Integer> getCell(double x, double y) {
        if (x < size || y < size || x >= size + this.automaton.getColumns() * size || y >= size + this.automaton.getRows() * size) {
            return null;
        }
        int row = (int) ((x - size) / size);
        int col = (int) ((y - size) / size);
        return new Pair<>(row, col);
    }

    public boolean isDisableZoomIn() {
        return disableZoomIn;
    }

    public boolean isDisableZoomOut() {
        return disableZoomOut;
    }

    private void multiplyCellSize() {
        cellSize = size * 2;
    }

    private static void incSize() {
        size++;
    }

    private static void decSize() {
        size--;
    }
}