package networks.cw2;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Client Handler - Jacob Holland
 * This class will manage the thread pool via the executor class
 */
public class ClientHandler implements Runnable {
    private static int FILE_PORT = 8845;
    public Log serverLog;
    private Scanner reader = null;
    private String ClientIP;
    private PrintWriter writer = null;

    public ClientHandler(Socket client) {
        try {
            ClientIP = client.getInetAddress().toString();
            reader = new Scanner(client.getInputStream());
            writer = new PrintWriter(client.getOutputStream(), true);
            serverLog = new Log("log.txt");
            serverLog.logger.setLevel(Level.ALL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        writer.println(message);
    }


    /*listDirectories - check ServerPublic folder and check if there are directories,
    add it to an array. Print this array and return it to be used for printing
     */
    public String listDirectories() {
        File file = new File("serverPublic");
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        String folderList = String.format("Folders Available:\n");
        for (int i = 0; i < directories.length; i++) {
            folderList = folderList + String.format("%d. %s\n", i + 1, directories[i]);
        }
        return folderList;
    }

    //Check if folder exists before opening a socket for file transfer
    public boolean folderExists(String folder){
        boolean result = new File("serverPublic/" + folder).exists();
        return result;
    }


    /* Sendfile - create a new file server socket exclusively for file transfer
    Send details of each file through DOS and then send file contents via packets
     */
    public void sendFile(String directory) {
        String serverLocation = ("serverPublic/" + directory);
        System.out.println(serverLocation);
        try {
            ServerSocket serverSocket = new ServerSocket(FILE_PORT);
            send("$DOWNLOAD$");

//            while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client Connected");
            //Create an array of files to be sent and send size to client
            File[] files = new File(serverLocation).listFiles();
            System.out.println(files.length);
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            DataOutputStream dos = new DataOutputStream(bos);

            dos.writeInt(files.length);
            dos.writeUTF(directory);

            //Send name of file to client for creation, and output the file contents
            //To a buffer for easier receptiion
            for (File file : files) {
                long length = file.length();
                dos.writeLong(length);

                String name = file.getName();
                dos.writeUTF(name);

                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);

                int fileByte;
                while ((fileByte = bis.read()) != -1) {
                    bos.write(fileByte);
                }

                bis.close();
            }
            //Close the socket once the transfer is done
            dos.close();
            serverSocket.close();
            send("Done");
//            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    //Reads commands been sent by the client application and executes the method to send back data to the client.
    public void run() {
        //send("You have Connected.");
        String message;
        try {
            while ((message = reader.nextLine()) != null) {
                if (message.contains("$LIST$")) {
                    send(listDirectories());
                    serverLog.logger.info(this.ClientIP + " Requested serverPublic List");
                }
                if (message.contains("$DOWNLOAD$")) {
                    String folder = reader.nextLine();
                    if (folderExists(folder)) {
                        sendFile(folder);
                        serverLog.logger.info(this.ClientIP + " Initiated Download Of: " + folder);
                    } else {
                        send("Folder Does Not Exist On The Server.");
                    }
                }
                //System.out.println("Client Handler Read: " + message);
                //setChanged();
                //notifyObservers(message);
            }
            reader.close();
        } catch (NoSuchElementException e) {
            reader.close();
        }
    }


}
