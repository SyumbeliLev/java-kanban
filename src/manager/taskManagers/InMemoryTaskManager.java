package manager.taskManagers;

import java.time.LocalDateTime;
import java.util.*;

import manager.Managers;
import manager.historyManagers.HistoryManager;

import model.Epic;
import model.Progress;
import model.Subtack;
import model.Task;

public class InMemoryTaskManager implements TaskManager {

    private int nextId = 1;

    protected final Map<Integer, Task> taskHashMap = new HashMap<>();
    protected final Map<Integer, Epic> epicHashMap = new HashMap<>();
    protected final Map<Integer, Subtack> subtackHashMap = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final Set<Task> prioritizedTasks = new TreeSet<>(this::compareTo);


    protected void setNextId(int nextId) {
        this.nextId = nextId;
    }

    protected int getNextId() {
        return nextId;
    }

    @Override
    public int compareTo(Task task1, Task task2) {
        if (task1.getStartTime() == null) {
            return 2;
        }
        if (task2.getStartTime() == null) {
            return 1;
        }
        if (task1.getStartTime().isEqual(task2.getStartTime())) {
            return 2;
        }
        return task1.getStartTime().compareTo(task2.getStartTime());
    }

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
        for (Integer taskId : taskHashMap.keySet()) {
            historyManager.remove(taskId);
        }
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
        addTaskToPrioritizedTasks(task);
    }

    @Override
    public void updateTask(Task task) { /* 2.5 Обновление task*/

        Integer id = task.getId();
        Task savedTask = taskHashMap.get(id);
        if (savedTask == null) {
            return;
        }
        taskHashMap.put(id, task);
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
        for (Map.Entry<Integer, Epic> epicEntry : epicHashMap.entrySet()) {
            historyManager.remove(epicEntry.getKey());
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
    public void addEpic(Epic epic) { /* 2.4 Создание Epic задачи*/
        epic.setId(nextId);
        nextId++;

        setEpicStartAndEndTime(epic);
        setStatusEpic(epic);
        epicHashMap.put(epic.getId(), epic);
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
        return new ArrayList<>(subtackHashMap.values());

    }

    @Override
    public void removeAllSubtack() { /* 2.2 Удаление всех Subtack задач.*/
        for (Integer KeyEpic : epicHashMap.keySet()) {
            Epic epic = epicHashMap.get(KeyEpic);
            epic.removeListSubtasks();
            setStatusEpic(epic);
            setEpicStartAndEndTime(epic);
        }

        for (Map.Entry<Integer, Subtack> subtackEntry : subtackHashMap.entrySet()) {
            historyManager.remove(subtackEntry.getKey());
            prioritizedTasks.remove(subtackEntry.getValue());
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
        setEpicStartAndEndTime(epic);
        addTaskToPrioritizedTasks(subtack);

    }

    @Override
    public void updateSubtack(Subtack subtack) { /* 2.5 Обновление Subtack*/
        subtackHashMap.put(subtack.getId(), subtack);
        Epic epic = epicHashMap.get(subtack.getEpicId());
        setStatusEpic(epic);
        setEpicStartAndEndTime(epic);

    }

    @Override
    public void removeSubtackById(int id) { /* 2.6 Удаление по идентификатору Subtack.*/
        historyManager.remove(id);
        if (subtackHashMap.containsKey(id)) {
            Subtack subtack = subtackHashMap.remove(id);
            prioritizedTasks.remove(subtack);
            int epicId = subtack.getEpicId();
            Epic epic = epicHashMap.get(epicId);
            epic.removeSubtask(id);
            setStatusEpic(epic);
            setEpicStartAndEndTime(epic);
        }
    }

    public void addListHistory(List<Integer> id) {
        for (Integer idTask : id) {
            if (epicHashMap.containsKey(idTask)) {
                historyManager.add(epicHashMap.get(idTask));
            } else if (subtackHashMap.containsKey(idTask)) {
                historyManager.add(subtackHashMap.get(idTask));
            } else historyManager.add(taskHashMap.get(idTask));
        }
    }

    public void setEpicStartAndEndTime(Epic epic) {
        if (epic.getSubtasksId().isEmpty()) {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(0);
        } else {
            int duration = 0;
            LocalDateTime startTime = LocalDateTime.MAX;
            LocalDateTime endTime = LocalDateTime.MIN;

            for (Integer idSub : epic.getSubtasksId()) {
                Subtack subtack = subtackHashMap.get(idSub);
                if (subtack.getStartTime().isBefore(startTime)) {
                    startTime = subtack.getStartTime();
                }
                if (subtack.getEndTime().isAfter(endTime)) {
                    endTime = subtack.getEndTime();
                }
                duration += subtack.getDuration();
            }
            epic.setEndTime(endTime);
            epic.setStartTime(startTime);
            epic.setDuration(duration);
        }
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);

    }

    private void addTaskToPrioritizedTasks(Task task) {
        prioritizedTasks.add(task);
        checkIntersections();
    }

    private void checkIntersections() {
        List<Task> prioritizedTasks = getPrioritizedTasks();

        for (int i = 1; i < prioritizedTasks.size(); i++) {
            Task prioritizedTask = prioritizedTasks.get(i);

            if (prioritizedTask.getStartTime().isBefore(prioritizedTasks.get(i - 1).getEndTime()))
                throw new RuntimeException("Найдено пересечение " + prioritizedTasks.get(i) + " и " + prioritizedTasks.get(i - 1));
        }
    }
}
