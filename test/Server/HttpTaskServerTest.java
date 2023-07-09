package Server;

import com.google.gson.Gson;

import manager.Managers;

import manager.taskManagers.TaskManager;
import model.Epic;
import model.Progress;
import model.Subtack;
import model.Task;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


public class HttpTaskServerTest {

    private HttpTaskServer server;
    private TaskManager manager;

    private final Task task1 = new Task("Task1", "Task1Description", Progress.NEW, 100, LocalDateTime.of(2020, 1, 1, 1, 1));
    private final Task task2 = new Task("Task2", "Task1Description", Progress.NEW, 100, LocalDateTime.of(2020, 6, 1, 1, 1));

    private final Epic epic1 = new Epic("Epic1", "Epic1Description");

    private final Subtack subtask1 = new Subtack("Subtask1", "Subtask1Description", Progress.NEW, 100, LocalDateTime.of(2021, 1, 1, 1, 1), 3);

    private final Subtack subtask2 = new Subtack("Subtask2", "Subtask2Description", Progress.NEW, 100, LocalDateTime.of(2022, 1, 1, 1, 1), 3);


    private Gson gson;

    @BeforeAll
    public static void startKvServer() throws IOException {
        new KVServer().start();
    }

    @BeforeEach
    public void Before() throws IOException, InterruptedException {

        manager = Managers.getDefault();

        gson = Managers.getGson();
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addSubtack(subtask1);
        manager.addSubtack(subtask2);
        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getSubtackById(4);
        manager.getSubtackById(5);

        server = new HttpTaskServer(manager);
        server.start();
    }

    @AfterEach
    public void serverStop() {
        server.stop();
    }

    @Test
    public void getAllTasksTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(gson.toJson(manager.getTaskList()), response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void getAllEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(gson.toJson(manager.getEpicList()), response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void getAllSubtaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(gson.toJson(manager.getSubtackList()), response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void getHistoryTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(gson.toJson(manager.getHistory()), response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void getPrioritizedTasksTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(gson.toJson(manager.getPrioritizedTasks()), response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void getTaskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(gson.toJson(manager.getTaskById(2)), response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void getEpicByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(gson.toJson(manager.getEpicById(3)), response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void getSubtaskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(gson.toJson(manager.getSubtackById(5)), response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void removeAllTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(manager.getTaskList(), List.of());
    }

    @Test
    public void removeAllEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(manager.getEpicList(), List.of());
        assertEquals(manager.getSubtackList(), List.of());
    }

    @Test
    public void removeAllSubtaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(manager.getSubtackList(), List.of());
    }

    @Test
    public void removeTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=2");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getTaskList().size());
    }

    @Test
    public void removeEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getEpicList().size());
    }

    @Test
    public void removeSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=5");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getSubtackList().size());
    }

    @Test
    public void postTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Task newTask = new Task("newTask", "newTaskDescription", Progress.NEW, 100, LocalDateTime.of(2020, 3, 1, 1, 1));
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(3, manager.getTaskList().size());
    }

    @Test
    public void postEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Epic newEpic = new Epic("newEpic", "newEpicDescription");
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(newEpic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(2, manager.getEpicList().size());
    }

    @Test
    public void postSubtaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Subtack newSubtask = new Subtack("newSubtask", "newSubtaskDescription", Progress.NEW, 100, LocalDateTime.of(2022, 4, 1, 1, 1), 3);
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        String json = gson.toJson(newSubtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(3, manager.getSubtackList().size());
    }

    @Test
    public void badRequestTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/asds/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("Этот запрос не обрабатывается", response.body());
        assertEquals(405, response.statusCode());
    }
}

