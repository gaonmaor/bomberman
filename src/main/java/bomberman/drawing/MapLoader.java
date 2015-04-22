package bomberman.drawing;

import bomberman.entities.*;
import bomberman.logic.GameBuilder;
import bomberman.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static bomberman.entities.GameBoard.TILE_HORZ_COUNT;
import static bomberman.entities.GameBoard.TILE_VERT_COUNT;

public class MapLoader {
    private static MapLoader instance = new MapLoader();
    private List<GameObject> gameObjectList;

    private WorldTile worldRes;
    private String world;

    public static MapLoader getInstance() {
        return instance;
    }

    public MapLoader() {
        gameObjectList = new ArrayList<>(TILE_VERT_COUNT * TILE_HORZ_COUNT);
    }

    public WorldTile getWorldRes() {
        return this.worldRes;
    }

    public void setWorldRes(WorldTile worldRes) {
        this.worldRes = worldRes;
    }

    public void loadMap(char[][] map) throws Exception {
        gameObjectList.clear();
        for (int y = 0; y < TILE_VERT_COUNT; ++y) {
            for (int x = 0; x < TILE_HORZ_COUNT; ++x) {
                if ((map[y][x] == 'M') || (map[y][x] == 'S')) {
                    WorldTile worldTile = getWorldRes();
                    GameObject gameObject = new GameObject(worldTile.getSpriteByKey(map[y][x]),
                            x, y, worldTile.getName(map[y][x]));
                    gameObjectList.set(y * TILE_HORZ_COUNT + x, gameObject);
                }
            }
        }
    }

    /** This method shows how to create world resources.
     *
     */
//    public static void createWorldRes() {
//        WorldTile worldTile = new WorldTile("yard.png");
//        worldTile.add(new WorldObject("Grass", 'G', 0, 0, 31, 32));
//        worldTile.add(new WorldObject("Metal", 'M', 31, 0, 31, 32));
//        worldTile.add(new WorldObject("Stone", 'S', 0, 33, 31, 32));
//        try {
//            Utils.getInstance().saveObject(worldTile, "worlds/yard.dat");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    public WorldTile loadWorldRes() {
        WorldTile worldTile = null;
        try {
            worldTile = (WorldTile) Utils.getInstance().loadObject("worlds/" +
                    GameBuilder.getInstance().getSelectedWorld() + ".dat");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return worldTile;
    }

    public void addGameObjectSprites() {
        gameObjectList.forEach(gameObject -> {
            Sprite sprite = null;
            if (gameObject.getName().equals(WorldObject.Types.METAL.getName())) {
                try {
                    sprite = getWorldRes().getSpriteByKey(WorldObject.Types.METAL.getValue());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    sprite = getWorldRes().getSpriteByKey(WorldObject.Types.STONE.getValue());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            gameObject.setSprite(sprite);
        });
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getWorld() {
        return world;
    }
}
