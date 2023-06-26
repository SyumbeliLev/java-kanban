package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtack extends Task {
    private final int epicId;

    public Subtack(String title, String description, Progress status, int duration, LocalDateTime startTime, int epicId) {
        super(title, description, status, duration, startTime);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return getId() + "," + TaskType.SUBTASK + "," + getTitle() +','+ getStatus() + "," + getDescription() + "," + getStartTime().format(DATE_TIME_FORMATTER) + "," + getDuration() + "," + getEndTime().format(DATE_TIME_FORMATTER) + "," + getEpicId();
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
