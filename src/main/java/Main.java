import java.io.*;
import java.net.*;
import java.util.Base64;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(new FileReader("config.json"));
        Config config = gson.fromJson(jsonReader, Config.class);


        try {
            Socket client = new Socket(config.server.host, config.server.port);

            BufferedReader is = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter os = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            System.out.println(is.readLine());
            os.write("EHLO example.com\n");
            os.flush();

            String str = "";
            while(str = is.readLine()) {
                System.out.println(str);
            }
            System.out.println(str);

            /*
            os.write("AUTH LOGIN\n");
            os.flush();

            System.out.println(is.readLine());

            os.write(new String(Base64.getEncoder().encode(config.auth.username.getBytes())) + '\n');
            os.flush();

            System.out.println(is.readLine());

            os.write(new String(Base64.getEncoder().encode(config.auth.password.getBytes())) + '\n');
            os.flush();

            System.out.println(is.readLine());
             */



            os.write("MAIL FROM: <from@example.com>\r\n");
            os.flush();

            System.out.println(is.readLine());

            os.write("RCPT TO: <to@example.com>\r\n");
            os.flush();

            System.out.println(is.readLine());

            os.write("DATA\r\n");
            os.flush();

            System.out.println(is.readLine());

            os.write("To: to@example.com\r\n" +
                    "From: from@example.com\r\n" +
                    "Subject: Hello world!\r\n" +
                    "\r\n" +
                    "This is the test message...\r\n" +
                    ".\r\n");
            os.flush();

            System.out.println(is.readLine());

            os.write("quit\r\n");
            os.flush();

            System.out.println(is.readLine());
        } catch (Exception ignored) {

        }
    }
}
