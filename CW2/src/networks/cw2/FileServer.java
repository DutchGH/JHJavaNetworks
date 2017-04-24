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

    public class ServerLoop implements Runnable {
        public void run() {
            try {
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


