package Model;

public class Subtack extends Task {
    private int epicId = 0;
    public Subtack(String title, String description, int id, Task.progress status) {
        super(title, description, id, status);
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
