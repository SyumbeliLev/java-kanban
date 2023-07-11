package manager.taskManagers;

import com.google.gson.Gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import manager.Managers;

import model.Epic;
import model.Subtack;
import model.Task;
import server.KVTaskClient;


public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient KTVclient;
    private final Gson gson;

    public HttpTaskManager(String url, boolean shouldBeLoaded) {
        KTVclient = new KVTaskClient(url);
        gson = Managers.getGson();
        if(shouldBeLoaded){
            load();
        }
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

        String lastId = gson.toJson(getNextId());
        KTVclient.put("lastId", lastId);
    }

    public static HttpTaskManager loadFromServer() {
        HttpTaskManager newManager = new HttpTaskManager("http://localhost:8078/",true);
        return newManager;
    }

    private void load() {
        JsonElement jsonElement;
        JsonObject jsonObject;

        jsonElement = JsonParser.parseString(KTVclient.load("tasks"));
        for (JsonElement element : jsonElement.getAsJsonArray()) {
            jsonObject = element.getAsJsonObject();
            Task prioritizedTasks;

            if (jsonObject.has("subtasks")) {
                prioritizedTasks = gson.fromJson(element, Epic.class);
            } else if (jsonObject.has("epicId")) {
                prioritizedTasks = gson.fromJson(element, Subtack.class);
            } else prioritizedTasks = gson.fromJson(element, Task.class);
            this.prioritizedTasks.add(prioritizedTasks);
        }

        jsonElement = JsonParser.parseString(KTVclient.load("tasks/history"));
        for (JsonElement element : jsonElement.getAsJsonArray()) {
            jsonObject = element.getAsJsonObject();
            Task taskHistory;

            if (jsonObject.has("subtasks")) {
                taskHistory = gson.fromJson(element, Epic.class);
            } else if (jsonObject.has("epicId")) {
                taskHistory = gson.fromJson(element, Subtack.class);
            } else taskHistory = gson.fromJson(element, Task.class);
            historyManager.add(taskHistory);
        }


        jsonElement = JsonParser.parseString(KTVclient.load("tasks/task"));
        for (JsonElement element : jsonElement.getAsJsonArray()) {
            Task taskWrite = gson.fromJson(element, Task.class);
            taskHashMap.put(taskWrite.getId(), taskWrite);
        }

        jsonElement = JsonParser.parseString(KTVclient.load("tasks/epic"));
        for (JsonElement element : jsonElement.getAsJsonArray()) {
            Epic epicsWrite = gson.fromJson(element, Epic.class);
            epicHashMap.put(epicsWrite.getId(), epicsWrite);
        }

        jsonElement = JsonParser.parseString(KTVclient.load("tasks/subtask"));
        for (JsonElement element : jsonElement.getAsJsonArray()) {
            Subtack subtaskWrite = gson.fromJson(element, Subtack.class);
            subtackHashMap.put(subtaskWrite.getId(), subtaskWrite);
        }


        jsonElement = JsonParser.parseString(KTVclient.load("lastId"));
        setNextId(gson.fromJson(jsonElement, Integer.class));
    }
}
