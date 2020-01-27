/**
 * Translates CSftp commands to actual FTP commands
 */

public class Translator {
    private static final int MAX_ARGS = 1;

    public static Translation translate(String str) throws TranslationException {
//        String str = new String(input);
        String[] split = str.split(" ", MAX_ARGS + 2);
        if (split.length == 0) {
            throw new TranslationException();
        }

        if (split.length > MAX_ARGS + 1) {
            throw new TranslationException();
        }

        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].trim();
        }

        String applicationCommand = split[0];
        switch (applicationCommand) {
        case "user":
            if (split.length != 2) {
                throw new TranslationException();
            }
            return new Translation(split[1], Action.USER_PASS);

        case "pw":
            if (split.length > 2) {
                throw new TranslationException();
            }
            else if (split.length == 2) return new Translation(split[1], Action.PASS);
            else return new Translation("", Action.PASS);
        case "quit":
            if (split.length != 1) {
                throw new TranslationException();
            }
            return new Translation("", Action.QUIT);
        case "get":
            if (split.length != 2) {
                throw new TranslationException();
            }

            break;
        case "features":
            if (split.length != 1) {
                throw new TranslationException();
            }
            return new Translation("", Action.FEAT);
        case "cd":
            if (split.length != 2) {
                throw new TranslationException();
            }
            return new Translation(split[1], Action.CWD);
        case "dir":
            if (split.length != 1) {
                throw new TranslationException();
            }
            return new Translation("", Action.PASV_LIST);
        default:
            throw new TranslationException();
        }
        return new Translation("", Action.USER_PASS);
    }
}