import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RaySocket {
    public int port;
    public String host;
    public boolean isConnected;

    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    public void connect() {
        try {
            socket = new Socket(host, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            isConnected = true;
        } catch (IOException e) {
        }
    }

    public void write(byte cmdString[]) throws IOException {
        outputStream.write(cmdString);
        outputStream.flush();
    }

    public String read() {
        if (inputStream.read() != 0) {
            // todo
        }
    }
}