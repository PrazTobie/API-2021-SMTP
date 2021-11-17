import java.io.*;
import java.net.*;
import java.util.Base64;
import com.google.gson.*;

public class Main {
    public static void main(String[] args) {
        try {
            Socket client = new Socket("smtp.mailtrap.io", 2525);

            BufferedReader is = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter os = new PrintWriter(client.getOutputStream());

            System.out.println(is.readLine());
            os.println("EHLO example.com");
            os.flush();

            String str = "";
            while(!(str = is.readLine()).startsWith("250 ")) {
                System.out.println(str);
            }
            System.out.println(str);

            os.println("AUTH LOGIN");
            os.flush();

            System.out.println(is.readLine());

            os.println(new String(Base64.getEncoder().encode(args[0].getBytes())));
            os.flush();

            System.out.println(is.readLine());

            os.println(new String(Base64.getEncoder().encode(args[1].getBytes())));
            os.flush();

            System.out.println(is.readLine());



            os.println("MAIL FROM: <from@example.com>\r");
            os.flush();

            System.out.println(is.readLine());

            os.println("RCPT TO: <to@example.com>\r");
            os.flush();

            System.out.println(is.readLine());

            os.println("DATA\r");
            os.flush();

            System.out.println(is.readLine());

            os.println("To: to@example.com\r\n" +
                    "From: from@example.com\r\n" +
                    "Subject: Hello world!\r\n" +
                    "\r\n" +
                    "This is the test message...\r\n" +
                    ".\r");
            os.flush();

            System.out.println(is.readLine());

            os.println("quit\r");
            os.flush();

            System.out.println(is.readLine());
        } catch (Exception ignored) {

        }
    }
}
