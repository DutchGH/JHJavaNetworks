package networks.cw2;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * File Client - Jacob Holland
 * This will be used to deal with viewing folders from the server
 * and downloading them to a local drive
 */

public class FileClient {

    private Scanner socketIn = null;
    private PrintWriter socketOut = null;
    private Scanner keyboardIn = null;

    public FileClient(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream(), true);
            keyboardIn = new Scanner(System.in);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileClient client = new FileClient("127.0.0.1", 4444);
        client.talkToServer();
    }

    private void talkToServer() {
        String message;

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        System.out.printf("What Would You Like To Do?\nLIST?\nDOWNLOAD?\nEXIT\nCommand: ");
        while ((message = keyboardIn.nextLine()) != null) {
            if (message.toLowerCase().contains("list")) {
                socketOut.println("$LIST$");
            } else if (message.toLowerCase().contains("download")) {
                System.out.println("Name The Folder You Want To Download");
                String folder = keyboardIn.nextLine();
                socketOut.println("$DOWNLOAD$");
                socketOut.println(folder);
            } else if (message.toLowerCase().contains("exit")) {
                System.out.println("Have A Good Day!");
            } else {
                System.out.println("INVALID COMMAND. TRY AGAIN");
                System.out.printf("What Would You Like To Do?\nLIST?\nDOWNLOAD?\nEXIT?Command: ");
            }
        }
    }

    public void downloadFolder(String folderName) {

    }

    public void downloadFile(String fileToRecieve) {

    }

    public void createFolder(String folderName) {
        File path = new File("download/" + folderName + "/");
        boolean result = path.exists();
        if (!result){
            result = path.mkdirs();
        }
        else {

        }
    }



//    public class FileReciever {
//        public FileReciever() {
//
//        }
//    }

    private class IncomingReader implements Runnable {
        public void run() {
            String message;
            String folder;
            while ((message = socketIn.nextLine()) != null) {
                if (message.contains("$DOWNLOAD$")) {
                    System.out.println("Connecting To Server");
                    //folder = socketIn.nextLine();
                    if((socketIn.nextLine().contains("$CREATE$"))) {
                        createFolder(socketIn.nextLine());
                    }
                }

                else {
                    System.out.println(message);
                }
            }
        }
    }

}
