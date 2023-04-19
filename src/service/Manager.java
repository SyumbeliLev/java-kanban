package service;


import java.util.ArrayList;
import java.util.HashMap;

import model.Epic;
import model.Progress;
import model.Subtack;
import model.Task;


public class Manager {

    private int nextId = 1;
    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private final HashMap<Integer, Subtack> subtackHashMap = new HashMap<>();


    /*методы Task*/
    public ArrayList<Task> getTaskList() { /* 2.1Получение списка tasks задач*/
        return new ArrayList<>(taskHashMap.values());
    }

    public void removeAllTask() { /* 2.2 Удаление всех tasks задач.*/
        taskHashMap.clear();
    }

    public Task getByIdTask(int id) {  /* 2.3 Получение по идентификатору.*/
        return taskHashMap.getOrDefault(id, null);
    }

    public void addTask(Task task) { /* 2.4 Создание tasks задачи*/
        task.setId(nextId);
        nextId++;
        taskHashMap.put(task.getId(), task);
    }

    public void updateTask(Task task) { /* 2.5 Обновление task*/
        if (taskHashMap.containsKey(task.getId())) {
            taskHashMap.put(task.getId(), task);
        }
    }

    public void removeTaskById(int id) { /* 2.6 Удаление по идентификатору.*/
        taskHashMap.remove(id);
    }

    /*методы Epic*/
    public ArrayList<Epic> getEpicList() { /* 2.1Получение списка Epic задач*/
        return new ArrayList<>(epicHashMap.values());
    }

    public void removeAllEpic() { /* 2.2 Удаление всех Epic задач.*/
        epicHashMap.clear();
        subtackHashMap.clear();
    }

    public Epic getByIdEpic(int id) {  /* 2.3 Получение по идентификатору Epic.*/
        return epicHashMap.getOrDefault(id, null);
    }

    public int addEpic(Epic epic) { /* 2.4 Создание Epic задачи*/
        epic.setId(nextId);
        nextId++;

        setStatusEpic(epic);
        epicHashMap.put(epic.getId(), epic);
        return epic.getId();
    }

    private Epic setStatusEpic(Epic epic) {
        int statusNew = 0;
        int statusDone = 0;

        for (Integer idSubToEpic : epic.getSubtasksId()) {
            Subtack subtack = subtackHashMap.get(idSubToEpic);

            if (subtack.getStatus() == Progress.NEW) {
                statusNew++;
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
        return epic;
    }

    public void updateEpic(Epic epic) { /* 2.5 Обновление Epic*/
        setStatusEpic(epic);
        epicHashMap.put(epic.getId(), epic);
    }

    public void removeEpicById(int id) { /* 2.6 Удаление по идентификатору Epic.*/
        Epic epic = epicHashMap.remove(id);
        for (Integer idSub : epic.getSubtasksId()) {
            subtackHashMap.remove(idSub);
        }
        epicHashMap.remove(id);


    }

    public ArrayList<Subtack> getSubtackByEpic(int id) { /*Получение списка всех подзадач определённого эпика.*/
        Epic epic = epicHashMap.get(id);
        ArrayList<Subtack> list = new ArrayList<>();
        for (Integer idSub : epic.getSubtasksId()) {
            list.add(subtackHashMap.get(idSub));
        }
        return list;
    }

    /*методы Subtack */
    public ArrayList<Subtack> getSubtackList() { /* 2.1Получение списка Subtack задач*/
        return new ArrayList<>(subtackHashMap.values());
    }

    public void removeAllSubtack() { /* 2.2 Удаление всех Subtack задач.*/
        for (Integer KeyEpic : epicHashMap.keySet()) {
            Epic epic = epicHashMap.get(KeyEpic);
            epic.removeListSubtasks();
            setStatusEpic(epic);
        }
        subtackHashMap.clear();
    }

    public Subtack getByIdSubtack(int id) {  /* 2.3 Получение по идентификатору Subtack.*/
        return subtackHashMap.getOrDefault(id, null);
    }

    public void addSubtack(Subtack subtack) { /* 2.4 Создание Subtack задачи*/
        subtack.setId(nextId);
        nextId++;

        Epic epic = epicHashMap.get(subtack.getEpicId());
        if (subtack.getEpicId() == epic.getId()) {
            epic.setSubtasks(subtack.getId());
        }
        subtackHashMap.put(subtack.getId(), subtack);
        setStatusEpic(epic);

    }

    public void updateSubtack(Subtack subtack) { /* 2.5 Обновление Subtack*/

        subtackHashMap.put(subtack.getId(), subtack);
        setStatusEpic(epicHashMap.get(subtack.getEpicId()));

    }

    public void removeSubtackById(int id) { /* 2.6 Удаление по идентификатору Subtack.*/

        if (subtackHashMap.containsKey(id)) {
            Subtack subtack = subtackHashMap.get(id);
            int epicId = subtack.getEpicId();

            Epic epic = epicHashMap.get(epicId);
            epic.removeSubtaskToList(id);
            setStatusEpic(epic);

            subtackHashMap.remove(id);
        }
    }
}
