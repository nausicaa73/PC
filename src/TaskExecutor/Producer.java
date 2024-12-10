package src.TaskExecutor;

import java.util.Random;

public class Producer extends Thread {
    IProdConsBuffer buffer;
    int id;
    int prodTime;
    int minProd;
    int maxProd;
    int maxQuantity;
    int minQuantity;

    public Producer(IProdConsBuffer buffer, int id, int minProd, int maxProd, int prodTime) {
        this.buffer = buffer;
        this.id = id;
        this.minProd = minProd;
        this.maxProd = maxProd;
        this.prodTime = prodTime;

    }

    public void run() {

        Random rand = new Random();
        int nb = rand.nextInt(maxProd - minProd) + minProd;
        for (int i = 0; i < nb; i++) {
            Message m = new Message("src.TaskExecutor.Tache");

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
