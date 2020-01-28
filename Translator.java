import java.util.Scanner;

/**
 * Translates CSftp commands to actual FTP commands
 */

public class Translator {

    public static Translation translate(String str) {
        Scanner scanner = new Scanner(str);

        // Silently ignore empty line
        if (!scanner.hasNext()) {
            scanner.close();
            return new Translation(TranslationResult.SilentlyIgnore);
        }

        // Silently ignore any input that starts with #
        String applicationCommand = scanner.next();
        if (applicationCommand.charAt(0) == '#') {
            scanner.close();
            return new Translation(TranslationResult.SilentlyIgnore);
        }

        String rest = "";
        if (scanner.hasNext()) {
            rest = scanner.nextLine();
        }
        rest = rest.trim();

        FTPCommand ftpCommand;
        switch (applicationCommand) {
        case "user":
            ftpCommand = FTPCommand.USER;
            break;
        case "pw":
            ftpCommand = FTPCommand.PASS;
            break;
        case "quit":
            ftpCommand = FTPCommand.QUIT;
            break;
        case "get":
            ftpCommand = FTPCommand.RETR;
            break;
        case "features":
            ftpCommand = FTPCommand.FEAT;
            break;
        case "cd":
            ftpCommand = FTPCommand.CWD;
            break;
        case "dir":
            ftpCommand = FTPCommand.LIST;
            break;
        default:
            scanner.close();
            return new Translation(TranslationResult.Bad);
        }

        scanner.close();
        return new Translation(TranslationResult.Good, ftpCommand, rest);
    }
}
