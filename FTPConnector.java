import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.util.Pair;
import java.util.Arrays;


public class FTPConnector {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String host;

    public boolean isConnected;

    public FTPConnector(String host, int port) {
        this.host = host;
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

//    public void send(Command cmd) {
//        send(cmd, "");
//    }

    public String getReply() {
        String reply = "";
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
        return reply;
    }

    public String getReply(BufferedReader rdr) {
        String reply = "";
        try {
            while (!rdr.ready()) {
                // Wait
            }
            while (rdr.ready()) {
                try {
                    reply = rdr.readLine();
                    System.out.println(reply);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reply;
    }

    public void send(Translation tl) throws IOException {
        String toFTP = "";
        if (tl.action == Action.USER_PASS) {
            toFTP = "USER " + tl.ftpCommand;
            sendFTP(toFTP);
            getReply();
        }
        else if (tl.action == Action.PASS) {
            toFTP = "PASS " + tl.ftpCommand;
            sendFTP(toFTP);
            getReply();
        }
        else if (tl.action == Action.QUIT) {
            FTPExit();
        }
        else if (tl.action == Action.FEAT) {
            toFTP = "FEAT";
            sendFTP(toFTP);
            getReply();
        }
        else if (tl.action == Action.CWD) {
            toFTP = "CWD " + tl.ftpCommand;
            sendFTP(toFTP);
            getReply();
        } else if (tl.action == Action.PASV_LIST) {
            toFTP = "PASV";
            sendFTP(toFTP);
            String reply = getReply();
            Pair<String, Integer> port_ip = parsePASV(reply);

            try {
                Socket dataSocket = new Socket(port_ip.getKey(), port_ip.getValue());
                BufferedReader date_message_reader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                toFTP = "LIST";
                sendFTP(toFTP);
                reply = getReply();
                if (!reply.startsWith("150 ")) {
                    System.out.println("0x3A2 Data transfer connection to " + this.host +
                            " on port " + String.valueOf(port_ip.getValue()) + " failed to open.");

                } else {
                    getReply(date_message_reader);
                    getReply();
                    dataSocket.close();
                }
            } catch (IOException e) {
                System.out.println("0x3A2 Data transfer connection to " + this.host +
                        " on port " + String.valueOf(port_ip.getValue()) + " failed to open.");
            }


        }
    }

    public void FTPExit() {
        String toFTP = "QUIT";
        sendFTP(toFTP);
        getReply();
        try {
            socket.close();
        } catch (IOException e) {

        }
        System.exit(0);
    }

    public void sendFTP(String str) {
        writer.write(str + "\r\n");
        writer.flush();
        System.out.println("--> " + str);
    }

    private Pair<String, Integer> parsePASV(String response) {
        if (!response.startsWith("227 ")) {
            System.out.println("0xFFFF Processing error. PASV failed.");
            FTPExit();
        }
        String bracket = response.substring(response.indexOf("(")+1, response.indexOf(")"));
        String[] bracketNums = bracket.split(",");
        String[] ipNums = Arrays.copyOfRange(bracketNums, 0, 4);
        String ip = String.join(".", ipNums);
        Integer port = Integer.parseInt(bracketNums[4]) * 256 + Integer.parseInt(bracketNums[5]);
        return new Pair<String, Integer>(ip, port);
    }
}