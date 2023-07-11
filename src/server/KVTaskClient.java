package server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final String url;


    private String API_TOKEN;

    public KVTaskClient(String url) {
        this.url = url;
        API_TOKEN = registrationApiToken();

    }

    private String registrationApiToken() {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            API_TOKEN = response.body();

            if (response.statusCode() != 200) {
                throw new IOException();
            }
        } catch (InterruptedException | IOException e) {
            System.out.println("Во время выполнения регистрации токена возникла ошибка.\n" +
                    e.getMessage());
        }
        return API_TOKEN;
    }

    public void put(String key, String json) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "save/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json, DEFAULT_CHARSET);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException();
            }
        } catch (InterruptedException | IOException e) {
            System.out.println("Во время выполнения сохранения возникла ошибка.\n" +
                    e.getMessage());
        }
    }

    public String load(String key) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "load/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException();
            }
        } catch (InterruptedException | IOException e) {
            System.out.println("Во время выполнения загрузки возникла ошибка.\n" +
                    e.getMessage());
        }
        if (response != null) return response.body();
        else return "Файл не найден";
    }


}
