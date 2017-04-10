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


    public void run() {
        String message;
        while ((message = reader.nextLine()) != null) {
            System.out.println("clientHandler read" + message);
            setChanged();
            notifyObservers();
        }
    }


}
