package HistoryManager;

import manager.Managers;
import manager.historyManagers.HistoryManager;

import model.Epic;
import model.Progress;
import model.Subtack;
import model.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task;
    private Epic epic;
    private Subtack subtack;

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
        task = new Task("TitleTask", "DescriptionTask", Progress.NEW,20, LocalDateTime.of(2022, 2, 1, 1, 1));
        epic = new Epic("TitleEpic", "DescriptionEpic");
        subtack = new Subtack("TitleSubtack", "DescriptionSubtack", Progress.DONE,20,LocalDateTime.of(2022, 2, 1, 1, 1),2);

        task.setId(1);
        epic.setId(2);
        subtack.setId(3);

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtack);
    }

    @Test
    public void removeFirstTest() {
        historyManager.remove(1);
        assertEquals(List.of(epic, subtack), historyManager.getHistory(), "Не удаляет в начале");
    }

    @Test
    public void removeMiddleTest() {
        historyManager.remove(2);
        assertEquals(List.of(task, subtack), historyManager.getHistory(), "Не удаляет в середине");
    }

    @Test
    public void removeLastTest() {
        historyManager.remove(3);
        assertEquals(List.of(task, epic), historyManager.getHistory(), "Не удаляет в конце");
    }

    @Test
    public void getHistoryTest() {
        assertEquals(List.of(task, epic, subtack), historyManager.getHistory(), "Не выдает историю, также проверка на добавления");

        historyManager.remove(1);
        historyManager.remove(2);
        historyManager.remove(3);

        assertEquals(List.of(), historyManager.getHistory(), "Не выдает пустую историю");
    }

    @Test
    public void addHistoryTest() {
        assertNotNull(historyManager.getHistory(), "Не добавляет в историю");
        assertEquals(List.of(task,epic,subtack),historyManager.getHistory(),"Содержит неверные элементы");
    }

    @Test
    public void historyDeduplicationTest(){
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(epic);
        assertEquals(List.of(subtack,task,epic),historyManager.getHistory());
        assertEquals(3,historyManager.getHistory().size());

    }
}
