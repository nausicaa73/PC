package src;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer_synchrone implements IProdConsBuffer {
    Message[] buffer;
    Semaphore[] mutex;
    int compteur;
    int in = 0;
    int out = 0;
    int size = 0;
    int capacity;
    int totmsg = 0;
    Semaphore s = new Semaphore(1, true);

    public ProdConsBuffer_synchrone(int capacity) {
        this.capacity = capacity;
        buffer = new Message[capacity];
        mutex = new Semaphore[capacity];
        compteur = 1;
    }

    public void put(Message o) {
        try {
            s.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (size == capacity) {
            s.release();
            try {
                s.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        buffer[in] = o;
        mutex[in] = new Semaphore(0, true);

        totmsg++;
        size++;
        print_buffer();
        System.out.println("Producer " + Thread.currentThread().getId() + " produced " + o.quantite + " in " + in);
        int temp = in;
        in = (in + 1) % capacity;
        s.release();
        try {
            mutex[temp].acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Message get() throws InterruptedException {
        try {
            s.acquire();
        } catch (InterruptedException e) {
            throw e;
        }
        while (size == 0) {
            s.release();
            try {
                s.acquire();
            } catch (InterruptedException e) {
                throw e;
            }
        }
        Message o = buffer[out];
        if (o.quantite > compteur) {
            compteur++;
            System.out.println("Consumer " + Thread.currentThread().getId() + " wait in " + out);
            s.release();
            mutex[out].acquire();
        } else {
            compteur = 1;
            size--;
            mutex[out].release(o.quantite);
            print_buffer();
            System.out.println("Consumer " + Thread.currentThread().getId() + " consumed last in " + out);
            out = (out + 1) % capacity;
            s.release();
        }

        return o;

    }

    @Override
    public Message[] get(int k) throws InterruptedException {
        // TODO Auto-generated method stub
        // mutex.acquire();

        Message[] res = new Message[1];
        res[0] = get();
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
