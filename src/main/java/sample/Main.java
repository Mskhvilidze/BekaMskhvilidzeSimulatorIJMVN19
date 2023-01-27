package sample;

import sample.presenter.EditorPresenter;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;


public class Main {
    private static final String[] array = {EditorPresenter.PATH, EditorPresenter.SOURCE};

    public static void main(String[] args) {
        for (String path : array) {
            createDirectory(path);
        }
        App.main(args);
    }

    static void createDirectory(String path) {
        File file = new File(path);
        if (!file.exists()) {
            boolean mkdir = file.mkdirs();
            if (mkdir) {
                System.out.println("Directory creation " + path.substring(0, path.length() - 1));
            }
        } else {
            System.out.println("Automata exists " + path.substring(0, path.length() - 1));
        }
    }
}
