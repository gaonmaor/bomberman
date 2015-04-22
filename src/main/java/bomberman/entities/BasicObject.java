package bomberman.entities;

import java.awt.*;

public abstract class BasicObject extends Rectangle {
    private String name;

    public BasicObject() {
    }

    public BasicObject(int x, int y, int width, int height, String name) {
        super(x, y, width, height);
        setName(name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
