package sample.util;

import javafx.scene.paint.Color;

public class StatesColorMapping {
    private Color[] colors;

    public StatesColorMapping(int numberOfStates) {
        colors = new Color[mColors().length];
        for (int i = 0; i < numberOfStates; i++) {
            String color = mColors()[i];
            colors[i] = Color.web(color);
        }
    }

    public Color setColor(int state) {
        return colors[state];
    }

    private String[] mColors() {
        return new String[]{
                "#ffffff", // Weiß
                "#000000", // Schwarz
                "#0f7b4e", // grün
                "#b6d4e1", // mauve
                "#f9ee3d", // gelb
                "#5f85c4", // rot + grün + blau
                "#eaa8fe", // rot + blau
                "#2f8789", // grün + blau
                "#213968", // dunkel blau
                "#535c79", // grau
        };
    }
}
