package TasksManagers;

import manager.taskManagers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void loadInMemoryTaskManager() {
        taskManager = new InMemoryTaskManager();
    }
    @Test
    public void newManagerHasNoTasks(){
        assertIterableEquals(List.of(),taskManager.getTaskList(),"Новый менеджер содержит tasks");
        assertIterableEquals(List.of(),taskManager.getEpicList(),"Новый менеджер содержит epics");
        assertIterableEquals(List.of(),taskManager.getSubtackList(),"Новый менеджер содержит subtasks");
    }

}