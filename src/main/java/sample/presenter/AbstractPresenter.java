package sample.presenter;

import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AbstractPresenter {
    protected int a = 0;
    protected int b = 0;
    protected int activeCell = 0;
    protected Random random = new Random();
    protected Service service;
    protected Map<String, Stage> map = new HashMap<>();

    public void setService(Service service) {
        this.service = service;
    }

}
