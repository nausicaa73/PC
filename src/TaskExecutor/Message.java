package src.TaskExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

;

public class Message implements Runnable {
    String programme;

    public Message(String programme) {
        this.programme = programme;
    }

    public String toString() {
        return "";
    }

    @Override
    public void run() {
        try {
            // Commande pour exécuter un programme Java (par exemple HelloWorld)
            ProcessBuilder builder = new ProcessBuilder(
                    "java", "-cp", "./", programme, "HelloWorld");

            // Redirige la sortie standard et les erreurs
            builder.redirectErrorStream(true);

            // Démarre le processus
            Process process = builder.start();

            // Lit la sortie du programme exécuté
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            // Attend la fin de l'exécution
            int exitCode = process.waitFor();
            System.out.println("Programme terminé avec le code : " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}