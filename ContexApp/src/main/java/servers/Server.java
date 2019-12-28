package servers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import context.ApplicationContext;
import context.ApplicationContextReflectionBased;
import dispatcher.RequestDispatcher;
import service.MessageDataService;
import dto.MessagesDto;
import dto.TokenDto;
import model.*;
import protocol.Header;
import protocol.Request;
import service.AdminService;
import service.MessageService;
import service.AuthService;
import service.UserService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private final static String SECRET = "secret";

    public Server() {
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
        private ApplicationContext applicationContext;
        PrintWriter writer;
        ObjectMapper mapper;
        private RequestDispatcher dispatcher = new RequestDispatcher(applicationContext.getComponent(AuthService.class, "authService"));

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            clients.add(this);
            System.out.println("New client!");
            applicationContext = new ApplicationContextReflectionBased();
            mapper = new ObjectMapper();
            try {
                writer = new PrintWriter(
                        this.clientSocket.getOutputStream(), true);

                reader = new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            System.out.println("in run");
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    //System.out.println(line);

                    Request json = mapper.readValue(line, Request.class);
                    Header header = json.getHeader();
                    if (header.getTyp().equals("register")) {
                        User payload = mapper.convertValue(json.getPayload(), User.class);
                        register(payload);
                    } else if (header.getTyp().equals("login")) {
                        User payload = mapper.convertValue(json.getPayload(), User.class);
                        if (login(payload)) {
                            sendJWT();
                        }
                    } else {
                        String userToken = json.getToken();
                        if (!checkToken(userToken)) {
                            String s = "token is incorrect";
                            Request j = new Request(new Header("message"), s);
                            writer.println(mapper.writeValueAsString(j));
                        } else {
                            if (header.getTyp().equals("message") && hid != 0) {
                                String payload = mapper.convertValue(json.getPayload(), String.class);
                                sendMessageToAll(payload);
                            } else if (header.getTyp().equals("message") && hid == 0) {
                                String s = "you are not logged in";
                                Request j = new Request(new Header("message"), s);
                                writer.println(mapper.writeValueAsString(j));
                            } else if (header.getTyp().equals("command")) {
                                MessagesDto payload = mapper.convertValue(json.getPayload(), MessagesDto.class);
                                getMesLog(payload);
                            } else if (header.getTyp().equals("getall")) {
                                getProducts();
                            } else if (header.getTyp().equals("buy")) {
                                int buyid = mapper.convertValue(json.getPayload(), Integer.class);
                                buyProduct(buyid);
                            } else if (header.getTyp().equals("add")) {
                                Product p = mapper.convertValue(json.getPayload(), Product.class);
                                addProduct(p);
                            } else if (header.getTyp().equals("delete")) {
                                int delid = mapper.convertValue(json.getPayload(), Integer.class);
                                deleteProduct(delid);
                            }
                        }
                    }
                }
                reader.close();
                clientSocket.close();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                e.printStackTrace();
            }
        }

        private void sendJWT() throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
            Header h = new Header();
            h.setTyp("token");
            String token = getJWT();
            Request j = new Request(new Header(token), token);
            writer.println(mapper.writeValueAsString(j));
        }

        private void getMesLog(MessagesDto payload) throws JsonProcessingException {
            MessageDataService mdata = new MessageDataService();
            mdata.setData(getMessages(payload));
            Request j = new Request(new Header("pagination"), mdata);
            writer.println(mapper.writeValueAsString(j));
        }

        private void getProducts() throws JsonProcessingException {
            UserService us = applicationContext.getComponent(UserService.class, "userService");
            List<Product> l = us.getProducts();
            String s = l.toString();
            Request j = new Request(new Header("message"), s);
            writer.println(mapper.writeValueAsString(j));
        }

        private void buyProduct(int buyid) throws JsonProcessingException {
            UserService us = applicationContext.getComponent(UserService.class, "userService");
            try {
                us.buyProduct(hid, buyid);

                String s = "success";
                Request j = new Request(new Header("message"), s);
                writer.println(mapper.writeValueAsString(j));
            } catch (IllegalArgumentException e) {
                String s = "sorry, product not in stock";
                Request j = new Request(new Header("message"), s);
                writer.println(mapper.writeValueAsString(j));
            }
        }

        private void deleteProduct(int delid) throws JsonProcessingException {
            AdminService as = applicationContext.getComponent(AdminService.class, "adminService");
            try {
                as.deleteProduct(hid, delid);
                String s = "success";
                Request j = new Request(new Header("message"), s);
                writer.println(mapper.writeValueAsString(j));
            } catch (IllegalArgumentException e) {
                String s = "you're not admin";
                Request j = new Request(new Header("message"), s);
                writer.println(mapper.writeValueAsString(j));
            }
        }

        private void addProduct(Product p) throws JsonProcessingException {
            AdminService as = applicationContext.getComponent(AdminService.class, "adminService");
            try {
                as.addProduct(hid, p);
                String s = "success";
                Request j = new Request(new Header("message"), s);
                writer.println(mapper.writeValueAsString(j));
            } catch (IllegalArgumentException e) {
                String s = "you're not admin";
                Request j = new Request(new Header("message"), s);
                writer.println(mapper.writeValueAsString(j));
            }
        }

        private List<StoredMessage> getMessages(MessagesDto com) {
            MessageService serv = applicationContext.getComponent(MessageService.class, "messageService");
            List<StoredMessage> list = serv.get(com.getPage(), com.getSize());
            return list;
        }


        private boolean login(User user) {
            AuthService serv = applicationContext.getComponent(AuthService.class, "authService");
            if (serv.isRegistered(user)) {
                User adata = serv.get(user.getLogin());
                hid = adata.getId();
                return true;
            }
            return false;
        }

        private boolean checkToken(String token) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
            String realtoken = getJWT();
            return (realtoken.equals(token));
        }

        private String getJWT() throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException {
            AuthService serv = applicationContext.getComponent(AuthService.class, "authService");
            ObjectMapper mapper = new ObjectMapper();
            Header header = new Header();
            header.setAlg("HS256");
            header.setTyp("JWT");
            TokenDto u = new TokenDto();
            u.setId(hid);
            u.setRole(serv.getById(hid).getRole());
            String hjson = mapper.writeValueAsString(header);
            String pjson = mapper.writeValueAsString(u);

            String unsignedtoken = Base64.getUrlEncoder().encodeToString(hjson.getBytes()) + "." +
                    Base64.getUrlEncoder().encodeToString(pjson.getBytes());
            Mac sha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretkey = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
            sha256.init(secretkey);
            byte[] signature = (sha256.doFinal(unsignedtoken.getBytes()));
            String token = unsignedtoken + "." + Base64.getUrlEncoder().encodeToString(signature);
            return token;
        }

        private void register(User user) {
            AuthService serv = applicationContext.getComponent(AuthService.class, "authService");
//            AuthData adata = new AuthData(login, password);
            if (!serv.isRegistered(user)) {
                serv.create(user);
                hid = serv.get(user.getLogin()).getId();
            }
        }

        private void sendMessageToAll(String line) throws IOException {
            String s = hid + ": " + line;
            Request j = new Request(new Header("message"), s);
            for (ClientHandler client : clients) {
                PrintWriter writer = new PrintWriter(
                        client.clientSocket.getOutputStream(), true);
                writer.println(mapper.writeValueAsString(j));
            }
        }
    }
}
