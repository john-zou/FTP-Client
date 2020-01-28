
public class Translation {
    public final TranslationResult result;
    public final FTPCommand command;
    public final String argument;

    public Translation(TranslationResult result) {
        this(result, FTPCommand.NULL, "");
    }

    public Translation(TranslationResult result, FTPCommand command) {
        this(result, command, "");
    }

    public Translation(TranslationResult result, FTPCommand command, String argument) {
        this.result = result;
        this.command = command;
        this.argument = argument;
    }
}