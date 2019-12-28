package ru.javalab.myservletapp.model;

public class User {
    public String email;
    public String password;
    public String country;
    public String about;
    public String role;

    public User(){}

    public User(String email, String password, String country, String about) {
        this.email = email;
        this.password = password;
        this.country = country;
        this.about = about;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String country, String about, String role) {
        this.email = email;
        this.password = password;
        this.country = country;
        this.about = about;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
