package src;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Random;

public class TestProdCons {
    private static int nProd;
    private static int nCons;
    private static int buffersize;
    private static int prodTime;
    private static int consTime;
    private static int minProd;
    private static int maxProd;
    private static int minCons;
    private static int maxCons;
    private static int maxQuantity;
    private static int minQuantity;

    public static void main(String[] args) throws InvalidPropertiesFormatException, IOException {
        Properties properties = new Properties();
        properties.loadFromXML(
                TestProdCons.class.getClassLoader().getResourceAsStream("./src/options.xml"));
        nProd = Integer.parseInt(properties.getProperty("nProd"));
        nCons = Integer.parseInt(properties.getProperty("nCons"));
        buffersize = Integer.parseInt(properties.getProperty("bufSz"));
        prodTime = Integer.parseInt(properties.getProperty("prodTime"));
        consTime = Integer.parseInt(properties.getProperty("consTime"));
        minProd = Integer.parseInt(properties.getProperty("minProd"));
        maxProd = Integer.parseInt(properties.getProperty("maxProd"));
        minCons = Integer.parseInt(properties.getProperty("minCons"));
        maxCons = Integer.parseInt(properties.getProperty("maxCons"));
        maxQuantity = Integer.parseInt(properties.getProperty("maxQuantity"));
        minQuantity = Integer.parseInt(properties.getProperty("minQuantity"));
        if (maxQuantity > nCons) {
            throw new InvalidPropertiesFormatException("maxQuantity must be less than nCons");
        }
        ProdConsBuffer_synchrone buffer = new ProdConsBuffer_synchrone(buffersize);
        Thread[] threads_prod = new Thread[nProd];
        Thread[] threads_cons = new Thread[nCons];
        for (int i = 0; i < nProd; i++) {
            threads_prod[i] = new Producer(buffer, i, minProd, maxProd, prodTime, minQuantity, maxQuantity);
            threads_prod[i].start();
        }
        for (int i = 0; i < nCons; i++) {
            threads_cons[i] = new Consumer(buffer, i, consTime, minCons, maxCons);
            threads_cons[i].start();
        }
        for (int i = 0; i < nProd; i++) {
            try {
                threads_prod[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (buffer.getSize() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < nCons; i++) {
            threads_cons[i].interrupt();
        }
        System.out.println("End of simulation " + buffer.totmsg() + " messages produced");

    }
}
