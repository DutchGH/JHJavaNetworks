package networks.cw2;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client Handler - Jacob Holland
 * This class will manage the thread pool via the executor class
 */
public class ClientHandler implements Runnable {
    private static int FILE_PORT = 4455;
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
        File file = new File("src/serverPublic");
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
        String serverLocation = "/src/serverPublic/" + directory;
        try {
            ServerSocket serverSocket = new ServerSocket(FILE_PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                File[] files = new File(serverLocation).listFiles();
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

                    int fileByte = 0;
                    while ((fileByte = bis.read()) != -1) {
                        bos.write(fileByte);
                    }

                    bis.close();
                }
                dos.close();
                serverSocket.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public void run() {
        //send("You have Connected.");
        String message;
        while ((message = reader.nextLine()) != null) {
            if (message.contains("$LIST$")) {
                send(listDirectories());
            }
            if (message.contains("$DOWNLOAD$")) {
                String folder = reader.nextLine();
                if (folderExists(folder)) {
                    sendFile(folder);
                    send("$DOWNLOAD$");
                }
                else {
                    send("Folder Does Not Exist On The Server.");
                }
            }
            //System.out.println("Client Handler Read: " + message);
            //setChanged();
            //notifyObservers(message);
        }
    }


}
