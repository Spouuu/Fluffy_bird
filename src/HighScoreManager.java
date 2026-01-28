import java.io.*;

public class HighScoreManager {
    private static final String FILE = "highscore.txt";

    public static int load() {
        try {
            File f = new File(FILE);
            if (f.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                return Integer.parseInt(br.readLine());
            }
        } catch (Exception ignored) {}
        return 0;
    }

    public static void save(int score) {
        try (FileWriter w = new FileWriter(FILE)) {
            w.write(String.valueOf(score));
        } catch (IOException ignored) {}
    }
}
