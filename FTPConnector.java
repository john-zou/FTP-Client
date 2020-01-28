import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.InetSocketAddress;

import java.util.Arrays;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FTPConnector {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String host;

    public boolean isConnected;

    public FTPConnector(String host, int port) {
        this.host = host;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 20000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            getReply();
            isConnected = true;
        } catch (Exception e) {
            System.out.println("0xFFFC Control connection to " + host + " on port " + port + " failed to open.");
        }
    }

    private String getReply() {
        String reply = "";
        try {
            long t = System.currentTimeMillis();
            long end = t + 5000;
            while (System.currentTimeMillis() < end && !reader.ready()) {

            }
            while (reader.ready()) {
                reply = reader.readLine();
                System.out.println("<-- " + reply);
                if (reply.length() > 3 && Character.isDigit(reply.charAt(0)) && reply.charAt(3) == '-')
                    getReply();
            }
        } catch (IOException e) {
            System.out.println("0xFFFD Control connection I/O error, closing control connection.");
            FTPExit();
        } finally {
            return reply;
        }
    }

    private String getReply(BufferedReader rdr) {
        String reply = "";
        try {
            long t = System.currentTimeMillis();
            long end = t + 5000;
            while (System.currentTimeMillis() < end && !rdr.ready()) {

            }
            while (rdr.ready()) {
                reply = rdr.readLine();
                System.out.println(reply);
                if (reply.length() > 3 && Character.isDigit(reply.charAt(0)) && reply.charAt(3) == '-')
                    getReply(rdr);
            }
        } catch (IOException e) {
            System.out.println("0x3A7 Data transfer connection I/O error, closing data connection.");
        } finally {
            return reply;
        }
    }

    private void writeBufferToFile(InputStream rdr, String filename) throws IOException {
        int buffer;
        String reply = "";
        File f = new File(filename);
        FileOutputStream fr = new FileOutputStream(f);

        long t = System.currentTimeMillis();
        long end = t + 5000;
        while (System.currentTimeMillis() < end && rdr.available() == 0) {

        }

        buffer = rdr.read();
        while (buffer != -1) {
            fr.write(buffer);
            buffer = rdr.read();
        }
        fr.close();
    }

    public void send(Translation tl) throws IOException {
        String toFTP = "";
        String reply;
        if (tl.action == Action.USER) {
            toFTP = "USER " + tl.ftpCommand;
            sendFTP(toFTP);
            getReply();
        } else if (tl.action == Action.PASS) {
            toFTP = "PASS " + tl.ftpCommand;
            sendFTP(toFTP);
            getReply();
        } else if (tl.action == Action.QUIT) {
            FTPExit();
        } else if (tl.action == Action.FEAT) {
            toFTP = "FEAT";
            sendFTP(toFTP);
            getReply();
        } else if (tl.action == Action.CWD) {
            toFTP = "CWD " + tl.ftpCommand;
            sendFTP(toFTP);
            getReply();
        } else if (tl.action == Action.LIST) {
            IPPort port_ip = sendPASV();
            try {
                Socket dataSocket = new Socket();
                try {
                    dataSocket.connect(new InetSocketAddress(port_ip.ip, port_ip.port), 10000);
                } catch (Exception e) {
                    System.out.println("0x3A2 Data transfer connection to " + this.host + " on port "
                            + port_ip.port + " failed to open.");
                    return;
                }

                BufferedReader date_message_reader = new BufferedReader(
                        new InputStreamReader(dataSocket.getInputStream()));
                toFTP = "LIST";
                sendFTP(toFTP);
                reply = getReply();
                if (reply.startsWith("150 ")) {
                    getReply(date_message_reader);
                    getReply();
                    dataSocket.close();
                }
            } catch (IOException e) {
                System.out.println("0x3A7 Data transfer connection I/O error, closing data connection.");
            }

        } else if (tl.action == Action.RETR) {
            IPPort port_ip = sendPASV();
            try {
                Socket dataSocket = new Socket();
                try {
                    dataSocket.connect(new InetSocketAddress(port_ip.ip, port_ip.port), 10000);
                } catch (Exception e) {
                    System.out.println("0x3A2 Data transfer connection to " + this.host + " on port "
                            + port_ip.port + " failed to open.");
                    return;
                }
                toFTP = "RETR " + tl.ftpCommand;
                sendFTP(toFTP);
                reply = getReply();
                if (reply.startsWith("150 ")) {
                    writeBufferToFile(dataSocket.getInputStream(), tl.ftpCommand);
                    getReply();
                    dataSocket.close();
                }
            } catch (IOException e) {
                System.out.println("0x3A7 Data transfer connection I/O error, closing data connection.");
            }
        }
    }

    private IPPort sendPASV() {
        String toFTP = "PASV";
        sendFTP(toFTP);
        String reply = getReply();
        return parsePASV(reply);
    }

    private void FTPExit() {
        String toFTP = "QUIT";
        sendFTP(toFTP);
        getReply();
        try {
            socket.close();
        } catch (IOException e) {

        } finally {
            System.exit(0);
        }
    }

    private void sendFTP(String str) {
        try {
            writer.write(str + "\r\n");
            writer.flush();
            System.out.println("--> " + str);
        } catch (IOException e) {
            System.out.println("0xFFFD Control connection I/O error, closing control connection.");
            FTPExit();
        }

    }

    private IPPort parsePASV(String response) {
        if (!response.startsWith("227 ")) {
            System.out.println("0xFFFF Processing error. PASV failed.");
            FTPExit();
        }
        String bracket = response.substring(response.indexOf("(") + 1, response.indexOf(")"));
        String[] bracketNums = bracket.split(",");
        String[] ipNums = Arrays.copyOfRange(bracketNums, 0, 4);
        String ip = String.join(".", ipNums);
        Integer port = Integer.parseInt(bracketNums[4]) * 256 + Integer.parseInt(bracketNums[5]);
        return new IPPort(ip, port);
    }
}