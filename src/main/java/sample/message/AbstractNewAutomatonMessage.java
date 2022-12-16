package sample.message;

import sample.model.AbstractAutomaton;

public class AbstractNewAutomatonMessage implements Message {
    private AbstractAutomaton automaton;

    public AbstractNewAutomatonMessage(AbstractAutomaton automaton) {
        this.automaton = automaton;
    }

    public void setAutomaton(AbstractAutomaton automaton) {
        this.automaton = automaton;
    }

    public AbstractAutomaton getAutomaton() {
        return automaton;
    }
}
