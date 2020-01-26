/**
 * Translates CSftp commands to actual FTP commands
 */

public class Translator {
    private static final int MAX_ARGS = 1;

    public static Translation translate(byte[] input) throws TranslationException {
        String str = new String(input);
        String[] split = str.split(" ", MAX_ARGS + 2);
        if (split.length == 0) {
            throw new TranslationException();
        }

        if (split.length > MAX_ARGS + 1) {
            throw new TranslationException();
        }

        String applicationCommand = split[0];
        switch (applicationCommand) {
        case "user":
            if (split.length != 2) {
                throw new TranslationException();
            }
            return new Translation(split[1], Action.USER_PASS);

        case "pass":
            if (split.length != 2) {
                throw new TranslationException();
            }

            break;
        case "quit":
            if (split.length != 1) {
                throw new TranslationException();
            }

            break;
        case "get":
            if (split.length != 2) {
                throw new TranslationException();
            }

            break;
        case "features":
            if (split.length != 1) {
                throw new TranslationException();
            }

            break;
        case "cd":
            if (split.length != 2) {
                throw new TranslationException();
            }
            break;
        case "dir":
            if (split.length != 1) {
                throw new TranslationException();
            }
            break;
        default:
            throw new TranslationException();
        }
    }
}