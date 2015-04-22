package bomberman.entities;

import bomberman.entities.BasicObject;

import java.io.Serializable;

public class WorldObject extends BasicObject implements Serializable {
    public enum Types {
        METAL('M', "Metal"),
        STONE('S', "Stone");

        private final char value;
        private final String name;

        Types(char value, String name) {
            this.value = value;
            this.name = name;
        }

        public char getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    private char code;

    public WorldObject(String name, char code, int x, int y, int width, int height) {
        super(x, y, width, height, name);
        setCode(code);
    }

    public char getCode() {
        return this.code;
    }

    public void setCode(char code) {
        this.code = code;
    }
}