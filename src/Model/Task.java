package Model;

public class Task {
    private String title;
    private String Description;
    private int id;
    private progress status;

    public enum progress {
        NEW,
        IN_PROGRESS,
        DONE
    }

    @Override
    public String toString() {
        return
                getClass() + "{" +
                        "title = '" + title + '\'' +
                        ", Description = '" + Description + '\'' +
                        ", id = " + id +
                        ", status = " + status +
                        '}';
    }

    public Task(String title, String description, int id, progress status) {
        this.title = title;
        Description = description;
        this.id = id;
        this.status = status;
    }

    public progress getStatus() {
        return status;
    }

    public void setStatus(progress status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
