package bomberman.entities;

import bomberman.drawing.MovingSprite;
import bomberman.drawing.Sprite;
import java.io.Serializable;
import java.util.concurrent.Executors;

public class Player extends ControlledObject implements Serializable {
    public enum Status {
        ALIVE ("alive"),
        DEAD ("dead");
        private final String status;

        Status(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
    public static final int FACTOR = 20;
    public static final int SPEED = 5;
    private Status status;
    private int perimeter;
    private int bombCount;
    private int score;
    private String playerName;

    public Player(String imageName, String playerName) {
        this(playerName, 0, 0, imageName);
        this.playerName = playerName;
    }

    public Player(String imageName, int col, int row, String playerName) {
        super(new Sprite(imageName, Sprite.Types.MOVING.getModeDividers()), col, row, playerName);

        status = Status.ALIVE;
        score = 0;
        // TODO: Move into property file.
        bombCount = 1;
        perimeter = 1;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getBombCount() {
        return this.bombCount;
    }

    public Status getStatus() {
        return this.status;
    }

    public int getPerimeter() {
        return this.perimeter;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void updateAction() {
        getSprite().animated();
    }

    public void die() {
        this.status = Status.DEAD;
        // TODO: Move into game manager logic.
//        GameBoard.getInstance().getPlayers().remove(this);
//        GameManager.getInstance().removeCKeyListener(this);
//        GameManager.getInstance().playerDie(this);
    }

    public void live() {
        this.status = Status.ALIVE;
    }

    public void incBombCount() {
        this.bombCount += 1;
    }

    public void decBombCount() {
        this.bombCount -= 1;
    }

    public void moveUp() {
        int origX = getX();
        setX(getX() - SPEED);
        getSprite().setStateMode(MovingSprite.Directions.Up.getValue());
        // When the player is close to exact grid - round the location to the approaching grid.
        if (getX() % TILE_LENGTH >= TILE_LENGTH - FACTOR) {
            setX(getX() + (TILE_LENGTH - getX() % TILE_LENGTH));
        }
        if (getX() % TILE_LENGTH <= FACTOR) {
            setRow(getX() - getX() % TILE_LENGTH);
        }
        // If we have a collision or out of bound - revert to the previews value.
        if ((GameBoard.getInstance().colide(this)) ||
                (GameBoard.getInstance().outOfBounds(this))) {
            setX(origX);
        }
        if (!getSprite().isStateChanged()) {
            updateAction();
        }
    }

    public void moveRight() {
        setRow(getRow() + SPEED);
        boolean changed = getSprite().getY() != 1;
        getSprite().setY(1);
        if (getCol() % TILE_LENGTH >= TILE_LENGTH - FACTOR) {
            setCol(getCol() + (TILE_LENGTH - getCol() % TILE_LENGTH));
        }
        if (getCol() % TILE_LENGTH <= FACTOR) {
            setCol(getCol() - getCol() % TILE_LENGTH);
        }
        if ((GameBoard.getInstance().colide(this)) || (GameBoard.getInstance().outOfBounds(this))) {
            setRow(getRow() - SPEED);
        }
        if (!changed) {
            updateAction();
        } else {
            getSprite().setX(1);
        }
        return changed;
    }

    public void moveDown() {
        setCol(getCol() + SPEED);
        boolean changed = getSprite().getY() != 2;
        getSprite().setY(2);
        if (getRow() % TILE_LENGTH >= TILE_LENGTH - FACTOR) {
            setRow(getRow() + (TILE_LENGTH - getRow() % TILE_LENGTH));
        }
        if (getRow() % TILE_LENGTH <= FACTOR) {
            setRow(getRow() - getRow() % TILE_LENGTH);
        }
        if ((GameBoard.getInstance().colide(this)) || (GameBoard.getInstance().outOfBounds(this))) {
            setCol(getCol() - SPEED);
        }
        if (!changed) {
            updateAction();
        } else {
            getSprite().setX(1);
        }
        return changed;
    }

    public void moveLeft() {
        setRow(getRow() - SPEED);
        boolean changed = getSprite().getY() != 3;
        getSprite().setY(3);
        if (getCol() % TILE_LENGTH >= TILE_LENGTH - FACTOR) {
            setCol(getCol() + (TILE_LENGTH - getCol() % TILE_LENGTH));
        }
        if (getCol() % TILE_LENGTH <= FACTOR) {
            setCol(getCol() - getCol() % TILE_LENGTH);
        }
        if ((GameBoard.getInstance().colide(this)) || (GameBoard.getInstance().outOfBounds(this))) {
            setRow(getRow() + SPEED);
        }
        if (!changed) {
            updateAction();
        } else {
            getSprite().setX(1);
        }
        return changed;
    }

    public void fire() {
        if ((this.bombCount > 0) && (GameBoard.getInstance().getGameObject(getTileX(), getTileY()) == null)) {
            Executors.defaultThreadFactory().newThread(new Bomb(this, getTileX(), getTileY())).start();
        }
    }

    public synchronized void take(Bonus bonus) {
        switch (bonus.getType()) {
            case ADDITION_BOMB:
                ++bombCount;
                break;
            case ADDITION_PERIMETER:
                ++perimeter;
                break;
        }

        // TODO: Move this logic to GameBoard.
        GameBoard.getInstance().removeGameObject(bonus);
        GameBoard.getInstance().getMainWindow().repaint();
    }
}