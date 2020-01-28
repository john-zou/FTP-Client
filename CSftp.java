
import java.lang.System;

//
// This is an implementation of a simplified version of a command 
// line ftp client. The program always takes two arguments
//


public class CSftp
{
    static final int MAX_LEN = 255;
    static final int ARG_CNT = 2;

    public static void main(String [] args)
    {
	byte cmdString[] = new byte[MAX_LEN];

	// Get command line arguments and connected to FTP
	// If the arguments are invalid or there aren't enough of them
        // then exit.

<<<<<<< Updated upstream
	if (args.length != ARG_CNT) {
	    System.out.print("Usage: cmd ServerAddress ServerPort\n");
	    return;
	}

	try {
	    for (int len = 1; len > 0;) {
		System.out.print("csftp> ");
		len = System.in.read(cmdString);
		if (len <= 0) 
		    break;
		// Start processing the command here.
		System.out.println("900 Invalid command.");
	    }
	} catch (IOException exception) {
	    System.err.println("998 Input error while reading commands, terminating.");
=======
		String host = args[0];
		int port;
		if (args.length == 1) {
			// Set port number to the default: 21
			port = 21;
		} else {
			try {
				port = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.out.println("Usage: cmd ServerAddress ServerPort");
				return;
			}
		}

		FTPConnector connector = new FTPConnector(host, port);

		if (!connector.isConnected) {
			System.out.println("Unsuccessful connection.");
			// TODO: Modify this message if needed.
			return;
		}

		Scanner in = new Scanner(System.in);

		try {
			// CLI Loop
			while (true) {
				System.out.print("csftp> ");
				cmdString = in.nextLine();
				Translation translation = Translator.translate(cmdString);
				connector.execute(translation);

				// Just for fun
				if (translation.result == TranslationResult.Bad) {
					System.out.print(DidYouMean.match(cmdString));
				}
			}
			// } catch (IOException exception) {
			// System.err.println("998 Input error while reading commands, terminating.");
		} finally {
			in.close();
		}
>>>>>>> Stashed changes
	}
    }
}
