package bomberman.entities;

import bomberman.drawing.Sprite;

public class ControlledObject extends GameObject {
    public ControlledObject(Sprite sprite, int col, int row, String name) {
        super(sprite, col, row, name);
    }

    /** If after 30 multi seconds no button is pressed - return to default position.
     */
    // TODO: Refactor this feature.
//    public void keyReleased() {
//        this.pressed = false;
//        Thread thread = new Thread() {
//            public void run() {
//                synchronized (this) {
//                    try {
//                        wait(30L);
//                    } catch (InterruptedException ex) {
//                        ex.printStackTrace();
//                    }
//                    if (!pressed) {
//                        getSprite().setX(1);
//                        GameBoard.getInstance().getMainWindow().repaint();
//                    }
//                }
//            }
//        };
//        thread.start();
//    }
}
