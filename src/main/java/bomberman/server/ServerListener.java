package bomberman.server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerListener implements Runnable {
    private boolean serverRun;
    private ServerSocket serverSocket;

    public ServerListener() {
        this.serverRun = true;
        try {
            this.serverSocket = new ServerSocket(4646);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        HandleRequest handleRequest;
        while (this.serverRun) {
            try {
                handleRequest = new HandleRequest(this.serverSocket.accept());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void close() {
        try {
            this.serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}