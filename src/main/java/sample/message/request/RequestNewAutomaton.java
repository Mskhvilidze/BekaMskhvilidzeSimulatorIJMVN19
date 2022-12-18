package sample.message.request;

import javafx.stage.Stage;
import sample.message.AbstractNewAutomatonMessage;
import sample.model.AbstractAutomaton;

public class RequestNewAutomaton extends AbstractNewAutomatonMessage {

    private Stage stage;
    String name;
    public RequestNewAutomaton(AbstractAutomaton automaton, Stage stage, String name) {
        super(automaton);
        this.stage = stage;
        this.name = name;
    }

    public Stage getStage() {
        return stage;
    }

    public String getName() {
        return name;
    }
}
