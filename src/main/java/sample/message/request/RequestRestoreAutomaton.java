package sample.message.request;

import sample.message.AbstractRestoreAutomaton;

import java.util.List;

public class RequestRestoreAutomaton<T> extends AbstractRestoreAutomaton<T> {
    public RequestRestoreAutomaton(List<T> list) {
        super(list);
    }
}
