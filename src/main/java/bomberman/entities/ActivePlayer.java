package bomberman.entities;

import bomberman.logic.GameManager;
import bomberman.server.RequestHandler;

/**
 * The player being control from the keyboard.
 */
public class ActivePlayer extends Player {
    private int btnUp;
    private int btnDown;
    private int btnRight;
    private int btnLeft;
    private int btnFire;
    private boolean pressed;

    public ActivePlayer(String spritename, int x, int y, String name) {
        super(spritename, x, y, name);
        btnUp = 38;
        btnDown = 40;
        btnLeft = 37;
        btnRight = 39;
        btnFire = 32;
    }

    public void checkKeys() {
        GameManager gameManager = GameManager.getInstance();
        if (gameManager.isPressed(btnUp)) {
            RequestHandler.getInstance().moveUp(this);
            pressed = true;
        }
        if (gameManager.isPressed(btnRight)) {
            RequestHandler.getInstance().moveRight(this);
            pressed = true;
        }
        if (gameManager.isPressed(btnDown)) {
            RequestHandler.getInstance().moveDown(this);
            pressed = true;
        }
        if (gameManager.isPressed(btnLeft)) {
            RequestHandler.getInstance().moveLeft(this);
            pressed = true;
        }
        if (gameManager.isPressed(btnFire)) {
            RequestHandler.getInstance().fire(this);
            pressed = true;
        }
    }
}
