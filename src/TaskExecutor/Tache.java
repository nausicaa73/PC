package src.TaskExecutor;

public class Tache {
    public static void main(String[] args) {
        System.out.println("Début tache " + args[0]);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Début tache " + args[0]);
    }

}
