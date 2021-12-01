import java.io.*;
import java.net.*;
import java.util.ArrayList;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

public class Main {
    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(new FileReader("config.json"));
        Config config = gson.fromJson(jsonReader, Config.class);
        config.verify();
        ArrayList<Group> groups = Group.parseGroups(config);

        try {
            Socket client = new Socket(config.server.host, config.server.port);

            BufferedReader is = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter os = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));


            String str = "";
            boolean shouldQuit = false;
            while(!shouldQuit) {
                str = is.readLine();
                System.out.println(str);

                if (str.startsWith("220")) {
                    os.write("EHLO" + config.auth + "\r\n");
                    os.flush();
                    continue;
                }

                if (str.startsWith("250 ")) {
                    for (Group group : groups) {
                        os.write("MAIL FROM: <" + group.sender + ">\r\n");
                        os.flush();

                        System.out.println(is.readLine());


                        for (String victim: group.victims) {
                            os.write("RCPT TO: <" + victim +  ">\r\n");
                            os.flush();
                            System.out.println(is.readLine());
                        }

                        os.write("DATA\r\n");
                        os.flush();

                        System.out.println(is.readLine());

                        os.write(
                                "To: to@example.com\r\n" +
                                "From: from@example.com\r\n" +
                                "Subject: Hello world!\r\n" +
                                "\r\n" +
                                "\r\n" +
                                ".\r\n");
                        os.flush();
                    }
                    System.out.println(is.readLine());

                    os.write("quit\r\n");
                    os.flush();
                    shouldQuit = true;
                }
            }
        } catch (Exception ignored) {

        }
    }
}
