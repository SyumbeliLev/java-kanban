package TasksManagers;

import manager.taskManagers.TaskManager;


import model.Epic;
import model.Progress;
import model.Subtack;
import model.Task;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import java.util.ArrayList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;

    final int NON_EXISTENT_ID = 99;

    @BeforeEach
    public void createTaskAndEpic() {
        task1 = new Task("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 3, 1, 1, 1));
        task2 = new Task("title", "description", Progress.DONE, 33, LocalDateTime.of(2022, 5, 1, 1, 1));
        epic1 = new Epic("title", "description");
        epic2 = new Epic("title", "description");
    }

    @Test
    public void getHistoryTest() {
        taskManager.addTask(task1);
        taskManager.addEpic(epic1);
        Subtack subtack = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 2, 1, 1, 1), epic1.getId());
        taskManager.addSubtack(subtack);

        taskManager.getSubtackById(3);
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);

        List<Task> trueHistory = new ArrayList<>(List.of(subtack, task1, epic1));

        assertEquals(trueHistory, taskManager.getHistory(), "Не верный порядок записи");

        taskManager.removeEpicById(2);
        trueHistory.remove(subtack);
        trueHistory.remove(epic1);
        assertEquals(trueHistory, taskManager.getHistory(), "Не верное запись после удаления");

        taskManager.removeAllTask();
        taskManager.getEpicById(NON_EXISTENT_ID);
        assertIterableEquals(List.of(), taskManager.getHistory(), "История после очистки не пуста");
    }

    @Test
    public void getTaskListTest() {
        assertIterableEquals(List.of(), taskManager.getTaskList(), "taskList не пустой");

        taskManager.addTask(task1);
        assertIterableEquals(List.of(task1), taskManager.getTaskList(), "В taskList не добавляются таски");
    }

    @Test
    public void removeAllTaskTest() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        final List<Task> tasks = taskManager.getTaskList();
        assertNotNull(tasks, "Задачи не удаляются.");
    }

    @Test
    public void getTaskById() {
        assertNull(taskManager.getTaskById(NON_EXISTENT_ID), "Возвращает не существующий task");
        taskManager.addTask(task1);
        assertEquals(task1, taskManager.getTaskById(1), "Возвращает не правильный task по id");
    }

    @Test
    public void addTaskTest() {
        taskManager.addTask(task1);
        final int taskId = task1.getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void updateTaskTest() {
        taskManager.addTask(task1);
        task2.setId(task1.getId());
        taskManager.updateTask(task2);
        assertNotEquals(task1, taskManager.getTaskById(1), "обновление не работает");
    }

    @Test
    public void removeTaskByIdAndAllTest() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.removeTaskById(1);
        final List<Task> tasks = taskManager.getTaskList();
        assertIterableEquals(List.of(task2), tasks, "не удаляет задачу по id");

        taskManager.removeAllTask();
        assertNotNull(tasks, "не удаляет все задачи");
    }


    @Test
    public void getEpicListTest() {
        assertIterableEquals(List.of(), taskManager.getEpicList(), "epicList не пустой");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        assertIterableEquals(List.of(epic1, epic2), taskManager.getEpicList(), "В epicList не добавляются эпики");
    }

    @Test
    public void removeEpicByIdAndAllTest() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        Subtack subtack1 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 1, 1, 1, 1), epic1.getId());
        Subtack subtack2 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 2, 1, 1, 1), epic2.getId());
        Subtack subtack3 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 3, 1, 1, 1), epic2.getId());
        taskManager.addSubtack(subtack1);
        taskManager.addSubtack(subtack2);
        taskManager.addSubtack(subtack3);

        taskManager.removeEpicById(1);
        final List<Epic> epics = taskManager.getEpicList();
        assertIterableEquals(List.of(epic2), epics, "не удаляет epic по id");
        assertIterableEquals(List.of(subtack2, subtack3), taskManager.getSubtackList(), "Не удаляются подзадачи эпика при его удаление");

        taskManager.removeAllEpic();
        assertIterableEquals(List.of(), taskManager.getEpicList(), "не удаляет все epic");
        assertIterableEquals(List.of(), taskManager.getSubtackList(), "Не удаляются подзадачи при удаление всех эпиков");
    }

    @Test
    public void getEpicByIdTest() {
        assertNull(taskManager.getEpicById(NON_EXISTENT_ID), "Возвращает не существующий epic");
        taskManager.addEpic(epic1);
        assertEquals(epic1, taskManager.getEpicById(1), "Возвращает не правильный epic по id");
    }

    @Test
    public void addEpicTest() {
        taskManager.addEpic(epic1);
        final int epicId = epic1.getId();
        final Task savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Epic не найден.");
        assertEquals(epic1, savedEpic, "epic не совпадаю.");

        final List<Epic> epics = taskManager.getEpicList();

        assertIterableEquals(List.of(epic1), epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic1, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void updateEpicTest() {
        taskManager.addEpic(epic1);
        Epic updateEpic = new Epic("updateTitle", "updateDescription");
        updateEpic.setId(epic1.getId());
        taskManager.updateEpic(updateEpic);
        assertNotEquals(epic1.getTitle(), taskManager.getEpicById(1).getTitle(), "Обновление не работает");
    }

    @Test
    public void getSubtaskEpicTest() {
        assertEquals(List.of(),taskManager.getSubtackEpic(NON_EXISTENT_ID));

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        Subtack subtack1 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 1, 1, 1, 1), epic1.getId());
        Subtack subtack2 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 2, 1, 1, 1), epic1.getId());
        taskManager.addSubtack(subtack1);
        taskManager.addSubtack(subtack2);

        assertIterableEquals(List.of(), taskManager.getSubtackEpic(2), "Возвращает не существующие subtask");
        assertIterableEquals(List.of(subtack1, subtack2), taskManager.getSubtackEpic(1), "Не правильно возвращает id subtask");
    }

    @Test
    public void getSubtaskListTest() {
        assertEquals(new ArrayList<>(), taskManager.getSubtackList(), "SubtaskList не пустой");
        taskManager.addEpic(epic1);
        Subtack subtack1 = new Subtack("title", "description", Progress.NEW, 10, LocalDateTime.of(2022, 1, 1, 1, 1), epic1.getId());
        Subtack subtack2 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 2, 1, 1, 1), epic1.getId());
        taskManager.addSubtack(subtack1);
        taskManager.addSubtack(subtack2);
        assertIterableEquals(List.of(subtack1, subtack2), taskManager.getSubtackList(), "В epicList не добавляются эпики");
    }

    @Test
    public void getSubtaskById() {
        assertNull(taskManager.getSubtackById(NON_EXISTENT_ID), "Возвращает subtask c несуществующим Id");

        taskManager.addEpic(epic1);
        Subtack subtack1 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 2, 1, 1, 1), epic1.getId());
        taskManager.addSubtack(subtack1);
        assertEquals(subtack1, taskManager.getSubtackById(2), "Возвращает не правильный subtask по id");
    }

    @Test
    public void removeSubtaskAllAndByIdTest() {
        assertDoesNotThrow(() -> taskManager.removeSubtackById(NON_EXISTENT_ID), "Ошибки на удаление subtask с несуществующим id ");

        taskManager.addEpic(epic2);
        Subtack subtack2 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 1, 2, 2, 2), epic2.getId());
        Subtack subtack3 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 2, 2, 2, 2), epic2.getId());
        Subtack subtack4 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 3, 2, 2, 2), epic2.getId());
        taskManager.addSubtack(subtack2);
        taskManager.addSubtack(subtack3);
        taskManager.addSubtack(subtack4);

        taskManager.removeSubtackById(4);
        assertIterableEquals(List.of(subtack2, subtack3), taskManager.getSubtackList(), "Не верно удаляет subtask по id");

        taskManager.removeAllSubtack();
        assertIterableEquals(List.of(), taskManager.getSubtackList(), "Не удаляются все subtask");
    }


    @Test
    public void addSubtaskTest() {
        taskManager.addEpic(epic1);
        Subtack subtack1 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 1, 2, 2, 2), NON_EXISTENT_ID);
        Subtack subtack2 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 2, 2, 2, 2), epic1.getId());
        NullPointerException exp = assertThrows(NullPointerException.class, () -> taskManager.addSubtack(subtack1));
        assertNull(exp.getMessage(), "Создает subtask c несуществующим epicId");

        taskManager.addSubtack(subtack2);
        assertEquals(epic1.getId(), subtack2.getEpicId(), "subtask не присваивается epic");

        Subtack subtack3 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 3, 2, 2, 2), epic1.getId());
        taskManager.addSubtack(subtack3);
        final int subtaskId = subtack3.getId();
        final Task savedSubtask = taskManager.getSubtackById(subtaskId);

        assertNotNull(savedSubtask, "subtask не найден.");
        assertEquals(subtack3, savedSubtask, "subtask не совпадаю.");

        final List<Subtack> subtasks = taskManager.getSubtackList();

        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtack3, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void updateSubtaskTest() {
        taskManager.addEpic(epic1);

        Subtack subtack1 = new Subtack("Subtask1", "Description", Progress.NEW, 20, LocalDateTime.of(2022, 1, 2, 2, 2), epic1.getId());
        Subtack subtask2 = new Subtack("Subtask2", "Description", Progress.NEW, 20, LocalDateTime.of(2022, 2, 2, 2, 2), epic1.getId());
        taskManager.addSubtack(subtack1);
        taskManager.addSubtack(subtask2);

        Epic epicStatusNewSaved = taskManager.getEpicById(1);

        assertEquals(Progress.NEW, epicStatusNewSaved.getStatus(), "Статус не NEW");

        Subtack subtaskStatusInProgress = new Subtack("Subtask1", "Description", Progress.IN_PROGRESS, 20, LocalDateTime.of(2022, 3, 1, 1, 1), epic1.getId());
        subtaskStatusInProgress.setId(subtack1.getId());
        taskManager.updateSubtack(subtaskStatusInProgress);
        Subtack subtaskStatusInProgressSaved = taskManager.getSubtackById(2);
        Epic epicStatusInProgressSaved = taskManager.getEpicById(1);

        assertEquals(subtaskStatusInProgressSaved, subtaskStatusInProgress, "подзадачи отличаются");
        assertEquals(Progress.IN_PROGRESS, epicStatusInProgressSaved.getStatus(), "Статус у Эпика не IN_PROGRESS");

        Subtack subtaskStatusDone1 = new Subtack("Subtask1", "Description", Progress.DONE, 20, LocalDateTime.of(2022, 5, 1, 1, 1), epic1.getId());
        Subtack subtaskStatusDone2 = new Subtack("Subtask2", "Description", Progress.DONE, 20, LocalDateTime.of(2022, 7, 1, 1, 1), epic1.getId());

        subtaskStatusDone1.setId(subtack1.getId());
        subtaskStatusDone2.setId(subtask2.getId());
        taskManager.updateSubtack(subtaskStatusDone1);
        taskManager.updateSubtack(subtaskStatusDone2);

        Epic epicStatusDoneSaved = taskManager.getEpicById(1);
        assertEquals(Progress.DONE, epicStatusDoneSaved.getStatus(), "Статус эпика не DONE");
    }

    @Test
    public void setEpicStartAndEndTimeTest() {
        taskManager.addEpic(epic1);
        assertNull(epic1.getStartTime(), "Неправильно определяет стартовое время эпика без подзадачи");
        assertNull(epic1.getEndTime(), "Неправильно определяет конец выполнения эпика без подзадачи");

        Subtack subtack1 = new Subtack("Subtask1", "Description", Progress.DONE, 50, LocalDateTime.of(2000, 1, 1, 1, 1), epic1.getId());
        Subtack subtack2 = new Subtack("Subtask2", "Description", Progress.DONE, 60, LocalDateTime.of(2000, 3, 1, 1, 1), epic1.getId());
        taskManager.addSubtack(subtack1);
        taskManager.addSubtack(subtack2);
        assertEquals(subtack1.getStartTime(), epic1.getStartTime(), " Время начала не равно — дате старта самой ранней подзадачи");
        assertEquals(subtack2.getEndTime(), epic1.getEndTime(), " Время конца не равно — дате конца самой поздней подзадачи");
    }

    @Test
    public void getPrioritizedTasksTest() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        List<Task> prioritizedTasks = new ArrayList<>();
        prioritizedTasks.add(task1);
        prioritizedTasks.add(task2);
        List<Task> prioritizedTasksCreated = taskManager.getPrioritizedTasks();
        assertEquals(prioritizedTasks, prioritizedTasksCreated);
    }

    @Test
    public void getDurationTest() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(20, taskManager.getTaskById(1).getDuration());
        assertEquals(33, taskManager.getTaskById(2).getDuration());
    }

    @Test
    public void setDurationTest() {
        taskManager.addTask(task1);
        taskManager.getTaskById(1).setDuration(42);
        assertEquals(42, taskManager.getTaskById(1).getDuration());
    }

    @Test
    public void checkEpicStatusWhenModifyingSubtasks(){
        taskManager.addEpic(epic1);
        Subtack subtack1 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 1, 1, 1, 1), epic1.getId());
        Subtack subtack2 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 2, 1, 1, 1), epic1.getId());
        Subtack subtack3 = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 3, 1, 1, 1), epic1.getId());
        taskManager.addSubtack(subtack1);
        taskManager.addSubtack(subtack2);
        taskManager.addSubtack(subtack3);

        assertEquals(Progress.NEW, epic1.getStatus());

        subtack2.setStatus(Progress.DONE);
        taskManager.updateSubtack(subtack2);
        assertEquals(Progress.IN_PROGRESS, epic1.getStatus());

        subtack1.setStatus(Progress.DONE);
        subtack3.setStatus(Progress.DONE);
        taskManager.updateSubtack(subtack1);
        taskManager.updateSubtack(subtack3);
        assertEquals(Progress.DONE, epic1.getStatus());
    }

}
