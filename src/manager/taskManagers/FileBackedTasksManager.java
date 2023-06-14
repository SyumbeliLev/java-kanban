package manager.taskManagers;

import manager.historyManagers.HistoryManager;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static final String headCsv = "id,type,name,status,description,epic";
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private void createTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    private void createEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }

    private void createSub(Subtack subtack) {
        int idSub = subtack.getId();
        epicHashMap.get(subtack.getEpicId()).addSubtask(idSub);
        subtackHashMap.put(idSub, subtack);
    }

    public void save() {
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
            fileWriter.write(historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось записать в файл ", e);
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

    private static Task fromString(String value) {
        String[] argument = value.split(",");

        String taskId = argument[0];
        String taskType = argument[1];
        String taskTitle = argument[2];
        String taskStatus = argument[3];
        String taskDescription = argument[4];


        switch (taskType.toLowerCase()) {
            case "task":
                Task task = new Task(taskTitle, taskDescription, Progress.valueOf(taskStatus.toUpperCase()));
                task.setId(Integer.parseInt(taskId));
                return task;
            case "epic":
                Epic epic = new Epic(taskTitle, taskDescription);
                epic.setId(Integer.parseInt(taskId));
                epic.setStatus(Progress.valueOf(taskStatus.toUpperCase()));
                return epic;
            case "subtask":
                String epicIdToSub = argument[5];

                Subtack subtack = new Subtack(taskTitle, taskDescription, Progress.valueOf(taskStatus.toUpperCase()), Integer.parseInt(epicIdToSub));
                subtack.setId(Integer.parseInt(taskId));
                return subtack;
            default:
                return new Task("null", "null", Progress.NEW);
        }
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

    protected void setNextId(int id) {
        if (id > super.getNextId()) {
            super.setNextId(++id);
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);

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
                    manager.setNextId(task.getId());
                    manager.createEpic((Epic) task);

                } else if (task instanceof Subtack) {
                    manager.setNextId(task.getId());
                    manager.createSub((Subtack) task);
                } else {
                    manager.setNextId(task.getId());
                    manager.createTask(task);
                }
            }


            String historyLine = fileReader.readLine();

            if (historyLine != null) manager.addListHistory(historyFromString(historyLine));

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось прочитать файл", e);
        }
        return manager;
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
