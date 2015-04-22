package bomberman.server;

import bomberman.entities.Bonus;
import bomberman.entities.GameBoard;
import bomberman.entities.Player;
import bomberman.utils.Utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PServerHandler implements Runnable {
    private Socket socket;

    public PServerHandler(Socket socket) {
        setSocket(socket);
        Thread t = new Thread(this);
        t.start();
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public synchronized void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String command = in.readLine();
            if (command.equals("ADDPLAYER")) {
                String name = in.readLine();
                String character = in.readLine();
                int location = Integer.parseInt(in.readLine());
                if (!Utils.getInstance().hasImage(character + ".png")) {
                    Utils.getInstance().loadImage("characters/" + character + ".png", character + ".png");
                }
                int x = location % 13;
                int y = location / 13;
                Player player = new Player(character, x, y, name);
                GameBoard.getInstance().getPlayers().add(player);
            }
            if (command.equals("MOVELEFT")) {
                String playerName = in.readLine();
                GameBoard.getInstance().getPlayer(playerName).moveLeft();
            }
            if (command.equals("MOVERIGHT")) {
                String playerName = in.readLine();
                GameBoard.getInstance().getPlayer(playerName).moveRight();
            }
            if (command.equals("MOVEUP")) {
                String playerName = in.readLine();
                GameBoard.getInstance().getPlayer(playerName).moveUp();
            }
            if (command.equals("MOVEDOWN")) {
                String playerName = in.readLine();
                GameBoard.getInstance().getPlayer(playerName).moveDown();
            }
            if (command.equals("FIRE")) {
                String playerName = in.readLine();
                GameBoard.getInstance().getPlayer(playerName).fire();
            }
            if (command.equals("CREATEBONUS")) {
                Bonus b = null;
                int x = Integer.parseInt(in.readLine());
                int y = Integer.parseInt(in.readLine());
                String bonusType = in.readLine();
                if (bonusType.equals("EXPLODERANGE")) {
                    Thread t = null;
                    try {
                        wait(700L);
                        b = new Bonus(x, y, 0);
                        t = new Thread(b);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    synchronized (GameBoard.getInstance().getGameObjects()) {
                        GameBoard.getInstance().addGameObject(b);
                    }
                    t.start();
                }
                if (bonusType.equals("BOMBCOUNT")) {
                    Thread t = null;
                    try {
                        wait(700L);
                        b = new Bonus(x, y, 1);
                        t = new Thread(b);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    synchronized (GameBoard.getInstance().getGameObjects()) {
                        GameBoard.getInstance().addGameObject(b);
                    }
                    t.start();
                }
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}