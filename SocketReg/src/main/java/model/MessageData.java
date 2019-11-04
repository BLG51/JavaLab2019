package model;

import java.util.List;

public class MessageData {
    List<StoredMessage> data;

    public List<StoredMessage> getData() {
        return data;
    }

    public void setData(List<StoredMessage> data) {
        this.data = data;
    }
}
