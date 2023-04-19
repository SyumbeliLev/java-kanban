package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasks = new ArrayList<>();
    public Epic(String title, String description, Progress status, int id) {
        super(title, description, status, id);
    }

    public void setSubtasks(int subtasks) {
        this.subtasks.add(subtasks);
    }
    public void removeListSubtasks(){
        subtasks.clear();
    }
    public void removeSubtaskToList(int idSub){
        int indexOf = subtasks.indexOf(idSub);
        this.subtasks.remove(indexOf);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasks;
    }
}
