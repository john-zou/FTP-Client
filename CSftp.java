
import java.lang.System;
import java.io.IOException;

import java.util.Scanner;
//
// This is an implementation of a simplified version of a command 
// line ftp client. The program always takes two arguments
//

public class CSftp {
	static final int MAX_LEN = 255;
	static final int ARG_MIN = 1;
	static final int ARG_MAX = 2;

	public static void main(String[] args) {
		String cmdString = "";

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

		FTPConnector rs = new FTPConnector(host, port);
//		rs.host = host;
//		rs.port = port;

//		rs.connect();
		if (!rs.isConnected) {
			System.out.println("Unsuccessful connection");
			return;
		}

		Scanner in = new Scanner(System.in);

		try {
			for (int len = 1; len > 0;) {
				System.out.print("csftp> ");

				cmdString = in.nextLine();
				if (cmdString.length() <= 0)
					break;

				Translation translationToSend;

				try {
					translationToSend = Translator.translate(cmdString);
				} catch (TranslationException e) {
					System.out.println("900 Invalid command.");
					continue;
				}

				rs.send(translationToSend);
//				String nextLine = rs.readLine();
//				System.out.println(nextLine);
//				while (nextLine != null) {
//					System.out.println(nextLine);
//					nextLine = rs.readLine();
//				}
			}
		} catch (IOException exception) {
			System.err.println("998 Input error while reading commands, terminating.");
		}
	}
}
