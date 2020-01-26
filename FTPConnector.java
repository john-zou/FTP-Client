import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class FTPConnector {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public boolean isConnected;

    public FTPConnector(String host, int port) {
        try {
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            getReply();
            isConnected = true;
        } catch (UnknownHostException e) {
            isConnected = false;
        } catch (IOException e) {
            isConnected = false;
        }
    }

    public void send(Command cmd) {
        send(cmd, "");
    }

    public void getReply() {
        String reply;
        try {
            while (!reader.ready()) {
                // Wait
            }
            while (reader.ready()) {
                try {
                    reply = reader.readLine();
                    System.out.println("<-- " + reply);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Command cmd, String arg) {
        String str = cmd.toString() + " " + arg;
        writer.println(str);
        System.out.println("--> " + str);

        getReply();
    }
}