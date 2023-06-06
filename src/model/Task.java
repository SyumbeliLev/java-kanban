package model;

import java.util.Objects;

public class Task {
    private final String title;
    private final String description;
    private int id;
    private Progress status;

    public Task(String title, String description, Progress status) {
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status);
    }
}
