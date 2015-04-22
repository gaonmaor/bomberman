package bomberman.entities;

import bomberman.drawing.Sprite;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Bomb extends GameObject implements Runnable {
    public static final int ANIMATED_MODES = 3;
    public static final int STATE_MODES = 1;
    private boolean exploded;
    private Player owner;

    public Bomb(Player owner, int x, int y) {
        super(null, x, y, "Bomb");
        owner.decBombCount();
        this.exploded = false;
        Sprite sprite = new Sprite("misc/bomb.png", Sprite.Types.BOMB.getModeDividers());
        setSprite(sprite);
        setOwner(owner);
        GameBoard.getInstance().getGameObjects().set(getTileY() * GameBoard.TILE_HORZ_COUNT + getTileX(), this);
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public void run() {
        synchronized (this) {
            for (int i = 0; i < 30; ++i) {
                updateAction();
                try {
                    wait(200L);
                } catch (InterruptedException ignored) {
                }
            }
        }
        if ((!this.exploded) && (!getOwner().isDestroyed())) {
            explode();
        }
    }

    public synchronized void explode(List<GameObject> explodeGroup) {
        this.exploded = true;
        this.owner.incBombCount();
        synchronized (this) {
            GameBoard.getInstance().removeGameObject(this);
            Explode explode = new Explode(getTileX(), getTileY(), this.owner.getPerimeter(), 0, explodeGroup, this.owner);
            //explodeGroup.add(explode);
            // TODO: add the explosion?
        }
    }

    public void explode() {
        this.exploded = true;
        this.owner.incBombCount();
        synchronized (this) {
            GameBoard.getInstance().removeGameObject(this);
            List<Explode> explodeGroup = new ArrayList<>();
            //explode = new Explode(getTileX(), getTileY(), this.owner.getPerimeter(), 0, explodeGroup, this.owner);
            for (int i = 0; i < 4; i++) {
                explodeGroup.forEach(explode -> explode.getSprite().animated());
                try {
                    wait(100L);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            explodeGroup.forEach(explode -> GameBoard.getInstance().removeGameObject(explode));
        }
    }

    public void paint(Graphics g) {
        if (getSprite() != null) {
            g.drawImage(getSprite().getImage(), getRow(), getCol(),
                    getRow() + TILE_LENGTH, getCol() + TILE_LENGTH,
                    getSprite().getX() * getSprite().getWidth(), 0,
                    (getSprite().getX() + 1) * getSprite().getWidth(),
                    getSprite().getHeight(), GameBoard.getInstance());
        } else {
            super.paint(g);
        }
    }

    public void updateAction() {
        getSprite().setX((getSprite().getX() + 1) % 3);
    }
}