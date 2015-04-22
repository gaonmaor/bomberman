package bomberman.entities;

import bomberman.drawing.Sprite;
import bomberman.server.RequestHandler;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class Explode extends GameObject implements Serializable {
    public static final int CENTER = 0;
    public static final int HORZ = 1;
    public static final int VERT = 2;
    public static final int RIGHT = 3;
    public static final int UP = 4;
    public static final int LEFT = 5;
    public static final int DOWN = 6;
    public static final int ANIMATED_MODES = 4;
    public static final int STATE_MODES = 7;
    private List<GameObject> explodeGroup;
    private int perimeter;
    private int expend;
    private Player owner;

    public Explode(int x, int y, int perimeter, int expend, List<GameObject> explodeGroup, Player owner) {
        super(null, x, y, "Explode");
        boolean lSF = false;
        boolean rSF = false;
        boolean uSF = false;
        boolean dSF = false;
        setSprite(new Sprite("misc/explode.png", Sprite.Types.EXPLODE.getModeDividers()));
        this.explodeGroup = explodeGroup;
        this.owner = owner;
        this.perimeter = perimeter;
        this.expend = expend;
        explodeGroup.add(this);
        GameBoard.getInstance().addGameObject(this);
        GameObject lObj = GameBoard.getInstance().getGameObject(x - 1, y);
        GameObject rObj = GameBoard.getInstance().getGameObject(x + 1, y);
        GameObject uObj = GameBoard.getInstance().getGameObject(x, y - 1);
        GameObject dObj = GameBoard.getInstance().getGameObject(x, y + 1);
        if ((lObj != null) && ((expend == LEFT) || (expend == CENTER))) {
            GameObject gameObject = lObj;
            if (gameObject.getName().equals("Stone")) {
                lSF = true;
                lObj = null;
                randBonus(x - 1, y);
            }
            if (gameObject.getName().equals("Bonus")) {
                lSF = true;
                lObj = null;
            }
            if ((gameObject.getName().equals("Bomb"))) {
                ((Bomb)gameObject).explode(explodeGroup);
            }
        }
        if ((rObj != null) && ((expend == RIGHT) || (expend == CENTER))) {
            GameObject gameObject = rObj;
            if (gameObject.getName().equals("Stone")) {
                rSF = true;
                rObj = null;
                randBonus(x + 1, y);
            }
            if (gameObject.getName().equals("Bonus")) {
                rSF = true;
                rObj = null;
            }
            if ((gameObject.getName().equals("Bomb"))) {
                ((Bomb)gameObject).explode(explodeGroup);
            }
        }
        if ((dObj != null) && ((expend == DOWN) || (expend == CENTER))) {
            GameObject gameObject = dObj;
            if (gameObject.getName().equals("Stone")) {
                dSF = true;
                dObj = null;
                randBonus(x, y + 1);
            }
            if (gameObject.getName().equals("Bonus")) {
                dSF = true;
                dObj = null;
            }
            if ((gameObject.getName().equals("Bomb"))) {
                ((Bomb)gameObject).explode(explodeGroup);
            }
        }
        if ((uObj != null) && ((expend == UP) || (expend == CENTER))) {
            GameObject gameObject = uObj;
            if (gameObject.getName().equals("Stone")) {
                uSF = true;
                uObj = null;
                randBonus(x, y - 1);
            }
            if (gameObject.getName().equals("Bonus")) {
                uSF = true;
                uObj = null;
            }
            if ((gameObject.getName().equals("Bomb"))) {
                ((Bomb)gameObject).explode(explodeGroup);
            }
        }
        if ((lObj == null) && ((expend == LEFT) || (expend == CENTER)) && (x - 1 >= 0)) {
            Explode e = new Explode(getTileX() - 1, getTileY(), lSF ? CENTER : perimeter - 1, 5, explodeGroup, owner);
            if (perimeter == HORZ) {
                e.getSprite().setY(5);
            } else {
                e.getSprite().setY(1);
            }
        }
        if ((rObj == null) && ((expend == RIGHT) || (expend == CENTER))) {
            GameBoard.getInstance();
            if (x + 1 < 13) {
                Explode e = new Explode(getTileX() + 1, getTileY(), rSF ? CENTER : perimeter - 1, 3, explodeGroup, owner);
                if (perimeter == HORZ) {
                    e.getSprite().setY(3);
                } else {
                    e.getSprite().setY(1);
                }
            }
        }
        if ((dObj == null) && ((expend == DOWN) || (expend == CENTER))) {
            GameBoard.getInstance();
            if (y + 1 < 11) {
                Explode e = new Explode(getTileX(), getTileY() + 1, dSF ? CENTER : perimeter - 1, 6, explodeGroup, owner);
                if (perimeter == HORZ) {
                    e.getSprite().setY(6);
                } else {
                    e.getSprite().setY(2);
                }
            }
        }
        if ((uObj == null) && ((expend == UP) || (expend == CENTER)) && (y - 1 >= CENTER)) {
            Explode e = new Explode(getTileX(), getTileY() - 1, uSF ? CENTER : perimeter - 1, 4, explodeGroup, owner);
            if (perimeter == HORZ) {
                e.getSprite().setY(4);
            } else {
                e.getSprite().setY(2);
            }
        }
        GameBoard.getInstance().getPlayers().stream().filter(player -> player.colide(this)).forEach(player -> {
            player.die();
            owner.setScore(owner.getScore() + 200);
        });
    }

    public void paint(Graphics g) {
        g.drawImage(getSprite().getImage(), getRow(), getCol(), getRow() + TILE_LENGTH,
                getCol() + TILE_LENGTH, getSprite().getX() * getSprite().getWidth(),
                getSprite().getY() * getSprite().getHeight() + 1,
                (getSprite().getX() + 1) * getSprite().getWidth(),
                (getSprite().getY() + 1) * getSprite().getHeight(), GameBoard.getInstance());
    }

    public synchronized void randBonus(int x, int y) {
        //if (this.owner.getName().equals(GameBoard.getInstance().getActivePlayer().getName())) {
            RequestHandler.getInstance().randBonus(x, y);
        //}
    }
}