package manager.taskManagers;

import manager.historyManagers.HistoryManager;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private String headCsv = "id,type,name,status,description,epic";
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public void save() {
        try {
            Files.deleteIfExists(file.toPath());
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось создать файл" + e);
        }
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            fileWriter.write(headCsv + "\n");

            for (Task task : getTaskList()) {
                fileWriter.write(toString(task) + "\n");
            }

            for (Epic epic : getEpicList()) {
                fileWriter.write(toString(epic) + "\n");
            }

            for (Subtack subtack : getSubtackList()) {
                fileWriter.write(toString(subtack) + "\n");
            }

            fileWriter.write("\n");
            fileWriter.write(historyToString(getHistoryManager())); // не разобрался, как записать в одну ячейку числа через запятую...

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

    @Override
    public HistoryManager getHistoryManager() {
        return super.getHistoryManager();
    }

    private static String historyToString(HistoryManager manager) {
        List<String> idTask = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            idTask.add(String.valueOf(task.getId()));
        }
        return String.join(",", idTask);
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

    public void loadFromFile(File file) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {

            while (fileReader.ready()) {
                String line = fileReader.readLine();
                if (line.equals(headCsv)) {
                    continue;
                } else if (line.equals("")) {
                    break;
                }
                Task task = fromString(line);
                if (task instanceof Epic) {
                    addEpic((Epic) task);
                } else if (task instanceof Subtack) {
                    addSubtack((Subtack) task);
                } else {
                    addTask(task);
                }
            }

            String historyLine = fileReader.readLine();
            addListHistory(historyFromString(historyLine));

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось прочитать файл", e);
        }
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public void addListHistory(List<Integer> id) {
        super.addListHistory(id);
    }

    @Override
    public List<Task> getTaskList() {
        return super.getTaskList();
    }

    @Override
    public List<Epic> getEpicList() {
        return super.getEpicList();
    }

    @Override
    public List<Subtack> getSubtackList() {
        return super.getSubtackList();
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
        return super.addEpic(epic);
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
