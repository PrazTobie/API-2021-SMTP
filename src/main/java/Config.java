import java.util.ArrayList;

public class Config {
    public ArrayList<String> victims;
    public int numGroups;
    public ArrayList<String> messages;
    public Server server;
    public Auth auth;

    public static class Server {
        public String host;
        public int port;
    }

    public static class Auth {
        public String username;
        public String password;
    }
}
