package servers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import service.AdminService;
import service.MessageService;
import service.RegService;
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

public class RegServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private final static String SECRET = "secret";

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
                        User payload = mapper.convertValue(json.getPayload(), User.class);
                        register(payload);
                    } else if (header.getTyp().equals("login")) {
                        User payload = mapper.convertValue(json.getPayload(), User.class);
                        if (login(payload)) {
                            Header h = new Header();
                            h.setTyp("token");
                            String token = getJWT();
                            JsonObj j = new JsonObj(new Header(token), token);

                            PrintWriter writer = new PrintWriter(
                                    this.clientSocket.getOutputStream(), true);
                            writer.println(mapper.writeValueAsString(j));
                        }
                        ;
                    } else {
                        String userToken = json.getToken();
                        if (!checkToken(userToken)) {
                            String s = "token is incorrect";
                            JsonObj j = new JsonObj(new Header("message"), s);
                            PrintWriter writer = new PrintWriter(
                                    this.clientSocket.getOutputStream(), true);
                            writer.println(mapper.writeValueAsString(j));
                        } else {
                            if (header.getTyp().equals("message") && hid != 0) {
                                String payload = mapper.convertValue(json.getPayload(), String.class);
                                String s = hid + ": " + payload;
                                JsonObj j = new JsonObj(new Header("message"), s);
                                for (ClientHandler client : clients) {
                                    PrintWriter writer = new PrintWriter(
                                            client.clientSocket.getOutputStream(), true);
                                    writer.println(mapper.writeValueAsString(j));
                                }
                            } else if (header.getTyp().equals("message") && hid == 0) {
                                String s = "you are not logged in";
                                JsonObj j = new JsonObj(new Header("message"), s);
                                PrintWriter writer = new PrintWriter(
                                        this.clientSocket.getOutputStream(), true);
                                writer.println(mapper.writeValueAsString(j));
                            } else if (header.getTyp().equals("command")) {
                                MessageData mdata = new MessageData();
                                GetMessagesCommand payload = mapper.convertValue(json.getPayload(), GetMessagesCommand.class);
                                mdata.setData(getMessages(payload));
                                JsonObj j = new JsonObj(new Header("pagination"), mdata);
                                PrintWriter writer = new PrintWriter(
                                        this.clientSocket.getOutputStream(), true);
                                writer.println(mapper.writeValueAsString(j));
                            } else if (header.getTyp().equals("buy")) {
                                int buyid = mapper.convertValue(json.getPayload(), Integer.class);
                                UserService us = new UserService();
                                try {
                                    us.buyProduct(hid, buyid);

                                    String s = "success";
                                    JsonObj j = new JsonObj(new Header("message"), s);
                                    PrintWriter writer = new PrintWriter(
                                            this.clientSocket.getOutputStream(), true);
                                    writer.println(mapper.writeValueAsString(j));
                                } catch (IllegalArgumentException e) {
                                    String s = "sorry, product not in stock";
                                    JsonObj j = new JsonObj(new Header("message"), s);
                                    PrintWriter writer = new PrintWriter(
                                            this.clientSocket.getOutputStream(), true);
                                    writer.println(mapper.writeValueAsString(j));
                                }
                            } else if (header.getTyp().equals("add")) {
                                AdminService as = new AdminService();
                                Product p = mapper.convertValue(json.getPayload(), Product.class);
                                try {
                                    as.addProduct(hid, p);
                                    String s = "success";
                                    JsonObj j = new JsonObj(new Header("message"), s);
                                    PrintWriter writer = new PrintWriter(
                                            this.clientSocket.getOutputStream(), true);
                                    writer.println(mapper.writeValueAsString(j));
                                } catch (IllegalArgumentException e) {
                                    String s = "you're not admin";
                                    JsonObj j = new JsonObj(new Header("message"), s);
                                    PrintWriter writer = new PrintWriter(
                                            this.clientSocket.getOutputStream(), true);
                                    writer.println(mapper.writeValueAsString(j));
                                }
                            } else if (header.getTyp().equals("delete")) {
                                AdminService as = new AdminService();
                                int delid = mapper.convertValue(json.getPayload(), Integer.class);
                                try {
                                    as.deleteProduct(hid, delid);
                                    String s = "success";
                                    JsonObj j = new JsonObj(new Header("message"), s);
                                    PrintWriter writer = new PrintWriter(
                                            this.clientSocket.getOutputStream(), true);
                                    writer.println(mapper.writeValueAsString(j));
                                } catch (IllegalArgumentException e) {
                                    String s = "you're not admin";
                                    JsonObj j = new JsonObj(new Header("message"), s);
                                    PrintWriter writer = new PrintWriter(
                                            this.clientSocket.getOutputStream(), true);
                                    writer.println(mapper.writeValueAsString(j));
                                }
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

        private List<StoredMessage> getMessages(GetMessagesCommand com) {
            MessageService serv = new MessageService();
            List<StoredMessage> list = serv.get(com.getPage(), com.getSize());
            return list;
        }


        private boolean login(User user) {
            RegService serv = new RegService();
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
            RegService serv = new RegService();
            ObjectMapper mapper = new ObjectMapper();
            Header header = new Header();
            header.setAlg("HS256");
            header.setTyp("JWT");
            TokenData u = new TokenData();
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
