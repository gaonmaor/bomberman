package bomberman.logic;

import bomberman.drawing.MapLoader;
import bomberman.entities.GameBoard;
import bomberman.entities.Player;
import bomberman.server.RequestHandler;
import bomberman.server.PlayerServer;
import bomberman.ui.MessagePanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class GameManager implements Runnable {
    private boolean paused;
    private boolean[] keyMap;
    private GameTime gameTime;
    private Player activePlayer;
    private List<Player> CKeyListener;
    private static int playerCounter = 0;
    private static GameManager instance = new GameManager();
    public static GameManager getInstance() {
        return instance;
    }

    public GameTime getGameTime() {
        return this.gameTime;
    }

    public Player getActivePlayer() {
        return this.activePlayer;
    }

    private GameManager() {
        CKeyListener = new ArrayList<>();
        keyMap = new boolean[256];
        setPaused(true);
    }

    public void run() {
        long counter = 0L;
        while (!isPaused()) {
            updatePlayers();
            if (counter != getGameTime().getCounter()) {
                getMainWindow().repaint();
                counter = getGameTime().getCounter();
            }
            try {
                wait(UPDATE_SPEED);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void newGame(String playerName, String characterName, int port) {
        ++playerCounter;
        renewAll();
        PlayerServer playerServer = new PlayerServer(port);
        MapLoader.getInstance().setWorld(RequestHandler.getInstance().getSelectedWorld());
        MapLoader.getInstance().loadWorldRes();
        try {
            MapLoader.getInstance().loadMap(RequestHandler.getInstance().getMap());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setPaused(false);
        if (!join(playerName, characterName, port)) {
            setPaused(true);
            JOptionPane.showInputDialog("Can't join the game.");
        }
        gameTime = new GameTime();
        Thread thread = new Thread(this);
        thread.start();
    }

    public boolean join(String playerName, String characterName, int port) {
        ++playerCounter;
        RequestHandler requestHandler = new RequestHandler(this.IPAddress);
        int result = requestHandler.join(playerName, characterName, port);
        if (result != RequestHandler.FAILED) {
            Player player = new Player(characterName, result % TILE_HORZ_COUNT,
                    result / TILE_HORZ_COUNT, playerName);
            activePlayer = player;
            addCKeyListener(player);
        }
        return result != RequestHandler.FAILED;
    }

    public Player getLocalPlayer() {
        return players.values().iterator().next();
    }

    public void addCKeyListener(Player player) {
        this.CKeyListener.add(player);
    }

    public void removeCKeyListener(Player player) {
        this.CKeyListener.remove(player);
    }

    public boolean isPressed(int KeyCode) {
        return keyMap[KeyCode];
    }

    public void updatePlayers() {
        CKeyListener.forEach(Player::checkKeys);
    }

    public void renewAll() {
        CKeyListener.forEach(Player::destroy);
        CKeyListener.clear();
        playerCounter = 0;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void playerDie(Player player) {
        --playerCounter;
        if (playerCounter == 1) {
            MessagePanel.lblMessage.setText("The winner is " + (GameBoard.getInstance().getLocalPlayer().getName()));
            GameBoard.getInstance().removeCKeyListener(GameBoard.getInstance().getLocalPlayer());

            Executors.defaultThreadFactory().newThread(() -> {
                synchronized (this) {
                    try {
                        wait(1000L);
                        GameBoard.getInstance().setPaused(true);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
