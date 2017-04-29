package networks.cw2;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * File Client - Jacob Holland
 * This will be used to deal with viewing folders from the server
 * and downloading them to a local drive
 */

public class FileClient {

    private static String HOST_ADDRESS = "127.0.0.1";
    private static int MAIN_PORT = 4444;
    private static int FILE_PORT = 8845;
    private Socket socket = null;
    private Scanner socketIn = null;
    private PrintWriter socketOut = null;
    private Scanner keyboardIn = null;
    private boolean cliRunning = true;


    /* FileClient
    Creates a new Socket with a specified port number, scanner for input and socket for output
     */
    public FileClient(String host, int port) {
        try {
            socket = new Socket(host, port);
            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream(), true);
            keyboardIn = new Scanner(System.in);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /*Main Method creates client object and initiates link to server to send messages*/
    public static void main(String[] args) {
        FileClient client = new FileClient(HOST_ADDRESS, MAIN_PORT);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                client.shutdown();
            }
        });
        client.talkToServer();
    }


    /* Method will check for system input and will send commands to the server if the user
    Request LIST, DOWNLOAD or EXIT
     */
    private void talkToServer() {
        String message;
        //Start A new thread and ask the user what they want
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        System.out.printf("What Would You Like To Do?\nLIST?\nDOWNLOAD?\nEXIT\nCommand: ");
        //Check the keyboard for input and send a command to the client handler if it is valid
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
                shutdown();
                System.exit(0);
                cliRunning = false;
            } else {
                System.out.println("INVALID COMMAND. TRY AGAIN");
                System.out.printf("What Would You Like To Do?\nLIST?\nDOWNLOAD?\nEXIT\nCommand: ");
            }
        }
    }

    //shutdown() - Close all sockets and exit the program
    public void shutdown() {
        try {
            socketIn.close();
            socketOut.close();
            socket.close();
            cliRunning = false;
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Connect to the file transfer socket. Create a new folder and download them to disk
    public void downloadFiles() throws IOException {
        try {

            Socket socket = new Socket(HOST_ADDRESS, FILE_PORT);

            //Buffered is for file packets, dis is for file info
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            DataInputStream dis = new DataInputStream(bis);

            int filesCount = dis.readInt();
            File[] files = new File[filesCount];
            //check if the file path already exists - if not, create it.
            String dirPath = "clientDownload/" + dis.readUTF();
            File folderVerification = new File(dirPath);
            boolean wasSuccessful = folderVerification.mkdirs();
            if (!wasSuccessful) {
                if (folderVerification.exists()) {
                    System.out.println("Folder Already Exists");
                } else {
                    System.out.println("ERROR CREATING FOLDER");
                }
            } else {
                //Get file name from DIS and write each packet onto disk as it is recieved
                //Once it reaches EOF, move to next file
                for (int i = 0; i < filesCount; i++) {
                    long fileLength = dis.readLong();
                    String fileName = dis.readUTF();

                    String targetFile = dirPath + "/" + fileName;
                    System.out.println(targetFile);
                    files[i] = new File(targetFile);

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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Checks for messages from server, if it receives a valid $DOWNLOAD COMMAND$,
    //intiialise the new socket opening.
    private class IncomingReader implements Runnable {
        public void run() {
            try {
                String message;
                while ((message = socketIn.nextLine()) != null) {
                    if (message.contains("$DOWNLOAD$")) {
                        try {
                            System.out.println("Attempting To Print Files");
                            downloadFiles();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    } else {
                        System.out.println(message);
                    }
                }
            } catch (NoSuchElementException e) {
                //If the server shuts down, close the program.
                System.out.println("Could Not Connect To Server");
                Thread.currentThread().interrupt();//preserve the message
                shutdown();//Stop doing whatever I am doing and terminate
            }
            }
        }
}
