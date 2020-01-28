import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class FTPConnector {
    private Socket controlSocket;
    private BufferedReader reader;
    private PrintWriter writer;

    public static final int TIMEOUT_MILLIS = 5000;
    public static final int CONTROL_SOCKET_TIMEOUT_MILLIS = 20000;
    public static final int DATA_CONNECTION_TIMEOUT_MILLIS = 10000;

    private Socket dataSocket;
    private String dataHost;
    private int dataPort;

    public boolean isConnected;

    public FTPConnector(String host, int port) {
        try {
            controlSocket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
            writer = new PrintWriter(controlSocket.getOutputStream(), true);
            String reply = getReply(CONTROL_SOCKET_TIMEOUT_MILLIS);
            isConnected = true;
        } catch (UnknownHostException e) {
            isConnected = false;
        } catch (IOException e) {
            isConnected = false;
        }
    }

    private PASVResult PASV() {
        // TODO

        return PASVResult.unsuccessful();
    }

    private void executeCommand(FTPCommand cmd, String arg) {
        // Perform PASV if required
        if (cmd == FTPCommand.RETR || cmd == FTPCommand.LIST || cmd == FTPCommand.CWD) {
            PASVResult pasvResult = PASV();
        }

    }

    public void execute(Translation translation) {
        switch (translation.result) {
        case SilentlyIgnore:
            // Do nothing
            return;
        case Bad:
            System.out.println("900 Invalid command.");
            return;
        case Good:
            executeCommand(translation.command, translation.argument);
            return;
        }
    }

    private String getReply(int timeoutMillis) {
        String reply = "";
        try {
            long time = System.currentTimeMillis();
            long end = time + timeoutMillis;
            while (System.currentTimeMillis() < end && !reader.ready()) {
                // Wait for response
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

    public String getReply() {
        return getReply(TIMEOUT_MILLIS);
    }

    public String getReply(BufferedReader rdr) {
        String reply = "";
        try {
            long t = System.currentTimeMillis();
            long end = t + TIMEOUT_MILLIS;
            while (System.currentTimeMillis() < end && !rdr.ready()) {

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

    public void writeBufferToFile(BufferedReader rdr, String filename) {
        try {
            int BUFFER_SIZE = 4096;
            char[] buffer = new char[BUFFER_SIZE];

            String reply = "";
            File f = new File(filename);
            FileWriter fr = new FileWriter(f);
            BufferedWriter br = new BufferedWriter(fr);
            try {
                long t = System.currentTimeMillis();
                long end = t + 5000;
                while (System.currentTimeMillis() < end && !rdr.ready()) {

                }
                while (rdr.ready()) {
                    try {
                        if (rdr.read(buffer) != -1)
                            br.write(buffer);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                br.close();
            }
        } catch (IOException e) {
            System.out.println("0x3A7 Data transfer connection I/O error, closing data connection.");
        }

    }

    // public void send(Translation tl) throws IOException {
    // String toFTP = "";
    // if (tl.action == FTPCommand.USER) {
    // toFTP = "USER " + tl.ftpCommand;
    // sendFTP(toFTP);
    // getReply();
    // } else if (tl.action == FTPCommand.PASS) {
    // toFTP = "PASS " + tl.ftpCommand;
    // sendFTP(toFTP);
    // getReply();
    // } else if (tl.action == FTPCommand.QUIT) {
    // FTPExit();
    // } else if (tl.action == FTPCommand.FEAT) {
    // toFTP = "FEAT";
    // sendFTP(toFTP);
    // getReply();
    // } else if (tl.action == FTPCommand.CWD) {
    // toFTP = "CWD " + tl.ftpCommand;
    // sendFTP(toFTP);
    // getReply();
    // } else if (tl.action == FTPCommand.PASV_LIST) {
    // toFTP = "PASV";
    // sendFTP(toFTP);
    // String reply = getReply();
    // Pair<String, Integer> port_ip = parsePASV(reply);

    // try {
    // Socket dataSocket = new Socket(port_ip.getKey(), port_ip.getValue());
    // BufferedReader date_message_reader = new BufferedReader(
    // new InputStreamReader(dataSocket.getInputStream()));
    // toFTP = "LIST";
    // sendFTP(toFTP);
    // reply = getReply();
    // if (!reply.startsWith("150 ")) {
    // System.out.println("0x3A2 Data transfer connection to " + this.host + " on
    // port "
    // + String.valueOf(port_ip.getValue()) + " failed to open.");

    // } else {
    // getReply(date_message_reader);
    // getReply();
    // dataSocket.close();
    // }
    // } catch (IOException e) {
    // System.out.println("0x3A2 Data transfer connection to " + this.host + " on
    // port "
    // + String.valueOf(port_ip.getValue()) + " failed to open.");
    // }

    // } else if (tl.action == FTPCommand.PASV_RETR) {
    // toFTP = "PASV";
    // sendFTP(toFTP);
    // String reply = getReply();
    // Pair<String, Integer> port_ip = parsePASV(reply);

    // try {
    // Socket dataSocket = new Socket(port_ip.getKey(), port_ip.getValue());
    // BufferedReader date_message_reader = new BufferedReader(
    // new InputStreamReader(dataSocket.getInputStream(), "UTF-8"));
    // toFTP = "RETR " + tl.ftpCommand;
    // sendFTP(toFTP);
    // reply = getReply();
    // if (!reply.startsWith("150 ")) {
    // System.out.println("0x3A2 Data transfer connection to " + this.host + " on
    // port "
    // + String.valueOf(port_ip.getValue()) + " failed to open.");

    // } else {
    // writeBufferToFile(date_message_reader, tl.ftpCommand);
    // getReply();
    // dataSocket.close();
    // }
    // } catch (IOException e) {
    // System.out.println("0x3A2 Data transfer connection to " + this.host + " on
    // port "
    // + String.valueOf(port_ip.getValue()) + " failed to open.");
    // }
    // }
    // }

    public void FTPExit() {
        String toFTP = "QUIT";
        sendFTP(toFTP);
        getReply();
        try {
            controlSocket.close();
        } catch (IOException e) {

        }
        System.exit(0);
    }

    public void sendFTP(String str) {
        writer.write(str + "\r\n");
        writer.flush();
        System.out.println("--> " + str);
    }

    // private Pair<String, Integer> parsePASV(String response) {
    // if (!response.startsWith("227 ")) {
    // System.out.println("0xFFFF Processing error. PASV failed.");
    // FTPExit();
    // }
    // String bracket = response.substring(response.indexOf("(") + 1,
    // response.indexOf(")"));
    // String[] bracketNums = bracket.split(",");
    // String[] ipNums = Arrays.copyOfRange(bracketNums, 0, 4);
    // String ip = String.join(".", ipNums);
    // Integer port = Integer.parseInt(bracketNums[4]) * 256 +
    // Integer.parseInt(bracketNums[5]);
    // return new Pair<String, Integer>(ip, port);
    // }
}