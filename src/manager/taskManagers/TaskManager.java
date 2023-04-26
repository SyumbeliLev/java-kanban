package manager.taskManagers;

import model.Epic;
import model.Subtack;
import model.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getHistory(); /*Получение истории просомтров*/

    /*методы Task*/
    List<Task> getTaskList(); /* 2.1Получение списка tasks задач*/

    void removeAllTask(); /* 2.2 Удаление всех tasks задач.*/

    Task getTaskById(int id);  /* 2.3 Получение по идентификатору.*/

    void addTask(Task task); /* 2.4 Создание tasks задачи*/

    void updateTask(Task task); /* 2.5 Обновление task*/

    void removeTaskById(int id);  /* 2.6 Удаление по идентификатору.*/

    /*методы Epic*/
    List<Epic> getEpicList(); /* 2.1Получение списка Epic задач*/

    void removeAllEpic(); /* 2.2 Удаление всех Epic задач.*/

    Epic getEpicById(int id);  /* 2.3 Получение по идентификатору Epic.*/

    int addEpic(Epic epic); /* 2.4 Создание Epic задачи*/

    void updateEpic(Epic epic); /* 2.5 Обновление Epic*/

    void removeEpicById(int id); /* 2.6 Удаление по идентификатору Epic.*/

    List<Subtack> getSubtackEpic(int id); /* 3.1 Получение списка всех подзадач определённого эпика.*/
    /*методы Subtack */

    List<Subtack> getSubtackList(); /* 2.1Получение списка Subtack задач*/

    void removeAllSubtack(); /* 2.2 Удаление всех Subtack задач.*/

    Subtack getSubtackById(int id); /* 2.3 Получение по идентификатору Subtack.*/

    void addSubtack(Subtack subtack); /* 2.4 Создание Subtack задачи*/

    void updateSubtack(Subtack subtack);/* 2.5 Обновление Subtack*/

    void removeSubtackById(int id); /* 2.6 Удаление по идентификатору Subtack.*/
}
