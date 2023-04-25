package model;

public class Subtack extends Task {
    private final int epicId;

    public Subtack(String title, String description, Progress status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
