import java.util.ArrayList;

public class Config {
    public ArrayList<String> victims;
    public int numGroups;
    public ArrayList<String> messages;
    public Server server;
    public String auth;

    public static class Server {
        public String host;
        public int port;
    }

    public void verify() {
        if (victims == null) {
            throw new RuntimeException("No victims specified");
        }

        if (messages == null) {
            throw new RuntimeException("No messages specified");
        }

        if (server == null) {
            throw new RuntimeException("No mail server specified");
        }

        if (server.host == null) {
            throw new RuntimeException("No host specified");
        }

        if (server.port == 0) {
            throw new RuntimeException("No port specified");
        }

        if (numGroups < 1) {
            throw new RuntimeException("The number of groups cannot be lower than 1");
        }

        if (victims.size() == 0) {
            throw new RuntimeException("Victims list cannot be empty");
        }

        if (victims.size() / numGroups < 3) {
            throw new RuntimeException("Too many groups");
        }
    }
}
