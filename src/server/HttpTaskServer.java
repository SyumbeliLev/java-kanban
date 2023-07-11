package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;

import manager.taskManagers.TaskManager;
import model.Epic;
import model.Subtack;
import model.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager manager;

    public TaskManager getManager() {
        return manager;
    }

    public HttpTaskServer() throws IOException {
        manager = Managers.getDefault();
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new handleTasks());
    }

    public void start() {
        System.out.println("Started server " + PORT);
        System.out.println("http://localhost:" + PORT + "/tasks");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server stopped on port " + PORT);
    }

    private class handleTasks implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            int taskId = 0;
            int rCode = 200;
            String response = null;
            boolean exists = false;
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath() + exchange.getRequestURI().getQuery(), exchange.getRequestMethod());

            if (exchange.getRequestURI().getQuery() != null && exchange.getRequestURI().getQuery().contains("=")) {
                int index = exchange.getRequestURI().getQuery().indexOf("=");
                taskId = Integer.parseInt(exchange.getRequestURI().getQuery().substring(index + 1));
            }

            try {
                switch (endpoint) {
                    case GET_HISTORY:
                        response = gson.toJson(manager.getHistory());
                        break;
                    case GET_PRIORITIZED_TASK:
                        response = gson.toJson(manager.getPrioritizedTasks());
                        break;
                    case GET_TASKS:
                        response = gson.toJson(manager.getTaskList());
                        break;
                    case GET_TASK_BY_ID:
                        response = gson.toJson(manager.getTaskById(taskId));
                        break;
                    case GET_EPICS:
                        response = gson.toJson(manager.getEpicList());
                        break;
                    case GET_EPIC_BY_ID:
                        response = gson.toJson(manager.getEpicById(taskId));
                        break;
                    case GET_SUBTASKS:
                        response = gson.toJson(manager.getSubtackList());
                        break;
                    case GET_SUBTASK_BY_ID:
                        response = gson.toJson(manager.getSubtackById(taskId));
                        break;
                    case GET_EPIC_SUBTASKS_ID:
                        response = gson.toJson(manager.getSubtackEpic(taskId));
                        break;

                    case DELETE_ALL_TASKS:
                        manager.removeAllTask();
                        response = "Все таски удалены";
                        break;
                    case DELETE_TASK_BY_ID:
                        manager.removeTaskById(taskId);
                        response = "Задача с id = " + taskId + " удалена";
                        break;
                    case DELETE_ALL_EPICS:
                        manager.removeAllEpic();
                        response = "Все эпики удалены";
                        break;
                    case DELETE_EPIC_BY_ID:
                        manager.removeEpicById(taskId);
                        response = "Эпик с id = " + taskId + " удален";
                        break;
                    case DELETE_ALL_SUBTASKS:
                        manager.removeAllSubtack();
                        response = "Все подзадачи удалены";
                        break;
                    case DELETE_SUBTASK_BY_ID:
                        manager.removeSubtackById(taskId);
                        response = "Подзадача с id = " + taskId + " удалена";
                        break;

                    case POST_ADD_OR_UPDATE_TASK:
                        try {
                            Task task = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8), Task.class);
                            for (Task allTask : manager.getPrioritizedTasks()) {
                                if (allTask.getId() == task.getId()) {
                                    exists = true;
                                    break;
                                }
                            }
                            if (exists) {
                                manager.updateTask(task);
                                response = "Задача обновлена";
                            } else {
                                manager.addTask(task);
                                response = "Задача добавлена";
                            }
                            break;

                        } catch (JsonSyntaxException e) {
                            writeResponse(exchange, "Получен некорректный JSON", 400);
                        }
                    case POST_ADD_OR_UPDATE_EPIC:
                        try {
                            Epic epic = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8), Epic.class);

                            for (Task allTask : manager.getPrioritizedTasks()) {
                                if (allTask.getId() == epic.getId()) {
                                    exists = true;
                                    break;
                                }
                            }
                            if (exists) {
                                manager.updateEpic(epic);
                                response = "Эпик обновлен";
                            } else {
                                manager.addEpic(epic);
                                response = "Эпик добавлен";
                            }
                            break;

                        } catch (JsonSyntaxException e) {
                            writeResponse(exchange, "Получен некорректный JSON", 400);
                        }
                    case POST_ADD_OR_UPDATE_SUBTASK:
                        exists = false;
                        try {
                            Subtack subtack = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8), Subtack.class);

                            for (Task allTask : manager.getPrioritizedTasks()) {
                                if (allTask.getId() == subtack.getId()) {
                                    exists = true;
                                    break;
                                }
                            }
                            if (exists) {
                                manager.updateSubtack(subtack);
                                response = "Задача обновлена";
                            } else {
                                manager.addSubtack(subtack);
                                response = "Задача добавлена";
                            }
                            break;

                        } catch (JsonSyntaxException e) {
                            writeResponse(exchange, "Получен некорректный JSON", 400);
                        }
                    case UNKNOWN:
                        rCode = 405;
                        response = "Этот запрос не обрабатывается";

                }

                writeResponse(exchange, response, rCode);

            } catch (IOException e) {
                System.out.println("Ошибка выполнения запроса :" + e.getMessage());
            } finally {
                exchange.close();
            }
        }

        private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
            if (responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
        }

        private Endpoint getEndpoint(String requestPath, String requestMethod) {
            switch (requestMethod) {
                case "POST":
                    if (Pattern.matches("^/tasks/task/null$", requestPath)) {
                        return Endpoint.POST_ADD_OR_UPDATE_TASK;
                    } else if (Pattern.matches("^/tasks/epic/null$", requestPath)) {
                        return Endpoint.POST_ADD_OR_UPDATE_EPIC;
                    } else if (Pattern.matches("^/tasks/subtask/null$", requestPath)) {
                        return Endpoint.POST_ADD_OR_UPDATE_SUBTASK;
                    } else return Endpoint.UNKNOWN;

                case "GET":
                    if (Pattern.matches("^/tasks/null$", requestPath)) {
                        return Endpoint.GET_PRIORITIZED_TASK;
                    } else if (Pattern.matches("^/tasks/task/null$", requestPath)) {
                        return Endpoint.GET_TASKS;
                    } else if (Pattern.matches("^/tasks/task/?id=\\d+$", requestPath)) {
                        return Endpoint.GET_TASK_BY_ID;
                    } else if (Pattern.matches("^/tasks/epic/null$", requestPath)) {
                        return Endpoint.GET_EPICS;
                    } else if (Pattern.matches("^/tasks/epic/?id=\\d+$", requestPath)) {
                        return Endpoint.GET_EPIC_BY_ID;
                    } else if (Pattern.matches("^/tasks/subtask/epic/?id=\\d+$", requestPath)) {
                        return Endpoint.GET_EPIC_SUBTASKS_ID;
                    } else if (Pattern.matches("^/tasks/subtask/null$", requestPath)) {
                        return Endpoint.GET_SUBTASKS;
                    } else if (Pattern.matches("^/tasks/subtask/?id=\\d+$", requestPath)) {
                        return Endpoint.GET_SUBTASK_BY_ID;
                    } else if (Pattern.matches("^/tasks/history/null$", requestPath)) {
                        return Endpoint.GET_HISTORY;
                    } else return Endpoint.UNKNOWN;
                case "DELETE":
                    if (Pattern.matches("^/tasks/task/null$", requestPath)) {
                        return Endpoint.DELETE_ALL_TASKS;
                    } else if (Pattern.matches("^/tasks/task/?id=\\d+$", requestPath)) {
                        return Endpoint.DELETE_TASK_BY_ID;
                    } else if (Pattern.matches("^/tasks/epic/null$", requestPath)) {
                        return Endpoint.DELETE_ALL_EPICS;
                    } else if (Pattern.matches("^/tasks/epic/?id=\\d+$", requestPath)) {
                        return Endpoint.DELETE_EPIC_BY_ID;
                    } else if (Pattern.matches("^/tasks/subtask/null$", requestPath)) {
                        return Endpoint.DELETE_ALL_SUBTASKS;
                    } else if (Pattern.matches("^/tasks/subtask/?id=\\d+$", requestPath)) {
                        return Endpoint.DELETE_SUBTASK_BY_ID;
                    } else return Endpoint.UNKNOWN;
                default:
                    return Endpoint.UNKNOWN;
            }
        }
    }

}
