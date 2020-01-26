public class TestDriver {

    public static void main(String[] args) {
        FTPConnector c = new FTPConnector("ftp.cs.ubc.ca", 21);
        c.send(Command.USER, "anonymous");
        c.send(Command.PASS, "ray");
        c.send(Command.QUIT);
        // while (true) {
        // c.getReply();
        // }
    }
}