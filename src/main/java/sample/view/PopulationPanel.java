package sample.view;

import javafx.geometry.Bounds;
import sample.util.Pair;

public interface PopulationPanel {
    void paintPopulation();

    int getMinCanvasWidth();

    int getMinCanvasHeight();

    double getMinStackPaneWidth();

    double getMinStackPaneHeight();

    void incZoom();

    void decZoom();

    void center(Bounds viewPortBounds);

    boolean isDisableZoomIn();

    boolean isDisableZoomOut();
    Pair<Integer> getCell(double x, double y);
}
