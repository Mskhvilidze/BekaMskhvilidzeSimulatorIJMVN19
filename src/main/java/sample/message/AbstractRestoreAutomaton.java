package sample.message;

import java.util.List;

public class AbstractRestoreAutomaton<T> implements Message{
    private transient List<T> list;

    public AbstractRestoreAutomaton(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }
}
