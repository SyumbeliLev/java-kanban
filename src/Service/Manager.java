package Service;

import java.util.ArrayList;
import java.util.HashMap;

import Model.Epic;
import Model.Subtack;
import Model.Task;


public class Manager {

    private int nextId = 1;
    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private final HashMap<Integer, Subtack> subtackHashMap = new HashMap<>();


    /*методы Task*/
    public HashMap<Integer, Task> getTaskHashMap() { /* 2.1Получение списка tasks задач*/
        return taskHashMap;
    }

    public void removeAllTask() { /* 2.2 Удаление всех tasks задач.*/
        taskHashMap.clear();
    }

    public Object getByIdTask(int id) {  /* 2.3 Получение по идентификатору.*/
        if (!taskHashMap.containsKey(id)) {
            return "такого id нет в базе";
        } else {
            return taskHashMap.get(id);
        }
    }

    public void addTask(Task task) { /* 2.4 Создание tasks задачи*/
        task.setId(nextId);
        nextId++;
        taskHashMap.put(task.getId(), task);
    }

    public void updateTask(Task task, int id) { /* 2.5 Обновление task*/
        if (taskHashMap.containsKey(id)) {
            task.setId(id);
            taskHashMap.put(id, task);
        }
    }

    public void removeTaskById(int id) { /* 2.6 Удаление по идентификатору.*/
        taskHashMap.remove(id);
    }

    /*методы Epic*/
    public HashMap<Integer, Epic> getEpicHashMap() { /* 2.1Получение списка Epic задач*/
        return epicHashMap;
    }

    public void removeAllEpic() { /* 2.2 Удаление всех Epic задач.*/
        epicHashMap.clear();
        removeAllSubtack();
    }

    public Object getByIdEpic(int id) {  /* 2.3 Получение по идентификатору Epic.*/
        if (!epicHashMap.containsKey(id)) {
            return "такого id нет в базе";
        } else {
            return epicHashMap.get(id);
        }
    }

    public int addEpic(Epic epic) { /* 2.4 Создание Epic задачи*/
        epic.setId(nextId);
        nextId++;

        int statusNew = 0;
        int statusDone = 0;

        for (Integer idSubstack : subtackHashMap.keySet()) {
            Subtack subtack = subtackHashMap.get(idSubstack);

            if ((subtack.getEpicId() == 0)) {
                subtack.setEpicId(epic.getId());
                epic.setSubtasks(idSubstack);

                if (subtack.getStatus() == Task.progress.NEW) {
                    statusNew++;
                } else if (subtack.getStatus() == Task.progress.DONE) {
                    statusDone++;
                }
            }
        }


        setStatusEpic(epic, statusNew, statusDone);
        return epic.getId();
    }

    private void setStatusEpic(Epic epic, int statusNew, int statusDone) {
        if (epic.getSubtasksId().size() == statusNew || epic.getSubtasksId().size() == 0) {
            epic.setStatus(Task.progress.NEW);
        } else if (epic.getSubtasksId().size() == (statusDone)) {
            epic.setStatus(Task.progress.DONE);
        } else {
            epic.setStatus(Task.progress.IN_PROGRESS);
        }

        epicHashMap.put(epic.getId(), epic);
    }

    public void updateEpic(Epic epic) { /* 2.5 Обновление Epic*/

        int statusNew = 0;
        int statusDone = 0;
        setStatusEpic(epic, statusNew, statusDone);
    }

    public void removeEpicById(int id) { /* 2.6 Удаление по идентификатору Epic.*/
        Epic epic = epicHashMap.get(id);
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
    public HashMap<Integer, Subtack> getSubtackHashMap() { /* 2.1Получение списка Subtack задач*/
        return subtackHashMap;
    }

    public void removeAllSubtack() { /* 2.2 Удаление всех Subtack задач.*/
        subtackHashMap.clear();
    }

    public Object getByIdSubtack(int id) {  /* 2.3 Получение по идентификатору Subtack.*/
        if (!subtackHashMap.containsKey(id)) {
            return "такого id нет в базе";
        } else {
            return subtackHashMap.get(id);
        }
    }

    public int addSubtack(Subtack subtack) { /* 2.4 Создание Subtack задачи*/
        subtack.setId(nextId);
        nextId++;
        subtackHashMap.put(subtack.getId(), subtack);
        return subtack.getId();
    }

    public void updateSubtack(Subtack subtack, int id) { /* 2.5 Обновление Subtack*/
        if (subtackHashMap.containsKey(id)) {
            subtack.setId(id);
            subtackHashMap.put(id, subtack);
        }
    }

    public void removeSubtackById(int id) { /* 2.6 Удаление по идентификатору Subtack.*/
        subtackHashMap.remove(id);
    }
}
