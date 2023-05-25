package manager.historyManagers;

import model.Task;

import java.util.List;

public interface HistoryManager {
    public void remove(int id);
    public void add(Task task);

    public List<Task> getHistory();
}
