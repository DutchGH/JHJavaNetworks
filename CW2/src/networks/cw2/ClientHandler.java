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

    public void listFiles(String fol) {
        File folder = new File("src/serverPublic/" + fol);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                try {
                    sendFile("src/serverPublic/fol" + listOfFiles[i].getName());
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            } else if (listOfFiles[i].isDirectory()) {
                send("Directory " + listOfFiles[i].getName());
            }
        }
    }


    public void sendFile(String fileToSend) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servSock = null;
        Socket sock = null;

        try {
            servSock = new ServerSocket(8862);
            while (true) {
                System.out.println("Waiting For Connection");
                try {
                    sock = servSock.accept();
                    System.out.println("Accepted Connection");

                    File myFile = new File(fileToSend);
                    byte[] fileByteArray = new byte[(int) myFile.length()];
                    fis = new FileInputStream(myFile);
                    bis = new BufferedInputStream(fis);

                    bis.read(fileByteArray, 0, fileByteArray.length);
                    os = sock.getOutputStream();
                    //System.out.println("Sending" + fileToSend)
                    os.write(fileByteArray, 0, fileByteArray.length);
                    os.flush();
                    System.out.println("Done");
                } finally {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (sock != null) sock.close();
                }
            }
        } finally {
            if (servSock != null) servSock.close();
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
                //send(folder);
                if (folderExists(folder)) {
                    send("$CREATE$");
                }
                else {
                    send("Folder Does Not Exist On The Server");
                }
            }
            //System.out.println("Client Handler Read: " + message);
            //setChanged();
            //notifyObservers(message);
        }
    }


}
