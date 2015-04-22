package bomberman.server;

import java.io.IOException;
import java.net.ServerSocket;

public class PlayerServer
        implements Runnable {
    private boolean serverRun;
    private ServerSocket serverSocket;

    public PlayerServer(int port) {
        this.serverRun = true;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        while (this.serverRun) {
            try {
                PServerHandler psh = new PServerHandler(this.serverSocket.accept());
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



