
import com.google.gson.Gson;
import manager.Managers;
import manager.taskManagers.HttpTaskManager;

import server.HttpTaskServer;
import server.KVServer;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        new KVServer().start();

        HttpTaskManager manager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(manager);
        Gson gson = Managers.getGson();
        server.start();
    }
}

