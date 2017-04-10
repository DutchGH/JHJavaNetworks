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

    private static FileServer server;
    private ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        server = new FileServer();
        server.runServer();
    }

    public void runServer() throws IOException {
        serverSocket = new ServerSocket(4444);
        Executor service = Executors.newFixedThreadPool(10);

        while (true) {
            Socket client = serverSocket.accept();
            service.execute(new ClientHandler(client));
        }
    }
}
