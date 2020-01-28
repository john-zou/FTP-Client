public class PASVResult {
    public boolean isSuccessful;
    public String host;
    public int port;

    public PASVResult(String host, int port) {
        this.isSuccessful = true;
        this.host = host;
        this.port = port;
    }

    public static PASVResult unsuccessful() {
        PASVResult result = new PASVResult("", 0);
        result.isSuccessful = false;
        return result;
    }
}