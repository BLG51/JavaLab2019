import clients.RegClient;
import servers.RegServer;
import service.RegService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
            RegServer server = new RegServer();
            server.start(6000);

        RegClient client = new RegClient();
        client.startConnection("127.0.0.1", 6000);

        client.sendMessage("kektor, shmektor");
    }
}
