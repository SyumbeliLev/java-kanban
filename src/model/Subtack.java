package model;

public class Subtack extends Task {
    private final int epicId;

    public Subtack(String title, String description, Progress status, int id, int epicId) {
        super(title, description, status, id);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
