import java.util.ArrayList;
import java.util.Scanner;

// Tests for functionality of FTP client
public class TestDriver {

    public static void main(String[] args) {
        FTPConnector c = new FTPConnector("ftp.ca.debian.org", 21);

        c.sendFTP("USER anonymous");
        c.getReply();
        c.sendFTP("PASS");
        c.getReply();
        c.sendFTP("FEAT");
        c.getReply();
        while (true) {
            c.getReply();
        }
    }
}