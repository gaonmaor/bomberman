package bomberman.entities;

import bomberman.drawing.Sprite;

import java.io.Serializable;

public class Bonus extends GameObject implements Runnable, Serializable {
    public enum Type{
        ADDITION_BOMB (0, "Additional Bomb"),
        ADDITION_PERIMETER (1, "Additional Perimeter");
        private final String name;
        private int value;

        Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public String toString() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }

    public static final int ANIMATED_MODES = 2;
    public static final int STATE_MODES = 2;
    private boolean animated;
    private Type type;

    public Type getType() {
        return type;
    }

    public Bonus(int x, int y, Type type) {
        super(new Sprite("misc/bonus.png", Sprite.Types.BONUS.getModeDividers()), x, y, "Bonus");
        animated = false;
        getSprite().setStateMode(type.getValue());
        // TODO: Move to GameBoard.
        GameBoard.getInstance().addGameObject(this);
    }

    public synchronized void run() {
        while (!this.animated) {
            getSprite().animated();
            try {
                wait(200L);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void remove() {
        this.animated = true;
    }
}