package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasks = new ArrayList<>();
    public Epic(String title, String description, int id) {
        super(title, description,Progress.NEW, id);
    }

    public void addSubtask(int subtasks) {
        this.subtasks.add(subtasks);
    }
    public void removeListSubtasks(){
        subtasks.clear();
    }
    public void removeSubtask(Integer idSub){
        this.subtasks.remove(idSub);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasks;
    }
}
