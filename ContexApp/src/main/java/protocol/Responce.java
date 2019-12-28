package protocol;

public class Responce<T> {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Responce(T data) {
        this.data = data;
    }

    public static <G> Responce<G> build(G data) {
        return new Responce<>(data);
    }
}
