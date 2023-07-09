package manager.taskManagers;

import com.google.gson.Gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import manager.Managers;

import model.Epic;
import model.Subtack;
import model.Task;
import org.jetbrains.annotations.NotNull;
import server.KVTaskClient;


public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient KTVclient;
    private final Gson gson;


    public HttpTaskManager(String url) {
        KTVclient = new KVTaskClient(url);
        gson = Managers.getGson();
    }

    @Override
    public void save() {
        String tasks = gson.toJson(getTaskList());
        KTVclient.put("tasks/task", tasks);

        String epics = gson.toJson(getEpicList());
        KTVclient.put("tasks/epic", epics);

        String subtasks = gson.toJson(getSubtackList());
        KTVclient.put("tasks/subtask", subtasks);

        String history = gson.toJson(getHistory());
        KTVclient.put("tasks/history", history);

        String prioritizedTasks = gson.toJson(getPrioritizedTasks());
        KTVclient.put("tasks", prioritizedTasks);

    }

    public static HttpTaskManager loadFromFile(String key) {
        HttpTaskManager newManager = new HttpTaskManager("http://localhost:8078/");

        newManager.load(key);
        return newManager;
    }


    private void load(@NotNull String key) {
        JsonElement jsonElement;
        JsonObject jsonObject;
        switch (key) {
            case "tasks":
                String gsonPrioritizedTasks = KTVclient.load("tasks");
                jsonElement = JsonParser.parseString(gsonPrioritizedTasks);
                for (JsonElement element : jsonElement.getAsJsonArray()) {
                    jsonObject = element.getAsJsonObject();
                    Task taskWrite;

                    if (jsonObject.has("subtasks")) {
                        taskWrite = gson.fromJson(element, Epic.class);
                    } else if (jsonObject.has("epicId")) {
                        taskWrite = gson.fromJson(element, Subtack.class);
                    } else taskWrite = gson.fromJson(element, Task.class);
                    historyManager.add(taskWrite);
                    prioritizedTasks.add(taskWrite);
                }
                break;

            case "tasks/history":
                String gsonHistory = KTVclient.load("tasks/history");
                jsonElement = JsonParser.parseString(gsonHistory);
                for (JsonElement element : jsonElement.getAsJsonArray()) {
                    jsonObject = element.getAsJsonObject();
                    Task taskWrite;

                    if (jsonObject.has("subtasks")) {
                        taskWrite = gson.fromJson(element, Epic.class);
                    } else if (jsonObject.has("epicId")) {
                        taskWrite = gson.fromJson(element, Subtack.class);
                    } else taskWrite = gson.fromJson(element, Task.class);
                    historyManager.add(taskWrite);
                }
                break;

            case "tasks/task":
                String gsonTasks = KTVclient.load("tasks/task");

                jsonElement = JsonParser.parseString(gsonTasks);
                for (JsonElement element : jsonElement.getAsJsonArray()) {
                    Task taskWrite = gson.fromJson(element, Task.class);
                    taskHashMap.put(taskWrite.getId(), taskWrite);
                }
                break;

            case "tasks/epic":
                String gsonEpics = KTVclient.load("tasks/epic");

                jsonElement = JsonParser.parseString(gsonEpics);
                for (JsonElement element : jsonElement.getAsJsonArray()) {
                    Epic epicsWrite = gson.fromJson(element, Epic.class);
                    epicHashMap.put(epicsWrite.getId(), epicsWrite);
                }
                break;

            case "tasks/subtask":
                String gsonSubtasks = KTVclient.load("tasks/subtask");

                jsonElement = JsonParser.parseString(gsonSubtasks);
                for (JsonElement element : jsonElement.getAsJsonArray()) {
                    Subtack subtaskWrite = gson.fromJson(element, Subtack.class);
                    subtackHashMap.put(subtaskWrite.getId(), subtaskWrite);
                }
                break;
            default:
                System.out.println("Такой запрос не обрабатывается");
        }
    }


}
