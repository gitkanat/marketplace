package config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();

    static {
        loadProperties("application.properties");
//        loadProperties("notification.properties");
//        loadProperties("registration.properties");

    }

    private static void loadProperties(String fileName) {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException(fileName + " not found!");
            }
            // Reader с кодировкой UTF-8
            try (Reader reader = new InputStreamReader(input, "UTF-8")) {
                properties.load(reader);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + fileName, e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
