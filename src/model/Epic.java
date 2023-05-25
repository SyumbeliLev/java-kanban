package model;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> subtasks = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description, Progress.NEW);
    }

    public void addSubtask(int subtasks) {
        this.subtasks.add(subtasks);
    }

    public void removeListSubtasks() {
        subtasks.clear();
    }

    public void removeSubtask(Integer idSub) {
        this.subtasks.remove(idSub);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subtasks);
    }
}
