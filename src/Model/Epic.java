package Model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasks = new ArrayList<>();
    public Epic(String title, String description, int id, progress status) {
        super(title, description, id, status);

    }

    public void setSubtasks(int subtasks) {
        this.subtasks.add(subtasks);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasks;
    }
}
