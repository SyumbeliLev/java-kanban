package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.adapter.LocalDateTimeAdapter;
import manager.historyManagers.HistoryManager;
import manager.historyManagers.InMemoryHistoryManager;
import manager.taskManagers.FileBackedTasksManager;
import manager.taskManagers.HttpTaskManager;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {
    private Managers() {
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static HttpTaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8078/");
    }
    public static FileBackedTasksManager getDefaultFileBackedManager() {
        File file = new File("save.csv");
        return new FileBackedTasksManager(file);
    }
    public static Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}
