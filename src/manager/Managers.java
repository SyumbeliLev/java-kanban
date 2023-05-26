package manager;

import manager.historyManagers.HistoryManager;
import manager.historyManagers.InMemoryHistoryManager;
import manager.taskManagers.InMemoryTaskManager;
import manager.taskManagers.TaskManager;

public class Managers {
    private Managers() {
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return  new InMemoryTaskManager();
    }
}
