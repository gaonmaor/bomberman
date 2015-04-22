package bomberman.server;

import bomberman.entities.ControlledObject;

import java.io.*;
import java.net.Socket;

public class RequestHandler {
    public static final int FAILED = -1;
    private String host;
    private boolean conStatus;
    private static RequestHandler instance = new RequestHandler();
    private String ipAddress;

    public String getIPAddress() {
        return this.ipAddress;
    }

    public RequestHandler() {
    }

    public static RequestHandler getInstance() {
        return instance;
    }

    public String getSelectedWorld() {
        String selectedWorld = "yard";
        PrintWriter out;
        BufferedReader bufferedReader;
        Socket socket = null;
        try {
            socket = new Socket(this.host, 4646);
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            out.println("GETSELWORLD");
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            selectedWorld = bufferedReader.readLine();
            bufferedReader.close();
            out.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return selectedWorld;
    }

    public char[][] getMap() {
        char[][] map = null;
        PrintWriter out;
        ObjectInputStream ois;
        Socket socket;
        try {
            socket = new Socket(this.host, 4646);
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            out.println("GETMAP");
            ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            map = (char[][]) ois.readObject();
            ois.close();
            out.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public int join(String name, String character, int port) {
        int result = -1;


        DataInputStream dis = null;
        PrintWriter out = null;
        Socket socket = null;
        boolean success = false;
        try {
            socket = new Socket(this.host, 4646);
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            out.println("JOIN");
            out.println(name);
            out.println(character);
            out.println(port);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            success = dis.readBoolean();
            if (success) {
                result = dis.readInt();
            }
            dis.close();
            out.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public void add(String name, String character, int location, int port) {
        PrintWriter out = null;
        Socket socket = null;
        try {
            socket = new Socket(this.host, port);
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            out.println("ADDPLAYER");
            out.println(name);
            out.println(character);
            out.println(location);
            out.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void moveLeft(ControlledObject player) {
        PrintWriter out;
        Socket socket;
        try {
            socket = new Socket(this.host, 4646);
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            out.println("PLAYERCOMMAND");
            out.println("MOVELEFT");
            out.println(player.getName());
            out.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void moveRight(ControlledObject player) {
        PrintWriter out = null;
        Socket socket = null;
        try {
            socket = new Socket(this.host, 4646);
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            out.println("PLAYERCOMMAND");
            out.println("MOVERIGHT");
            out.println(player.getName());
            out.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void moveUp(ControlledObject player) {
        PrintWriter out = null;
        Socket socket = null;
        try {
            socket = new Socket(this.host, 4646);
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            out.println("PLAYERCOMMAND");
            out.println("MOVEUP");
            out.println(player.getName());
            out.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void moveDown(ControlledObject player) {
        PrintWriter out = null;
        Socket socket = null;
        try {
            socket = new Socket(this.host, 4646);
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            out.println("PLAYERCOMMAND");
            out.println("MOVEDOWN");
            out.println(player.getName());
            out.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void fire(ControlledObject player) {
        PrintWriter out = null;
        Socket socket = null;
        try {
            socket = new Socket(this.host, 4646);
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            out.println("PLAYERCOMMAND");
            out.println("FIRE");
            out.println(player.getName());
            out.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendPCommand(String cmd, String playerName, int port) {
        PrintWriter out;
        Socket socket;
        try {
            socket = new Socket(this.host, port);
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            out.println(cmd);
            out.println(playerName);
            out.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void randBonus(int x, int y) {
        PrintWriter out;
        Socket socket;
        try {
            socket = new Socket(this.host, 4646);
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            out.println("RANDBONUS");
            out.println(x);
            out.println(y);
            out.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void createBonus(int x, int y, String bonusType, int port) {
        PrintWriter out;
        Socket socket;
        try {
            socket = new Socket(this.host, port);
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            out.println("CREATEBONUS");
            out.println(x);
            out.println(y);
            out.println(bonusType);
            out.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}