package bomberman.entities;

import bomberman.drawing.Sprite;
import bomberman.drawing.Tile;
import bomberman.drawing.MapLoader;
import bomberman.ui.MessagePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GameBoard extends JPanel {
    private static GameBoard instance;
    public static final int UPDATE_SPEED = 40;
    public static final int TILE_VERT_COUNT = 11;
    public static final int TILE_HORZ_COUNT = 13;

    private List<GameObject> gameObjects;
    private Map<String, Player> players;

    private GameBoard() {
        gameObjects = new ArrayList<>();
        players = new ArrayList<>();
        setSize(Tile.TILE_LENGTH * TILE_HORZ_COUNT, Tile.TILE_LENGTH * TILE_VERT_COUNT);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainWindow.requestFocusInWindow();
            }
        });
        setPaused(true);
    }

    public void bindToWindow(Component mainWindow) {
        mainWindow.addKeyListener(new KeyAdapter() {
            @Override
            public synchronized void keyPressed(KeyEvent e) {
                keyMap[e.getKeyCode()] = true;
            }

            @Override
            public synchronized void keyReleased(KeyEvent e) {
                keyMap[e.getKeyCode()] = false;
                CKeyListener.forEach(Player::keyReleased);
            }
        });
        setMainWindow(mainWindow);
        setLocation((mainWindow.getWidth() - getWidth()) / 2, (mainWindow.getHeight() - getHeight()) / 2);
    }

    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }

    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    public Collection<Player> getPlayers() {
        return this.players.values();
    }

    public Player getPlayer(String playerName) {
        return players.get(playerName);
    }

    public void setLocalPlayer(String playerName, String characterName) {
        players.clear();
        players.put(playerName, new Player(playerName, characterName));
    }

    public void addPlayer(String name, Player player) {
        players.put(name, player);
    }

    public Component getMainWindow() {
        return this.mainWindow;
    }

    public synchronized void renewAll() {
        getPlayers().clear();
        gameObjects.clear();
        players.clear();
        MessagePanel.lblMessage.setText("");
        Player.resetPlayerCounter();
    }

    public synchronized void paintObjects(Graphics g) {
        getGameObjects().forEach(gameObject -> gameObject.paint(g));
    }

    public synchronized void paintPlayers(Graphics g) {
        getPlayers().forEach(player -> player.paint(g));
    }

    public void paintSurface(Graphics g) throws Exception {
        Sprite surface = MapLoader.getInstance().getWorldRes().getSprite("Grass");
        Tile tile = new Tile(surface, 0, 0);
        for (int i = 0; i < TILE_VERT_COUNT; i++) {
            for (int j = 0; j < TILE_HORZ_COUNT; j++) {
                tile.paint(g);
                tile.setRow(tile.getRow() + Tile.getTileWidth());
            }
            tile.setRow(0);
            tile.setCol(tile.getCol() + Tile.getTileHeight());
        }
    }

    public synchronized void paint(Graphics g) {
        synchronized (this) {
            super.paint(g);
            try {
                if (!isPaused()) {
                    paintSurface(g);
                    paintObjects(g);
                    paintPlayers(g);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            g.setColor(Color.DARK_GRAY);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

    public synchronized boolean colide(GameObject gameObject) {
        return getGameObjects().stream().anyMatch(curGameObject -> {
            if (gameObject == curGameObject) {
                return false;
            }
            if (curGameObject.getName().equals("Bomb")) {
                Bomb b = (Bomb)curGameObject;
                if (b.getOwner() == gameObject) {
                    return false;
                }
            }
            if ((curGameObject.getName().equals("Bonus")) && (curGameObject.colide(gameObject))) {
                Bonus b = (Bonus)curGameObject;
                Player p = (Player) gameObject;
                p.take(b);
                return false;
            }
            return gameObject.colide(curGameObject);
        });
    }

    public void removeGameObject(GameObject gameObject) {
        synchronized (this.gameObjects) {
            this.gameObjects.set(gameObject.getTileY() * TILE_HORZ_COUNT + gameObject.getTileX(), null);
        }
    }

    public void addGameObject(GameObject gameObject) {
        synchronized (this.gameObjects) {
            this.gameObjects.set(gameObject.getTileY() * TILE_HORZ_COUNT + gameObject.getTileX(), gameObject);
        }
    }

    public synchronized GameObject getGameObject(int x, int y) {
        GameObject retGO = null;
        try {
            retGO = getGameObjects().get(y * TILE_HORZ_COUNT + x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retGO;
    }

    public boolean outOfBounds(Tile tile) {
        return (tile.getRow() < 0) || (tile.getCol() < 0) ||
                (tile.getRow() + Tile.getTileWidth() > getSize().getWidth()) ||
                (tile.getCol() + Tile.getTileHeight() > getSize().getHeight());
    }
}