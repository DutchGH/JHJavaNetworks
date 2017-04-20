package networks.cw2;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * File Client - Jacob Holland
 * This will be used to deal with viewing folders from the server
 * and downloading them to a local drive
 */

public class FileClient {

    private static String HOST_ADDRESS = "127.0.0.1";
    private static int MAIN_PORT = 4444;
    private static int FILE_PORT = 4455;
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
        FileClient client = new FileClient(HOST_ADDRESS, MAIN_PORT);
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


    public void downloadFiles() throws IOException {
        try {

            Socket socket = new Socket(HOST_ADDRESS, FILE_PORT);

            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            DataInputStream dis = new DataInputStream(bis);

            int filesCount = dis.readInt();
            File[] files = new File[filesCount];
            String dirPath = "./" + dis.readUTF();

            for (int i = 0; i < filesCount; i++) {
                long fileLength = dis.readLong();
                String fileName = dis.readUTF();

                files[i] = new File(dirPath + "/" + fileName);

                FileOutputStream fos = new FileOutputStream(files[i]);
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                for (int j = 0; j < fileLength; j++) {
                    bos.write(bis.read());
                }

                bos.close();
            }

            socket.close();
            bis.close();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private class IncomingReader implements Runnable {
        public void run() {
            String message;
            String folder;
            while ((message = socketIn.nextLine()) != null) {
                if (message.contains("$DOWNLOAD$")) {
                    try {
                        downloadFiles();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }

                else {
                    System.out.println(message);
                }
            }
        }
    }

}
