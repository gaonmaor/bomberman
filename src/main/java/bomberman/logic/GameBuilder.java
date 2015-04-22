package bomberman.logic;

import java.awt.*;

public class GameBuilder {
    private static GameBuilder instance = new GameBuilder();
    private String selectedWorld;
    private Component mainWindow;
    private String IPAddress;

    public static GameBuilder getInstance() {
        return instance;
    }

    private GameBuilder() {
    }

    public String getSelectedWorld() {
        return selectedWorld;
    }
}
