package networks.cw2;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Client Handler - Jacob Holland
 * This class will manage the thread pool via the executor class
 */
public class ClientHandler implements Runnable {

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


    public String listDirectories() {
        File file = new File("src\\serverPublic");
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        return Arrays.toString(directories);
    }

    public void sendFile(String directory) {

    }

    public void run() {
        send("Hi There! What would you like to do?");
        send("Would you like to LIST, DOWNLOAD or EXIT?");
        send("Enter Command Here: ");
        String message;
        while ((message = reader.nextLine()) != null) {
            if (message.toLowerCase().contains("list")) {
                send("Folders Available:" + listDirectories());
            }
            if (message.toLowerCase().contains("download")) {
                send("Download Mode");
                send("Type the Name of The Folder You Want");
            }
            //System.out.println("Client Handler Read: " + message);
            //setChanged();
            //notifyObservers(message);
        }
    }


}
