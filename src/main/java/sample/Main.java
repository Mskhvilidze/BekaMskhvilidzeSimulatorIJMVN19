package sample;

import sample.presenter.EditorPresenter;
import sample.presenter.Service;
import sample.util.AutomatonHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {
    private static final String[] array = {EditorPresenter.PATH, EditorPresenter.SOURCE};
    private static final String[] nameOfGame = {"GameOfLifeAutomaton.txt", "KruemelmonsterAutomaton.txt"};

    public static void main(String[] args) {
        for (String path : array) {
            createDirectory(path);
        }
        createFiles(nameOfGame);
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

    static void createFiles(String... basketForGameName){
        for (String s : basketForGameName) {
            Path file = Paths.get(EditorPresenter.SOURCE + s);
            File searchFile = new File(file.toUri());
            if (!searchFile.exists()) {
                AutomatonHelper automatonHelper = null;
                try {
                   automatonHelper = new AutomatonHelper(searchFile);
                    automatonHelper.createAutomaton(s);
                } catch (IOException e) {
                    Service.alert("Main! Files Txt konnten nicht erstellt werden", "");
                    e.printStackTrace();
                }

            }
        }
    }
}
