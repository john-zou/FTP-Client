import java.util.Random;

// Just for fun
// Matches the user input with a command
public class DidYouMean {
    private static final String[] commands = { "user", "pw", "quit", "get", "features", "cd", "dir" };
    private static final String[] smalltalk = { "Perhaps", "Maybe", "I think", "It seems like", "Sorry, but maybe" };

    // Longest common subsequence
    private static int lcs(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 1; i <= n; ++i) {
            for (int j = 1; j <= m; ++j) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[n][m];
    }

    private static String randomSmalltalk() {
        Random r = new Random();
        int index = Math.abs(r.nextInt()) % smalltalk.length;
        return smalltalk[index];
    }

    public static String match(String str) {
        if (str.length() == 0) {
            return "";
        }

        String noRepeats = "" + str.charAt(0);
        for (int i = 1; i < str.length(); ++i) {
            if (str.charAt(i) != str.charAt(i - 1)) {
                noRepeats = noRepeats + str.charAt(i);
            }
        }
        str = noRepeats;

        String matchedCommand = "";
        int maxLCS = 0;

        // Find best match. Tie breaker: if first letter of both is the same
        for (String command : commands) {
            int lcs = lcs(command, str);
            if (lcs > maxLCS) {
                matchedCommand = command;
                maxLCS = lcs;
            } else {
                if (lcs == maxLCS) {
                    if (str.charAt(0) == command.charAt(0)) {
                        matchedCommand = command;
                    }
                }
            }
        }

        // Snarky comment only if maxLCS reaches some threasholds
        // (matches at least floor of half of the command AND two letters)
        if (maxLCS >= matchedCommand.length() / 2 && maxLCS >= 2) {
            return "(" + randomSmalltalk() + " you meant '" + matchedCommand + "'?)\r\n";
        } else {
            return "";
        }
    }
}