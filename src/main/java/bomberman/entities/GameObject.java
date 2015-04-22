package bomberman.entities;

import bomberman.drawing.Sprite;
import bomberman.drawing.Tile;

import java.io.Serializable;

public class GameObject extends Tile implements Serializable {
    private String name;

    public GameObject(Sprite sprite, int col, int row, String name) {
        super(sprite, col, row);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}