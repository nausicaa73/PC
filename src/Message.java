package src;

public class Message {
    int message;
    int quantite;

    public Message(int message, int quantite) {
        this.message = message;
        this.quantite = quantite;
    }

    public Message(int message) {
        this.message = message;
        this.quantite = 1;
    }

    public String toString() {
        return "" + message;
    }

}