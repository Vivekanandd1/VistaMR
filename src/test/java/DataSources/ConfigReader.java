package DataSources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {

    private static Map<String, String> envMap = new HashMap<>();

    static {
        loadDotEnv();   // Load .env if exists
    }

    private static void loadDotEnv() {
        try (BufferedReader br = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    envMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            // .env not found – ignore (CI environment)
        }
    }

    public static String get(String key) {
        // 1️⃣ First check System Environment Variables (GitHub Secrets / Windows)
        String value = System.getenv(key);

        if (value != null && !value.isEmpty()) {
            return value;
        }

        // 2️⃣ If not found, check .env file (Local fallback)
        return envMap.get(key);
    }
}