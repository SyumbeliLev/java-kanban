package manager.taskManagers;

import manager.historyManagers.HistoryManager;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static final String headCsv = "id,type,name,status,description,startTime,duration,endTime,epic";
    private File file;

    public FileBackedTasksManager() {
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    protected void createTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    protected void createEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }

    protected void createSub(Subtack subtack) {
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
        String[] allArguments = {String.valueOf(task.getId()), getTaskType(task).toString(), task.getTitle(), task.getStatus().toString(), task.getDescription(), String.valueOf(task.getStartTime()), String.valueOf(task.getDuration()), String.valueOf(task.getEndTime()), getEpicId(task)};

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

        int taskId = Integer.parseInt(argument[0]);
        TaskType taskType = TaskType.valueOf(argument[1]);
        String taskTitle = argument[2];
        Progress taskStatus = Progress.valueOf(argument[3]);
        String taskDescription = argument[4];

        LocalDateTime taskStartTime;
        if(!Objects.equals(argument[5], "null")){
            taskStartTime = LocalDateTime.parse(argument[5]);
        }else{taskStartTime = null;}

        int taskDuration = Integer.parseInt(argument[6]);


        switch (taskType) {
            case TASK:
                Task task = new Task(taskTitle, taskDescription, taskStatus,taskDuration,taskStartTime);
                task.setId(taskId);
                return task;
            case EPIC:
                Epic epic = new Epic(taskTitle, taskDescription);
                epic.setId(taskId);
                epic.setStatus(taskStatus);
                return epic;
            case SUBTASK:
                int epicIdToSub = Integer.parseInt(argument[8]);
                Subtack subtack = new Subtack(taskTitle,taskDescription,taskStatus,taskDuration,taskStartTime,epicIdToSub);
                subtack.setId(taskId);
                return subtack;
            default:
                throw new RuntimeException("Неверный формат");
        }
    }

    protected static String historyToString(HistoryManager manager) {
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
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
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
