package src;

public class Message {
    int message;
    boolean estLu = false;

    public Message(int message) {
        this.message = message;
    }

    public String toString() {
        return "" + message;
    }

    public void lu() {
        estLu = true;
    }

    public boolean estLu() {
        return estLu;
    }
}