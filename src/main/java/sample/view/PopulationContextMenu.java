package sample.view;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import sample.model.AbstractAutomaton;
import sample.model.Callable;
import sample.presenter.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class PopulationContextMenu extends ContextMenu {
    public PopulationContextMenu(AbstractAutomaton automaton, int col, int row, PopulationPanel p) {
        Object[] o = new Object[]{row, col};
        List<Method> methods = getMethods(automaton);
        run(methods, automaton, p, o);
    }

    private void run(List<Method> methods, AbstractAutomaton automaton, PopulationPanel p, Object... o) {
        for (Method method : methods) {
            Label lbl = new Label(method.getName());
            CustomMenuItem item = new CustomMenuItem(lbl);
            lbl.setOnMouseClicked(event -> {
                try {
                    method.setAccessible(true);
                    method.invoke(automaton, o);
                    p.paintPopulation();
                } catch (IllegalAccessException | InvocationTargetException e) {
                    Service.alert("Beim Ausfuehren der Methode ist ein Fehler aufgetreten!", "");
                }
            });
            getItems().add(item);
        }
    }

    private List<Method> getMethods(AbstractAutomaton automaton) {
        List<Method> res = new ArrayList<>();
        for (Method method : automaton.getClass().getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 2 && method.isAnnotationPresent(Callable.class) &&
                !Modifier.isAbstract(method.getModifiers()) && !Modifier.isStatic(method.getModifiers())) {
                res.add(method);
            }
        }
        return res;
    }
}
