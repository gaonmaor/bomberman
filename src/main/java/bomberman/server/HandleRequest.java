package bomberman.server;

import bomberman.utils.Utils;

import java.io.*;
import java.net.Socket;

public class HandleRequest
        implements Runnable {
    private static MainServer mainServer;
    private Socket socket;

    public HandleRequest(Socket socket) {
        setSocket(socket);
        Thread thread = new Thread(this);
        thread.start();
    }

    public static MainServer getMainServer() {
        return mainServer;
    }

    public static void setMainServer(MainServer aMainServer) {
        mainServer = aMainServer;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public synchronized void run() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
            String command = in.readLine();
            if (command.equals("GETSELWORLD")) {
                PrintWriter out = new PrintWriter(new BufferedOutputStream(getSocket().getOutputStream()), true);
                out.println(getMainServer().getSelectedWorld());
            }
            if (command.equals("GETMAP")) {
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(getSocket().getOutputStream()));
                try {
                    oos.writeObject(Utils.getInstance().loadMap("maps/" + mainServer.getSelectedMap() + ".map"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                oos.close();
            }
            if (command.equals("JOIN")) {
                int location = 0;


                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(getSocket().getOutputStream()));
                String name = in.readLine();
                String character = in.readLine();
                int port = Integer.parseInt(in.readLine());
                boolean success = checkPlayer(name, character);
                dos.writeBoolean(success);
                if (success) {
                    location = mainServer.getLocations().pop();
                    dos.writeInt(location);
                    mainServer.sendPlayers(this.socket.getInetAddress().getHostAddress(), port);
                    mainServer.notJoin(name, character, location);
                    PlayerInfo pi = new PlayerInfo(name, character, location, this.socket.getInetAddress().getHostAddress(), port);
                    mainServer.getPlayers().add(pi);
                }
                dos.close();
            }
            if (command.equals("PLAYERCOMMAND")) {
                String cmd = in.readLine();
                String playerName = in.readLine();
                mainServer.notPCommand(cmd, playerName);
            }
            if (command.equals("RANDBONUS")) {
                int x = Integer.parseInt(in.readLine());
                int y = Integer.parseInt(in.readLine());
                mainServer.randBonus(x, y);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            in.close();
            getSocket().close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean checkPlayer(String name, String character) {
        boolean flag = (!mainServer.isFull()) && (!mainServer.hasName(name)) && (!mainServer.hasCharacter(name));

        return flag;
    }
}



