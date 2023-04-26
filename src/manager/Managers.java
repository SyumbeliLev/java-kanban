package manager;

import manager.historyManagers.HistoryManager;
import manager.historyManagers.InMemoryHistoryManager;
import manager.taskManagers.InMemoryTaskManager;
import manager.taskManagers.TaskManager;

public class Managers {
    private Managers() {
    }

    public static HistoryManager getDefaultHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        return historyManager;
    }

    public static TaskManager getDefault() {
        TaskManager taskManager = new InMemoryTaskManager();
        return taskManager;
    }
}
