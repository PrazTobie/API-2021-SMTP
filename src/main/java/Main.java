import java.io.*;
import java.util.ArrayList;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

public class Main {
    static final String CRLF = "\r\n";

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(new FileReader("config.json"));
        Config config = gson.fromJson(jsonReader, Config.class);
        config.verify();
        ArrayList<Group> groups = Group.parseGroups(config);

        try {
            PrankClient client = new PrankClient();
            boolean connected = client.connect(config.server.host, config.server.port, config.auth);

            if(connected) {
                client.prank(groups);
            } else {
                System.out.println("Failed to connect to server");
            }

            client.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
