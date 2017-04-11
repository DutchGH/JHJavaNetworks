package networks.cw2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * FileServer - Jacob Holland
 * This will be used to deal with file handling and logging
 * Fromm the server side
 */
public class FileServer implements Observer {

    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> clients = null;

    public FileServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        clients = new ArrayList<>();

        Thread t = new Thread(new ServerLoop());
        t.start();
    }

    public static void main(String[] args) {
        FileServer server = new FileServer(4444);
    }

    public void tellEveryone(String message) {
        Iterator it = clients.iterator();

        while (it.hasNext()) {
            ClientHandler client = (ClientHandler) it.next();
            client.send(message);
        }
        System.out.println(message);

    }

    public void update(Observable client, Object msg) {
        tellEveryone("from server - " + msg);
    }

    public class ServerLoop implements Runnable {
        public void run() {
            try {
                Executor service = Executors.newFixedThreadPool(10);

                while (true) {
                    Socket client = serverSocket.accept();
                    ClientHandler c = new ClientHandler(client);
                    service.execute(c);
                    clients.add(c);
                    c.addObserver(FileServer.this);
                    System.out.println("Connection Recieved From: " + client.getInetAddress());
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

}


