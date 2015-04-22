package bomberman.drawing;

import bomberman.drawing.Sprite;

public class MovingSprite extends Sprite {
    public static final int ANIMATED_MODES = 3;
    public static final int STATE_MODES = 4;

    /** The sprite moving direction.
     */
    public enum Directions {
        Up(0),
        Right(1),
        Down(2),
        Left(3);
        private final int value;

        Directions(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static int count() {
            return 4;
        }
    }

    public MovingSprite(String imageName, int x, int y, int tileWidth, int tileHeight) {
        super(imageName, x, y, tileWidth, tileHeight);
    }
}
