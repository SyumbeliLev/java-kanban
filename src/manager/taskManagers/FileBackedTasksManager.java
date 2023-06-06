package manager.taskManagers;

import manager.historyManagers.HistoryManager;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final static String FIELDS_HEADINGS_CSV = "id,type,name,status,description,epic\n";
    private File file;
    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public void save() {
        try {
            Files.deleteIfExists(file.toPath());
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось создать файл");
        }
        try (Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write(FIELDS_HEADINGS_CSV);

            for (Task task : getTaskList()) {
                fileWriter.write(toString(task));
            }

            for (Epic epic : getEpicList()) {
                fileWriter.write(toString(epic));
            }

            for (Subtack subtack : getSubtackList()) {
                fileWriter.write(toString(subtack));
            }

            fileWriter.write("\n");
            fileWriter.write(historyToString((HistoryManager) getHistory()));

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл ", e);
        }
    }


    private String toString(Task task) {
        String[] allArguments = {String.valueOf(task.getId()), getTaskType(task).toString(), task.getTitle(), task.getStatus().toString(), task.getDescription(), getEpicId(task)};
        return String.join(",", allArguments);
    }

    private TaskType getTaskType(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof Subtack) {
            return TaskType.SUBTASK;
        } else return TaskType.TASK;
    }

    private String getEpicId(Task task) {
        if (task instanceof Subtack) {
            Subtack subtack = (Subtack) task;
            return String.valueOf(subtack.getEpicId());
        } else return "";
    }

    private Task fromString(String value) {
        String[] argument = value.split(",");
        switch (argument[1].toLowerCase()) {
            case "task":
                Task task = new Task(argument[2], argument[4], Progress.valueOf(argument[3].toUpperCase()));
                task.setId(Integer.parseInt(argument[0]));
                return task;
            case "epic":
                Epic epic = new Epic(argument[2], argument[4]);
                epic.setId(Integer.parseInt(argument[0]));
                epic.setStatus(Progress.valueOf(argument[3].toUpperCase()));
                return epic;
            case "subtask":
                Subtack subtack = new Subtack(argument[2], argument[4], Progress.valueOf(argument[3].toUpperCase()), Integer.parseInt(argument[5]));
                subtack.setId(Integer.parseInt(argument[0]));
                return subtack;
            default:
                return new Task("null", "null", Progress.NEW);
        }
    }

    private static String historyToString(HistoryManager manager) {
        List<String> idTask = null;
        for (Task task : manager.getHistory()) {
            idTask.add(String.valueOf(task.getId()));
        }
        String idToString = String.join(",", idTask);
        return idToString;
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> historyRestoration = new ArrayList<>();
        if (!value.isEmpty()) {
            for (String id : value.split(",")) {
                historyRestoration.add(Integer.parseInt(id));
            }
        }
        return historyRestoration;
    }

    private void loadFromFile(File file) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String line = fileReader.readLine();
            while (fileReader.ready()) {
                if (line.isEmpty()) {
                    break;
                }

                Task task = fromString(line);
                switch (getTaskType(task)) {
                    case TASK:
                        addTask(task);
                    case EPIC:
                        addEpic((Epic) task);
                    case SUBTASK:
                        addSubtack((Subtack) task);
                }

            }
            String historyLine = fileReader.readLine();
            addListHistory(historyFromString(historyLine));

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось прочитать файл", e);
        }
    }

    @Override
    public void addListHistory(List<Integer> id) {
        super.addListHistory(id);
        save();
    }

    @Override
    public List<Task> getTaskList() {
        List<Task> taskList = super.getTaskList();
        save();
        return taskList;
    }

    @Override
    public List<Epic> getEpicList() {
        List<Epic> epicList = super.getEpicList();
        save();
        return epicList;
    }

    @Override
    public List<Subtack> getSubtackList() {
        List<Subtack> subtackList = super.getSubtackList();
        save();
        return subtackList;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasksView = super.getHistory();
        save();
        return tasksView;
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public int addEpic(Epic epic) {
        int idEpic = super.addEpic(epic);
        save();
        return idEpic;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public List<Subtack> getSubtackEpic(int id) {
        List<Subtack> sub = super.getSubtackEpic(id);
        save();
        return sub;
    }

    @Override
    public void removeAllSubtack() {
        super.removeAllSubtack();
        save();
    }

    @Override
    public Subtack getSubtackById(int id) {
        Subtack sub = super.getSubtackById(id);
        save();
        return sub;

    }

    @Override
    public void addSubtack(Subtack subtack) {
        super.addSubtack(subtack);
        save();
    }

    @Override
    public void updateSubtack(Subtack subtack) {
        super.updateSubtack(subtack);
        save();
    }

    @Override
    public void removeSubtackById(int id) {
        super.removeSubtackById(id);
        save();
    }
}
