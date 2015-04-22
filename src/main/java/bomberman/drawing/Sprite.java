package bomberman.drawing;

import bomberman.entities.*;
import bomberman.utils.Utils;
import javafx.util.Pair;

import java.awt.*;
import java.io.Serializable;

public class Sprite extends BasicObject implements Serializable {
    public enum Types {
        MOVING(new Pair<>(MovingSprite.ANIMATED_MODES, MovingSprite.STATE_MODES)),
        BOMB(new Pair<>(Bomb.ANIMATED_MODES, Bomb.STATE_MODES)),
        BONUS(new Pair<>(Bonus.ANIMATED_MODES, Bonus.STATE_MODES)),
        EXPLODE(new Pair<>(Explode.ANIMATED_MODES, Explode.STATE_MODES));

        private final Pair<Integer,Integer> modeDividers;
        Types(Pair<Integer,Integer> modeDividers) {
            this.modeDividers = modeDividers;
        }

        public Pair<Integer,Integer> getModeDividers() {
            return modeDividers;
        }

    }
    private Image image;
    private boolean stateChanged;
    private int animatedModes;
    private int stateModes;
    private String imageName;

    public Sprite(String imageName, Pair<Integer,Integer> dividers) {
        this.imageName = imageName;
        image = Utils.getInstance().getImage(imageName);
        x = 0;
        y = 0;
        animatedModes = dividers.getKey();
        stateModes = dividers.getValue();
        width = image.getWidth(null) / animatedModes;
        height = image.getHeight(null) / stateModes;
    }

    public boolean isStateChanged() {
        return stateChanged;
    }

    public Image getImage() {
        return this.image;
    }

    public void setAnimatedMode(int modeNum) {
        x = modeNum * width;
    }

    public String getImageName() {
        return imageName;
    }

    public void setStateMode(int modeNum) {
        stateChanged = false;
        if (y != modeNum * height) {
            y = modeNum * height;
            setAnimatedMode(0);
            stateChanged = true;
        }
    }

    public int getAnimatedMode() {
        return x / width;
    }

    public int getStateMode() {
        return y / height;
    }

    public void animated() {
        setAnimatedMode((getAnimatedMode() + 1) % animatedModes);
    }
}