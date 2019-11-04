package servers;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import service.MessageService;
import service.RegService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RegServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;

    public RegServer() {
        clients = new ArrayList<>();
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        for (; ; ) {
            try {
                ClientHandler handler =
                        new ClientHandler(serverSocket.accept());
                handler.start();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader reader;
        private int hid;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            clients.add(this);
            System.out.println("New client!");
        }

        @Override
        public void run() {
            System.out.println("in run");
            try {
                reader = new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    //System.out.println(line);
                    ObjectMapper mapper = new ObjectMapper();
                    JsonObj json = mapper.readValue(line, JsonObj.class);
                    Header header = json.getHeader();
                    if (header.getTyp().equals("register")) {
                        register((User) json.getPayload());
                    } else if (header.getTyp().equals("login")) {
                        login((User) json.getPayload());
                    } else if (header.getTyp().equals("message") && hid != 0) {
                        for (ClientHandler client : clients) {
                            PrintWriter writer = new PrintWriter(
                                    client.clientSocket.getOutputStream(), true);
                            writer.println(hid+": " + json.getPayload());
                        }
                    } else if (header.getTyp().equals("message") && hid==0) {
                        PrintWriter writer = new PrintWriter(
                                this.clientSocket.getOutputStream(), true);
                        writer.println("you are not logged in");
                    } else if (header.getTyp().equals("command")) {
                        MessageData mdata = new MessageData();
                        mdata.setData(getMessages((GetMessagesCommand) json.getPayload()));
                        PrintWriter writer = new PrintWriter(
                                this.clientSocket.getOutputStream(), true);
                        writer.println(mapper.writeValueAsString(mdata));
                        break;
                    }
                }
                reader.close();
                clientSocket.close();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        private List<StoredMessage> getMessages(GetMessagesCommand com){
            MessageService serv = new MessageService();
            List<StoredMessage> list = serv.get(com.getPage(), com.getSize());
            return list;
        }

        private void login(User user) {
            RegService serv = new RegService();
            if (serv.isRegistered(user)) {
                User adata = serv.get(user.getLogin());
                hid = adata.getId();
            }
        }

        private void register(User user) {
            RegService serv = new RegService();
//            AuthData adata = new AuthData(login, password);
            if (!serv.isRegistered(user)) {
                serv.create(user);
                hid = serv.get(user.getLogin()).getId();
            }
        }

        private void sendMessageToAll(String line) throws IOException {
            for (ClientHandler client : clients) {
                PrintWriter writer = new PrintWriter(
                        client.clientSocket.getOutputStream(), true);
                writer.println(line);
            }
        }
    }
}
