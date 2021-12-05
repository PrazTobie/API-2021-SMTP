import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
        Matcher matcher;

        if (victims == null) {
            throw new RuntimeException("No victims specified");
        }

        if (messages == null) {
            throw new RuntimeException("No messages specified");
        }

        if (server == null) {
            throw new RuntimeException("No mail server specified");
        }

        if (server.host == null || server.host.equals("")) {
            throw new RuntimeException("No host specified");
        }

        if (!server.host.equalsIgnoreCase("localhost")) {
            Pattern patternIP = Pattern.compile("(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}");
            matcher = patternIP.matcher(server.host);
            if (!matcher.find()) {
                throw new RuntimeException("Host is not a valid IP adress");
            }
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

        Pattern patternMail = Pattern.compile("^([a-z0-9_.-]+@[\\da-z.-]+\\.[a-z.]{2,6})$", Pattern.CASE_INSENSITIVE);
        for (String victim : victims) {
            matcher = patternMail.matcher(victim);
            if (!matcher.find()) {
                throw new RuntimeException(victim + " is not a valid mail");
            }
        }
    }
}
