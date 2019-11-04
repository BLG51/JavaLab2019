package model;

public class User {
    int id;
    AuthData auth;
    String role;

    public User(int id, String login, String password, String role) {
      auth = new AuthData(login, password);
      this.id = id;
      this.role = role;
    }

    public User(String login, String password, String role) {
        auth = new AuthData(login, password);
        this.role = role;
    }

    public User(String login, String password) {
        auth = new AuthData(login, password);
    }

    public String getLogin(){
        return getAuth().getLogin();
    }

    public void setLogin(String login){
        getAuth().setLogin(login);
    }

    public String getPassword(){
        return getAuth().getPassword();
    }

    public void setPassword(String password){
        getAuth().setPassword(password);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AuthData getAuth() {
        return auth;
    }

    public void setAuth(AuthData auth) {
        this.auth = auth;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
