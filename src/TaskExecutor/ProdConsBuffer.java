package src.TaskExecutor;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

import java.util.Iterator;

public class ProdConsBuffer extends Thread {
    Message[] buffer;
    ArrayList<Map.Entry<Consumer, Integer>> consumers = new ArrayList<Map.Entry<Consumer, Integer>>();
    int in = 0;
    int out = 0;
    int size = 0;
    int capacity;
    int totmsg = 0;

    public ProdConsBuffer(int capacity) {
        this.capacity = capacity;
        buffer = new Message[capacity];
        start();
    }

    public synchronized boolean isfree() {
        for (int i = 0; i < consumers.size(); i++) {
            if (!consumers.get(i).getKey().running) {
                return true;
            }
        }
        return false;
    }

    public synchronized void put(Message o) {
        while (size == capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Producer put");
        buffer[in] = o;
        totmsg++;
        in = (in + 1) % capacity;
        size++;
        // print_buffer();

        if (consumers.size() == 0 || !isfree()) {
            Map.Entry<Consumer, Integer> e = new AbstractMap.SimpleEntry<>(new Consumer(this, 1, 100), 0);
            consumers.add(e);
            consumers.get(consumers.size() - 1).getKey().start();
        }

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
        // print_buffer();
        notifyAll();
        return o;

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

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                synchronized (this) {
                    if (consumers.size() == 0) {
                        continue;
                    }
                    Iterator<Map.Entry<Consumer, Integer>> iterator = consumers.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<Consumer, Integer> c = iterator.next();
                        if (!c.getKey().running) {
                            if (c.getValue() == 2) {
                                c.getKey().interrupt();
                                iterator.remove(); // Supprime l'élément en toute sécurité
                            } else {
                                c.setValue(c.getValue() + 1);
                            }
                        }
                    }
                }

            } catch (InterruptedException e) {
                return;
            }
            System.out.println("Nombre de consumers: " + consumers.size());
        }
    }
}
