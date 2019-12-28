package ru.javalab.myservletapp.model;

public class RandomString {
    int num;
    String id;

    public RandomString(int num, String id) {
        this.num = num;
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
