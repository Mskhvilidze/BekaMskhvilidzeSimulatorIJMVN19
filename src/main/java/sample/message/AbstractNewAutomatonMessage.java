package sample.message;

import sample.model.AbstractAutomaton;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractNewAutomatonMessage that = (AbstractNewAutomatonMessage) o;
        return Objects.equals(automaton, that.automaton);
    }

    @Override
    public int hashCode() {
        return Objects.hash(automaton);
    }
}
