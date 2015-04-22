package bomberman.server;

import bomberman.utils.Utils;

import java.util.*;

public class MainServer {
    private List<PlayerInfo> players;
    private Stack<Integer> locations;
    private String selectedMap;
    private String selectedWorld;
    private ServerListener serverListener;

    public MainServer() {
        setSelectedMap("map1");
        setSelectedWorld("yard");
        setPlayers(new ArrayList<>());
        setLocations(new Stack<>());
        try {
            loadLocations();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        HandleRequest.setMainServer(this);
        setServerListener(new ServerListener());
    }

    public List<PlayerInfo> getPlayers() {
        return this.players;
    }

    public void setPlayers(List<PlayerInfo> players) {
        this.players = players;
    }

    public Stack<Integer> getLocations() {
        return this.locations;
    }

    public void setLocations(Stack<Integer> locations) {
        this.locations = locations;
    }

    public String getSelectedMap() {
        return this.selectedMap;
    }

    public void setSelectedMap(String selectedMap) {
        this.selectedMap = selectedMap;
    }

    public String getSelectedWorld() {
        return this.selectedWorld;
    }

    public void setSelectedWorld(String selectedWorld) {
        this.selectedWorld = selectedWorld;
    }

    public ServerListener getServerListener() {
        return this.serverListener;
    }

    public void setServerListener(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    public synchronized boolean isFull() {
        return getLocations().empty();
    }

    public boolean hasName(String name) {
        return getPlayers().stream().anyMatch(playerInfo -> playerInfo.getName().equals(name));
    }

    public boolean hasCharacter(String character) {
        return getPlayers().stream().anyMatch(playerInfo -> playerInfo.getCharacter().equals(character));
    }

    public void loadLocations() throws Exception {
        char[][] map = Utils.getInstance().loadMap("maps/" + getSelectedMap() + ".map");
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 13; x++) {
                if (map[y][x] == 'P') {
                    getLocations().push(y * 13 + x);
                }
            }
        }
    }

    public synchronized void notJoin(String name, String character, int location) {
        getPlayers().forEach(playerInfo -> {
            RequestHandler cReq = new RequestHandler(playerInfo.getIpAdd());
            cReq.add(name, character, location, playerInfo.getPort());
        });
    }

    public void sendPlayers(String host, int port) {
        getPlayers().forEach(playerInfo -> {
            RequestHandler cReq = new RequestHandler(host);
            cReq.add(playerInfo.getName(), playerInfo.getCharacter(), playerInfo.getLocation(), port);
        });
    }

    public void notPCommand(String cmd, String playerName) {
        getPlayers().forEach(playerInfo -> {
            RequestHandler cReq = new RequestHandler(playerInfo.getIpAdd());
            cReq.sendPCommand(cmd, playerName, playerInfo.getPort());
        });
    }

    public void randBonus(int x, int y) {
        String bonusType = null;

        int rand = (int) (Math.random() * 100.0D);
        if ((rand > 10) && (rand < 20)) {
            bonusType = "EXPLODERANGE";
        }
        if ((rand > 70) && (rand < 80)) {
            bonusType = "BOMBCOUNT";
        }
        if (bonusType != null) {
            for (PlayerInfo playerInfo : getPlayers()) {
                RequestHandler cReq = new RequestHandler(playerInfo.getIpAdd());
                cReq.createBonus(x, y, bonusType, playerInfo.getPort());
            }
        }
    }
}