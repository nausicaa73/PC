package src.TaskExecutor;

import java.util.Random;

public class Consumer extends Thread {
    IProdConsBuffer buffer;
    int id;
    int consTime;
    int maxCons;
    int minCons;

    public Consumer(IProdConsBuffer buffer, int id, int consTime, int minCons, int maxCons) {
        this.buffer = buffer;
        this.id = id;
        this.consTime = consTime;
        this.minCons = minCons;
        this.maxCons = maxCons;
    }

    public void run() {

        while (true) {
            Random rand = new Random();
            try {
                Message m = buffer.get(rand.nextInt(maxCons - minCons + 1) + minCons)[0];
                m.run();
                Thread.yield();
                Thread.sleep(consTime);
            } catch (InterruptedException e) {
                // System.out.println("Consumer " + id + " interrupted");
                return;
            }
        }
    }

}
