package clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.GetMessagesCommand;
import model.Header;
import model.JsonObj;
import model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RegClient {

    private Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader reader;

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            new Thread(receiveMessagesTask).start();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void sendMessage(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Header header = new Header();
        JsonObj json = new JsonObj();
        String[] arr = message.split(" ");
        switch (arr[0]) {
            case "register":
                header.setTyp("register");
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                User reguser = new User(arr[1], encoder.encode(arr[2]));
                json.setPayload(reguser);
                break;
            case "login":
                header.setTyp("login");
                User loguser = new User(arr[1], arr[2]);
                json.setPayload(loguser);
                break;
            case "message":
                header.setTyp("message");
                json.setPayload(message);
                break;
            case "get_messages":
                header.setTyp("command");
                GetMessagesCommand com = new GetMessagesCommand(Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
                json.setPayload(com);
                break;
        }
        json.setHeader(header);
        writer.println(mapper.writeValueAsString(json));
    }

    private Runnable receiveMessagesTask = new Runnable() {
        public void run() {
            while (true) {
                try {
                    String message = reader.readLine();
                    System.out.println(message);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    };


}
