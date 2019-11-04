package model;

public class GetMessagesCommand {
    private String command;
    private int page;

    public GetMessagesCommand(int page, int size) {
        this.command = "get messages";
        this.page = page;
        this.size = size;
    }

    public String getCommand() {
        return command;
    }

    private int size;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
