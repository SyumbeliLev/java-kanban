package TasksManagers;

import manager.taskManagers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void loadInMemoryTaskManager() {
        taskManager = new InMemoryTaskManager();
    }

}