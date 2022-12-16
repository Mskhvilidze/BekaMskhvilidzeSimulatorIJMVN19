package sample.message.request;

import sample.message.AbstractNewAutomatonMessage;
import sample.model.AbstractAutomaton;

public class RequestNewAutomaton extends AbstractNewAutomatonMessage {

    public RequestNewAutomaton(AbstractAutomaton automaton) {
        super(automaton);
    }
}
