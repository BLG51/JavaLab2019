package ru.javalab.myservletapp.model;

public class Review {
    String email;
    String rname;
    int rmark;
    String rreview;
    String date;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public int getRmark() {
        return rmark;
    }

    public void setRmark(int rmark) {
        this.rmark = rmark;
    }

    public String getRreview() {
        return rreview;
    }

    public void setRreview(String rreview) {
        this.rreview = rreview;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Review(String email, String rname, int rmark, String rreview, String date) {
        this.email = email;
        this.rname = rname;
        this.rmark = rmark;
        this.rreview = rreview;
        this.date = date;
    }
}
