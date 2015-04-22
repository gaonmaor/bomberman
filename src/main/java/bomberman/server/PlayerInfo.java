package bomberman.server;

public class PlayerInfo {
    private String name;
    private String character;
    private int location;
    private int port;
    private String ipAdd;

    public PlayerInfo(String name, String character, int location, String ipAdd, int port) {
        setName(name);
        setCharacter(character);
        setLocation(location);
        setIpAdd(ipAdd);
        setPort(port);
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return this.character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public int getLocation() {
        return this.location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getIpAdd() {
        return this.ipAdd;
    }

    public void setIpAdd(String ipAdd) {
        this.ipAdd = ipAdd;
    }
}



