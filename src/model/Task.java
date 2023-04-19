package model;

public class Task {
    private String title;
    private String description;
    private int id;
    private Progress status;

    public Task(String title, String description, Progress status, int id) {
        this.title = title;
        this.description = description;
        this.status = status;
    }



    @Override
    public String toString() {
        return
                getClass() + "{" +
                        "title = '" + title + '\'' +
                        ", description = '" + description + '\'' +
                        ", id = " + id +
                        ", status = " + status +
                        '}';
    }
    public Progress getStatus() {
        return status;
    }

    public void setStatus(Progress status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
