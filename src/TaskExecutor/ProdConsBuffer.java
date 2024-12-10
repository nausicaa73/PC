package src.TaskExecutor;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {
    Message[] buffer;
    int in = 0;
    int out = 0;
    int size = 0;
    int capacity;
    int totmsg = 0;

    public ProdConsBuffer(int capacity) {
        this.capacity = capacity;
        buffer = new Message[capacity];
    }

    public synchronized void put(Message o) {
        while (size == capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        buffer[in] = o;
        totmsg++;
        in = (in + 1) % capacity;
        size++;
        print_buffer();
        notifyAll();
    }

    public synchronized Message get() throws InterruptedException {
        while (size == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw e;
            }
        }
        Message o = buffer[out];
        out = (out + 1) % capacity;
        size--;
        // mutex.release();
        print_buffer();
        notifyAll();
        return o;

    }

    @Override
    public synchronized Message[] get(int k) throws InterruptedException {
        // TODO Auto-generated method stub
        // mutex.acquire();
        System.out.println("Consumer " + Thread.currentThread().getId() + " consumed " + k);
        Message[] res = new Message[k];
        for (int i = 0; i < k; i++) {
            while (size == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw e;
                }
            }
            res[i] = buffer[out];
            out = (out + 1) % capacity;
            size--;

            print_buffer();
        }

        // mutex.release();

        notifyAll();
        return res;

    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

    public String get_remplissage(char c, int size) {
        String res = "";
        for (int i = 0; i < size; i++) {
            res += c;
        }
        return res;
    }

    public void print_buffer() {
        System.out.println("Size: " + size + " Capacity: " + capacity);

        System.out.print(" ");
        for (int i = 0; i < capacity; i++) {
            if (i == in && i == out) {

                System.out.print(get_remplissage(' ', buffer[i] == null ? 3 : (buffer[i].toString().length()) / 2 + 1)
                        + "IO" + get_remplissage(' ',
                                buffer[i] == null ? 3 : (buffer[i].toString().length()) / 2 + 1));
            } else if (i == in) {
                System.out.print(get_remplissage(' ', buffer[i] == null ? 3 : (buffer[i].toString().length()) / 2 + 1)
                        + "I" + get_remplissage(' ',
                                buffer[i] == null ? 3 : (buffer[i].toString().length()) / 2 + 2));
            } else if (i == out) {
                System.out.print(get_remplissage(' ', buffer[i] == null ? 3 : (buffer[i].toString().length()) / 2 + 1)
                        + "O" + get_remplissage(' ',
                                buffer[i] == null ? 3 : (buffer[i].toString().length()) / 2 + 2));
            } else {

                if (buffer[i] == null) {
                    System.out.print("      ");
                } else {
                    for (int j = 0; j < buffer[i].toString().length() + 3; j++) {
                        System.out.print(" ");
                    }
                }
            }
        }
        System.out.println();
        for (int i = 0; i < capacity; i++) {
            if (buffer[i] == null) {
                System.out.print("_______");
            } else {
                for (int j = 0; j < buffer[i].toString().length() + 3; j++) {
                    System.out.print("_");
                }
            }
        }
        System.out.println();
        System.out.print("| ");
        for (int i = 0; i < capacity; i++) {
            System.out.print(buffer[i] + " | ");
        }
        System.out.println();
        for (int i = 0; i < capacity; i++) {
            if (buffer[i] == null) {
                System.out.print("‾‾‾‾‾‾‾");
            } else {
                for (int j = 0; j < buffer[i].toString().length() + 3; j++) {
                    System.out.print("‾");
                }
            }
        }
        System.out.println();
    }

    @Override
    public int nmsg() {
        // TODO Auto-generated method stub
        return size;
    }

    @Override
    public int totmsg() {
        // TODO Auto-generated method stub
        return totmsg;
    }

}
