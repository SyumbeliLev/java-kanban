package manager.taskManagers;

import model.Epic;
import model.Subtack;
import model.Task;

import java.util.ArrayList;


public interface TaskManager {

    /*методы Task*/
    public ArrayList<Task> getTaskList(); /* 2.1Получение списка tasks задач*/
    public void removeAllTask(); /* 2.2 Удаление всех tasks задач.*/
    public Task getTaskById(int id);  /* 2.3 Получение по идентификатору.*/
    public void addTask(Task task); /* 2.4 Создание tasks задачи*/
    public void updateTask(Task task); /* 2.5 Обновление task*/
    public void removeTaskById(int id);  /* 2.6 Удаление по идентификатору.*/

    /*методы Epic*/
    public ArrayList<Epic> getEpicList(); /* 2.1Получение списка Epic задач*/
    public void removeAllEpic(); /* 2.2 Удаление всех Epic задач.*/
    public Epic getEpicById(int id);  /* 2.3 Получение по идентификатору Epic.*/
    public int addEpic(Epic epic); /* 2.4 Создание Epic задачи*/
    public void updateEpic(Epic epic); /* 2.5 Обновление Epic*/
    public void removeEpicById(int id); /* 2.6 Удаление по идентификатору Epic.*/
    public ArrayList<Subtack> getSubtackEpic(int id); /* 3.1 Получение списка всех подзадач определённого эпика.*/
    /*методы Subtack */

    public ArrayList<Subtack> getSubtackList(); /* 2.1Получение списка Subtack задач*/
    public void removeAllSubtack(); /* 2.2 Удаление всех Subtack задач.*/
    public Subtack getSubtackById(int id); /* 2.3 Получение по идентификатору Subtack.*/
    public void addSubtack(Subtack subtack); /* 2.4 Создание Subtack задачи*/
    public void updateSubtack(Subtack subtack);/* 2.5 Обновление Subtack*/
    public void removeSubtackById(int id); /* 2.6 Удаление по идентификатору Subtack.*/


}
