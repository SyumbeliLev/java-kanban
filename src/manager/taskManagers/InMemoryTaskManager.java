package manager.taskManagers;

import java.util.*;

import manager.Managers;
import manager.historyManagers.HistoryManager;

import model.Epic;
import model.Progress;
import model.Subtack;
import model.Task;

public class InMemoryTaskManager implements TaskManager {


    private int nextId = 1;

    private final Map<Integer, Task> taskHashMap = new HashMap<>();
    private final Map<Integer, Epic> epicHashMap = new HashMap<>();
    private final Map<Integer, Subtack> subtackHashMap = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    /*методы Task*/
    @Override
    public List<Task> getTaskList() { /* 2.1Получение списка tasks задач*/
        return new ArrayList<>(taskHashMap.values());
    }

    @Override
    public void removeAllTask() { /* 2.2 Удаление всех tasks задач.*/
        taskHashMap.clear();
    }

    @Override
    public Task getTaskById(int id) {  /* 2.3 Получение по идентификатору.*/
        Task task = taskHashMap.get(id);

        historyManager.add(task);
        return task;
    }

    @Override
    public void addTask(Task task) { /* 2.4 Создание tasks задачи*/
        task.setId(nextId);
        nextId++;
        taskHashMap.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task) { /* 2.5 Обновление task*/
        if (taskHashMap.containsKey(task.getId())) {
            taskHashMap.put(task.getId(), task);
        }
    }

    @Override
    public void removeTaskById(int id) { /* 2.6 Удаление по идентификатору.*/
        taskHashMap.remove(id);
        historyManager.remove(id);
    }

    /*методы Epic*/
    @Override
    public List<Epic> getEpicList() { /* 2.1Получение списка Epic задач*/
        return new ArrayList<>(epicHashMap.values());
    }

    @Override
    public void removeAllEpic() { /* 2.2 Удаление всех Epic задач.*/
        removeAllSubtack();
        for (Integer epicId : epicHashMap.keySet()) {
            historyManager.remove(epicId);
        }
        epicHashMap.clear();

    }

    @Override
    public Epic getEpicById(int id) {  /* 2.3 Получение по идентификатору Epic.*/
        Epic epic = epicHashMap.get(id);

        historyManager.add(epic);
        return epic;
    }

    @Override
    public int addEpic(Epic epic) { /* 2.4 Создание Epic задачи*/
        epic.setId(nextId);
        nextId++;

        setStatusEpic(epic);
        epicHashMap.put(epic.getId(), epic);
        return epic.getId();
    }

    private void setStatusEpic(Epic epic) {
        int statusNew = 0;
        int statusDone = 0;

        for (Integer idSubToEpic : epic.getSubtasksId()) {
            Subtack subtack = subtackHashMap.get(idSubToEpic);

            if (subtack.getStatus() == Progress.NEW) {
                statusNew++;
            } else if (subtack.getStatus() == Progress.IN_PROGRESS) {
                epic.setStatus(Progress.IN_PROGRESS);
                return;
            } else if (subtack.getStatus() == Progress.DONE) {
                statusDone++;
            }
        }
        if (epic.getSubtasksId().size() == 0 || statusNew == epic.getSubtasksId().size()) {
            epic.setStatus(Progress.NEW);
        } else if (statusDone == epic.getSubtasksId().size()) {
            epic.setStatus(Progress.DONE);
        } else {
            epic.setStatus(Progress.IN_PROGRESS);
        }

    }

    @Override
    public void updateEpic(Epic epic) { /* 2.5 Обновление Epic*/
        epicHashMap.put(epic.getId(), epic);
    }

    @Override
    public void removeEpicById(int id) { /* 2.6 Удаление по идентификатору Epic.*/
        if (!epicHashMap.isEmpty()) {
            Epic epic = epicHashMap.remove(id);
            historyManager.remove(id);

            for (Integer idSub : epic.getSubtasksId()) {
                subtackHashMap.remove(idSub);
                historyManager.remove(idSub);
            }
        }
    }

    @Override
    public List<Subtack> getSubtackEpic(int id) { /*Получение списка всех подзадач определённого эпика.*/
        Epic epic = epicHashMap.get(id);
        ArrayList<Subtack> list = new ArrayList<>();
        for (Integer idSub : epic.getSubtasksId()) {
            list.add(subtackHashMap.get(idSub));
            historyManager.add(subtackHashMap.get(idSub));
        }
        return list;
    }

    /*методы Subtack */
    @Override
    public List<Subtack> getSubtackList() { /* 2.1Получение списка Subtack задач*/
        for (Map.Entry<Integer, Subtack> entry : subtackHashMap.entrySet()) {
            historyManager.add(entry.getValue());
        }
        return new ArrayList<>(subtackHashMap.values());

    }

    @Override
    public void removeAllSubtack() { /* 2.2 Удаление всех Subtack задач.*/
        for (Integer KeyEpic : epicHashMap.keySet()) {
            Epic epic = epicHashMap.get(KeyEpic);
            epic.removeListSubtasks();
            setStatusEpic(epic);
        }

        for (Integer integer : subtackHashMap.keySet()) {
            historyManager.remove(integer);
        }
        subtackHashMap.clear();
    }

    @Override
    public Subtack getSubtackById(int id) {  /* 2.3 Получение по идентификатору Subtack.*/
        Subtack subtack = subtackHashMap.get(id);

        historyManager.add(subtack);
        return subtack;
    }

    @Override
    public void addSubtack(Subtack subtack) { /* 2.4 Создание Subtack задачи*/
        subtack.setId(nextId);
        nextId++;

        Epic epic = epicHashMap.get(subtack.getEpicId());
        if (subtack.getEpicId() == epic.getId()) {
            epic.addSubtask(subtack.getId());
        }
        subtackHashMap.put(subtack.getId(), subtack);
        setStatusEpic(epic);

    }

    @Override
    public void updateSubtack(Subtack subtack) { /* 2.5 Обновление Subtack*/
        subtackHashMap.put(subtack.getId(), subtack);
        setStatusEpic(epicHashMap.get(subtack.getEpicId()));

    }

    @Override
    public void removeSubtackById(int id) { /* 2.6 Удаление по идентификатору Subtack.*/
        historyManager.remove(id);
        if (subtackHashMap.containsKey(id)) {
            Subtack subtack = subtackHashMap.remove(id);
            int epicId = subtack.getEpicId();
            Epic epic = epicHashMap.get(epicId);
            epic.removeSubtask(id);
            setStatusEpic(epic);
        }
    }

    public void addListHistory(List<Integer> id) {
        for (Integer idTask : id) {
            if (epicHashMap.containsKey(idTask)) {
                historyManager.add(epicHashMap.get(idTask));
            } else if (subtackHashMap.containsKey(idTask)) {
                historyManager.add(subtackHashMap.get(idTask));
            } else
                historyManager.add(taskHashMap.get(idTask));
        }
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
