package clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RegClient {

    private Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader reader;
    private static String token;

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            new Thread(receiveMessagesTask).start();
            new Thread(sendMessageTask).start();
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
                json.setToken(token);
                break;
            case "get_messages":
                header.setTyp("command");
                GetMessagesCommand com = new GetMessagesCommand(Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
                json.setPayload(com);
                json.setToken(token);
                break;
            case "list":
                header.setTyp("list");
                json.setToken(token);
                break;
            case "buy":
                header.setTyp("buy");
                int buyid = Integer.parseInt(arr[1]);
                json.setPayload(buyid);
                json.setToken(token);
                break;
            case "getall":
                header.setTyp("getall");
                json.setToken(token);
                break;
            case "add":
                header.setTyp("add");
                Product addprod = new Product(); addprod.setName(arr[1]);
                addprod.setPrice(Integer.parseInt(arr[2]));
                addprod.setCount(Integer.parseInt(arr[3]));
                json.setPayload(addprod);
                json.setToken(token);
                break;
            case "delete":
                header.setTyp("delete");
                int delid = Integer.parseInt(arr[1]);
                json.setPayload(delid);
                json.setToken(token);
                break;
        }
        json.setHeader(header);
        writer.println(mapper.writeValueAsString(json));
    }

    private Runnable sendMessageTask = new Runnable() {
        @Override
        public void run() {
            for (;;) {
                Scanner in = new Scanner(System.in);
                try {
                    sendMessage(in.nextLine());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Runnable receiveMessagesTask = new Runnable() {
        ObjectMapper mapper = new ObjectMapper();
        public void run() {
            while (true) {
                try {
                    String message = reader.readLine();
                    JsonObj json = mapper.readValue(message, JsonObj.class);
                    Header header = json.getHeader();
                    if (header.getTyp().equals("message")){
                        String mes = mapper.convertValue(json.getPayload(), String.class);
                        System.out.println(mes);

                    } else if (header.getTyp().equals("token")){
                        RegClient.token = mapper.convertValue(json.getPayload(), String.class);
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    };


}
