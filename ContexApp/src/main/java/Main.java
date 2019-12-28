import clients.Client;
import servers.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(6000);

        Client client = new Client();
        client.startConnection("127.0.0.1", 6000);
    }
}
