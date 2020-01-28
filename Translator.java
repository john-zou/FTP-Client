import java.util.Scanner;

/**
 * Translates CSftp commands to actual FTP commands
 */

public class Translator {
    public static Translation translate(String str) throws TranslationException {
        // String str = new String(input);
        Scanner scanner = new Scanner(str);

        String applicationCommand = scanner.next();
        String arg = "";

        boolean hasArg = scanner.hasNext();
        if (hasArg) {
            arg = scanner.next();
        }

        if (scanner.hasNext()) {
            // Too many arguments
            scanner.close();
            throw new TranslationException("0x002 Incorrect number of arguments.");
        }
        scanner.close();

        switch (applicationCommand) {
        case "user":
            if (!hasArg) {
                throw new TranslationException("0x002 Incorrect number of arguments.");
            }
            return new Translation(arg, Action.USER);

        case "pw":
            return new Translation(arg, Action.PASS);

        case "quit":
            if (hasArg) {
                throw new TranslationException("0x002 Incorrect number of arguments.");
            }
            return new Translation("", Action.QUIT);
        case "get":
            if (!hasArg) {
                throw new TranslationException("0x002 Incorrect number of arguments.");
            }
            return new Translation(arg, Action.RETR);
        case "features":
            if (hasArg) {
                throw new TranslationException("0x002 Incorrect number of arguments.");
            }
            return new Translation("", Action.FEAT);
        case "cd":
            if (!hasArg) {
                throw new TranslationException("0x002 Incorrect number of arguments.");
            }
            return new Translation(arg, Action.CWD);
        case "dir":
            if (hasArg) {
                throw new TranslationException("0x002 Incorrect number of arguments.");
            }
            return new Translation("", Action.LIST);
        default:
            throw new TranslationException("0x001 Invalid command.");
        }
    }
}