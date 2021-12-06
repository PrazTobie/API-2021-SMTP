import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
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
            Socket client = new Socket(config.server.host, config.server.port);

            BufferedReader is = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter os = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));

            boolean shouldQuit = false;
            while(!shouldQuit) {
                String str = is.readLine();
                System.out.println(str);

                if (str.startsWith("220")) {
                    os.write("EHLO " + config.auth + CRLF);
                    os.flush();
                    continue;
                }

                if (str.startsWith("250 ")) {
                    for (int i = 0; i < groups.size(); i++) {
                        Group group = groups.get(i);

                        os.write("MAIL FROM: <" + group.sender + ">" + CRLF);
                        os.flush();

                        str = is.readLine();
                        System.out.println(str);
                        if(!str.startsWith("250")) {
                            System.out.println("Error with group #"+i + " when assigning sender.");
                            continue;
                        }

                        for (String victim: group.victims) {
                            os.write("RCPT TO: <" + victim +  ">" + CRLF);
                            os.flush();
                            str = is.readLine();
                            System.out.println(str);

                            if(!str.startsWith("250")) {
                                System.out.println("Error with group #"+i + " when assigning recipient \"" + victim +"\".");
                                continue;
                            }
                        }

                        os.write("DATA" + CRLF);
                        os.flush();

                        str = is.readLine();
                        System.out.println(str);
                        if(!str.startsWith("354")) {
                            System.out.println("Error with group #"+i + " when trying to send DATA.");
                            os.write("RSET" + CRLF);
                            os.flush();
                            System.out.println(is.readLine());
                            continue;
                        }

                        String msg =
                                "To: " + String.join(",", group.victims) + CRLF +
                                "From: " + group.sender + CRLF +
                                "Subject: Important" + CRLF +
                                "Content-type: text/plain; charset=utf-8" + CRLF +
                                        CRLF + group.message + CRLF +
                                        CRLF + "." + CRLF;

                        os.write(msg);
                        os.flush();

                        str = is.readLine();
                        System.out.println(str);

                        if(!str.startsWith("250")) {
                            System.out.println("Error with group #"+i + " when sending message \"" + group.message + "\".");
                            continue;
                        }
                    }

                    os.write("QUIT" + CRLF);
                    os.flush();
                    shouldQuit = true;
                }
            }
            client.close();
        } catch (Exception ignored) {

        }
    }
}
