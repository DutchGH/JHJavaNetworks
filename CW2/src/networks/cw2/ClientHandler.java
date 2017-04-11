package networks.cw2;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Scanner;

/**
 * Client Handler - Jacob Holland
 * This class will manage the thread pool via the executor class
 */
public class ClientHandler extends Observable implements Runnable {

    private Scanner reader = null;
    private PrintWriter writer = null;

    public ClientHandler(Socket client) {
        try {
            reader = new Scanner(client.getInputStream());
            writer = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        writer.println(message);
    }


    public void run() {
        String message;
        while ((message = reader.nextLine()) != null) {
            if (message.contains("LIST")) {
                System.out.println("LIST OF FOLDERS");
                setChanged();
                notifyObservers("LIST OF FOLDERS");
            }
            System.out.println("Client Handler Read: " + message);
            setChanged();
            notifyObservers(message);
        }
    }


}
