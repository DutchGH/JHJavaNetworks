package networks.cw2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * FileServer - Jacob Holland
 * This will be used to deal with file handling and logging
 * Fromm the server side
 */
public class FileServer {

    private ServerSocket serverSocket;
    //private ArrayList<ClientHandler> clients = null;

    //Create a new server socket for clients to connect to
    public FileServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        //clients = new ArrayList<>();

        Thread t = new Thread(new ServerLoop());
        t.start();
    }

    public static void main(String[] args) {
        FileServer server = new FileServer(4444);
    }

    //When each client connects, assign it a dedicated thread and CH for commands to be sent/received
    public class ServerLoop implements Runnable {
        public void run() {
            try {
                //Allows for a maxiumum of 10 simulatenously connected clients
                Executor service = Executors.newFixedThreadPool(10);

                while (true) {
                    Socket client = serverSocket.accept();
                    ClientHandler c = new ClientHandler(client);
                    service.execute(c);
                    System.out.println("Connection Recieved From: " + client.getInetAddress());
                }


            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }
    }


}


