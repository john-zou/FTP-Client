
import java.lang.System;
import java.io.IOException;

//
// This is an implementation of a simplified version of a command 
// line ftp client. The program always takes two arguments
//

public class CSftp {
	static final int MAX_LEN = 255;
	static final int ARG_MIN = 1;
	static final int ARG_MAX = 2;

	public static void main(String[] args) {
		byte cmdString[] = new byte[MAX_LEN];

		// Get command line arguments and connected to FTP
		// If the arguments are invalid or there aren't enough of them
		// then exit.

		if (args.length < ARG_MIN || args.length > ARG_MAX) {
			System.out.print("Usage: cmd ServerAddress [ServerPort (optional, default = 21)]\n");
			if (args.length > ARG_MAX) {
				System.out.println("Too many arguments.");
			}
			return;
		}

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

		RaySocket rs = new RaySocket();
		rs.host = host;
		rs.port = port;

		rs.connect();
		if (!rs.isConnected) {
			System.out.println("Unsuccessful connection");
			return;
		}



		try {
			for (int len = 1; len > 0;) {
				System.out.print("csftp> ");
				len = System.in.read(cmdString);
				if (len <= 0)
					break;
				rs.send(cmdString);
				// Start processing the command here.
				System.out.println("900 Invalid command.");
			}
		} catch (IOException exception) {
			System.err.println("998 Input error while reading commands, terminating.");
		}
	}
}
