package networks.cw2;

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

        while ((message = keyboardIn.nextLine()) != null) {
            //System.out.println("client typed: " + message);
            socketOut.println(message);
        }
    }

    public void downloadFolder(String folderName) {

    }

    public void downloadFile(String fileToRecieve) {

    }

    private class IncomingReader implements Runnable {
        public void run() {
            String message;
            while ((message = socketIn.nextLine()) != null) {
                System.out.println(message);
                if (message.toLowerCase().contains("download")) {
                    downloadFolder(socketIn.nextLine());
                }
            }
        }
    }

}
