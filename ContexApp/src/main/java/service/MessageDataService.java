package service;

import context.Component;
import model.StoredMessage;

import java.util.List;

public class MessageDataService implements Component {
    List<StoredMessage> data;

    public List<StoredMessage> getData() {
        return data;
    }

    public void setData(List<StoredMessage> data) {
        this.data = data;
    }

    @Override
    public String getName() {
        return "messageDataService";
    }
}
