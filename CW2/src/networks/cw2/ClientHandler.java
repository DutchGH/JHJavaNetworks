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


    public String listDirectories() {
        File file = new File("src/SerserverPublic");
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

    public boolean folderExists(String folder){
        boolean result = new File("src/serverPublic/" + folder).exists();
        return result;
    }


    public void sendFile(String directory) {
        String serverLocation = ("src/serverPublic/" + directory);
        System.out.println(serverLocation);
        try {
            ServerSocket serverSocket = new ServerSocket(FILE_PORT);
            send("$DOWNLOAD$");

//            while (true) {
                Socket socket = serverSocket.accept();
            System.out.println("Client Connected");
                File[] files = new File(serverLocation).listFiles();
            System.out.println(files.length);
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                DataOutputStream dos = new DataOutputStream(bos);

                dos.writeInt(files.length);
                dos.writeUTF(directory);

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
                dos.close();
                serverSocket.close();
            send("Done");
//            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

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
