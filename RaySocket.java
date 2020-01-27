import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RaySocket {
    public int port;
    public String host;
    public boolean isConnected = false;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public void connect() {
        try {
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            isConnected = true;
        } catch (IOException e) {
            // Do nothing, as isConnected is false
        }
    }

    public void send(Translation tl) throws IOException {
        if (tl.action == Action.USER_PASS) {
            writer.write("USER " + tl.ftpCommand);
        }

    }

    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }
}