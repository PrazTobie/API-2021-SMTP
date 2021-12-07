package PrankSMTP;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PrankClient {
    static final String CRLF = "\r\n";

    private Socket client;
    private BufferedReader is;
    private BufferedWriter os;
    private String ehloAuth;

    public boolean connect(String host, int port, String ehloAuth) throws IOException {
        try {
            client = new Socket(host, port);

            is = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            os = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));

            this.ehloAuth = ehloAuth;

            String str = is.readLine();
            System.out.println(str);

            return str.startsWith("220");
        } catch (ConnectException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            is.close();
            os.close();
            client.close();
        } catch (Exception ignored) {}
    }

    public boolean prank(List<Group> groups) throws IOException {
        boolean ehlo = EHLO();

        if (ehlo) {
            for (int i = 0; i < groups.size(); i++) {
                Group group = groups.get(i);
                try {
                    prankGroup(group);
                } catch (RuntimeException e) {
                    System.out.println("Warning: failure in group #" + i + ":");
                    System.out.println(e.getMessage());
                }
            }
        }

        return ehlo;
    }

    private void prankGroup(Group group) throws IOException, RuntimeException {
        os.write("MAIL FROM: <" + group.sender + ">" + CRLF);
        os.flush();

        String str = is.readLine();
        System.out.println(str);

        if (!str.startsWith("250")) {
            throw new RuntimeException("Error when assigning sender.");
        }

        for (String victim : group.victims) {
            os.write("RCPT TO: <" + victim + ">" + CRLF);
            os.flush();
            str = is.readLine();
            System.out.println(str);

            if (!str.startsWith("250")) {
                System.out.println("Warning: failed to assign recipient \"" + victim + "\".");
            }
        }

        os.write("DATA" + CRLF);
        os.flush();

        str = is.readLine();
        System.out.println(str);
        if (!str.startsWith("354")) {
            os.write("RSET" + CRLF);
            os.flush();
            System.out.println(is.readLine());

            throw new RuntimeException("Error when trying to send DATA.");
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

        if (!str.startsWith("250")) {
            throw new RuntimeException("Error when sending message \"" + group.message + "\".");
        }
    }

    private boolean EHLO() throws IOException {
        os.write("EHLO " + ehloAuth + CRLF);
        os.flush();

        String str = is.readLine();
        System.out.println(str);
        while (str.startsWith("250-")) {
            str = is.readLine();
            System.out.println(str);
        }

        return str.startsWith("250 ");
    }
}
