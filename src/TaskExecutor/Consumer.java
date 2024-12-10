package src.TaskExecutor;

public class Consumer extends Thread {
    ProdConsBuffer buffer;
    int id;
    int consTime;
    boolean running = false;

    public Consumer(ProdConsBuffer buffer, int id, int consTime) {
        this.buffer = buffer;
        this.id = id;
        this.consTime = consTime;
    }

    public void run() {
        while (true) {

            try {
                Message m = buffer.get();
                running = true;
                m.run();
                running = false;
                Thread.yield();
                Thread.sleep(consTime);
            } catch (InterruptedException e) {
                // System.out.println("Consumer " + id + " interrupted");
                return;
            }
        }

    }

}
