package model;

import java.util.Objects;

public class Subtack extends Task {
    private final int epicId;

    public Subtack(String title, String description, Progress status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtack subtack = (Subtack) o;
        return epicId == subtack.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(epicId);
    }
}
