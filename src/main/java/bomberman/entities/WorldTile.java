package bomberman.entities;

import bomberman.drawing.Sprite;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class WorldTile implements Serializable {
    private Map<Character, WorldObject> worldObjectKeyMap;
    private Map<String, WorldObject> worldObjectNameMap;
    private String worldImage;

    public WorldTile(String worldImage) {
        this.worldImage = worldImage;
        worldObjectKeyMap = new HashMap<>();
        worldObjectNameMap = new HashMap<>();
    }

    public void add(WorldObject worldObject) {
        worldObjectKeyMap.put(worldObject.getCode(), worldObject);
        worldObjectNameMap.put(worldObject.getName(), worldObject);
    }

    Sprite getSpriteByName(String string) throws IOException {
        WorldObject worldObject = worldObjectNameMap.get(string);
        return new Sprite(worldObject.getName(), worldObject.getX(), worldObject.getY(),
                worldObject.getWidth(), worldObject.getHeight());
    }

    Sprite getSpriteByKey(char character) throws IOException {
        WorldObject worldObject = worldObjectKeyMap.get(character);
        return new Sprite(worldObject.getName(), worldObject.getX(), worldObject.getY(),
                worldObject.getWidth(), worldObject.getHeight());
    }

    String getName(char character) {
        return worldObjectNameMap.get(character).getName();
    }
}