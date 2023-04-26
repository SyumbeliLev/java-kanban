package manager.historyManagers;

import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history = new LinkedList<>();

    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(history);
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.size() == 10) {
                history.remove(0);
            }
            history.add(task);

        }
    }
}
