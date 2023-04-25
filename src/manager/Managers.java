package manager;

import manager.historyManagers.HistoryManager;
import manager.historyManagers.InMemoryHistoryManager;
import manager.taskManagers.InMemoryTaskManager;
import manager.taskManagers.TaskManager;

public class Managers{

    static TaskManager taskManager =  new InMemoryTaskManager();
    static HistoryManager historyManager = new InMemoryHistoryManager();
    public static HistoryManager getDefaultHistory(){
      return historyManager;
    }
    public static TaskManager getDefault(){
        return taskManager;
    }
}
