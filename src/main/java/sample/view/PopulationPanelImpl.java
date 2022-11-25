package sample.view;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.StatesColorMapping;
import sample.model.AbstractAutomaton;
import sample.util.Pair;

public class PopulationPanelImpl implements PopulationPanel {
    private static final int CANVAS_WIDTH_AND_HEIGHT = 16;
    public static final int BORDER_SIZE = 10;
    private static final double GAME_WIDTH = 15;
    private static final double GAME_HEIGHT = 15;
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

    public PopulationPanelImpl(AbstractAutomaton automaten, Canvas canvas, StatesColorMapping mapping) {
        this.automaton = automaten;
        rows = this.automaton.getRows();
        cols = this.automaton.getColumns();
        canvas.setHeight(getMinCanvasHeight());
        canvas.setWidth(getMinCanvasWidth());
        this.canvas = canvas;
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
                g.fillRect(BORDER_SIZE + c * GAME_WIDTH, BORDER_SIZE + r * GAME_HEIGHT, GAME_WIDTH, GAME_HEIGHT);
                g.strokeRect(BORDER_SIZE + c * GAME_WIDTH, BORDER_SIZE + r * GAME_HEIGHT, GAME_WIDTH, GAME_HEIGHT);
            }
        }
    }

    public int getMinCanvasWidth() {
        return Math.min(cols * CANVAS_WIDTH_AND_HEIGHT, 1360);
    }

    public int getMinCanvasHeight() {
        return Math.min(rows * CANVAS_WIDTH_AND_HEIGHT, 1360);
    }

    public double getMinStackPaneWidth() {
        return Math.min(cols * CANVAS_WIDTH_AND_HEIGHT, 1360) + sizeCell * 10 * 1.5;
    }

    public double getMinStackPaneHeight() {
        return Math.min(rows * CANVAS_WIDTH_AND_HEIGHT, 1360) + sizeCell * 10 * 1.5;
    }

    public void incZoom() {
        if (sizeCell < MAX_SIZE && getMinStackPaneWidth() <= 735.0) {
            sizeCell = sizeCell + 0.1;
            this.disableZoomIn = false;
            this.disableZoomOut = false;
            Platform.runLater(() -> {
                this.canvas.setScaleX(sizeCell);
                this.canvas.setScaleY(sizeCell);
            });
            paintPopulation();
        } else {
            this.disableZoomIn = true;
        }
    }

    public void decZoom() {
        if (sizeCell > MIN_SIZE) {
            this.disableZoomOut = false;
            this.disableZoomIn = false;
            sizeCell = sizeCell - 0.1;
            Platform.runLater(() -> {
                this.canvas.setScaleX(sizeCell);
                this.canvas.setScaleY(sizeCell);
            });
            paintPopulation();
        } else {
            this.disableZoomOut = true;
        }
    }

    public void center(Bounds viewPortBounds) {
        double width = viewPortBounds.getWidth();
        double height = viewPortBounds.getHeight();
        if (width > this.getMinCanvasWidth()) {
            this.canvas.setTranslateX((width - this.getMinCanvasWidth()) / 2);
        } else {
            this.canvas.setTranslateX(0);
        }
        if (height > this.getMinCanvasHeight()) {
            this.canvas.setTranslateY((height - this.getMinCanvasHeight()) / 2);
        } else {
            this.canvas.setTranslateY(0);
        }

    }

    public Pair<Integer> getCell(double x, double y) {
        if (x < 15 || y < 15 || x >= 15 + this.automaton.getColumns() * 15 || y >= 15 + this.automaton.getRows() * 15) {
            return null;
        }
        int row = (int) ((x - 15) / 15);
        int col = (int) ((y - 15) / 15);
        return new Pair<>(row, col);
    }

    public boolean isDisableZoomIn() {
        return disableZoomIn;
    }

    public boolean isDisableZoomOut() {
        return disableZoomOut;
    }
}