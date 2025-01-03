package src;

import java.util.Random;

public class Producer extends Thread {
    IProdConsBuffer buffer;
    int id;
    int prodTime;
    int minProd;
    int maxProd;
    int maxQuantity;
    int minQuantity;

    public Producer(IProdConsBuffer buffer, int id, int minProd, int maxProd, int prodTime, int minQuantity,
            int maxQuantity) {
        this.buffer = buffer;
        this.id = id;
        this.minProd = minProd;
        this.maxProd = maxProd;
        this.prodTime = prodTime;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;

    }

    public void run() {

        Random rand = new Random();
        int nb = rand.nextInt(maxProd - minProd) + minProd;
        for (int i = 0; i < nb; i++) {
            Message m;
            if (maxQuantity - minQuantity == 0) {
                m = new Message(id, minQuantity);
            } else {
                m = new Message(id, rand.nextInt(maxQuantity - minQuantity) + minQuantity);
            }
            try {
                buffer.put(m);
                Thread.yield();
                Thread.sleep(prodTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
