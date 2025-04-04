package config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class ConfigDB {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    // Static block to load properties once
    static {
        try (InputStream input = ConfigDB.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find config.properties");
            }
            prop.load(input);
            URL = prop.getProperty("db.url");
            USER = prop.getProperty("db.user");
            PASSWORD = prop.getProperty("db.password");

            Class.forName("org.postgresql.Driver"); // Load the PostgreSQL driver
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }


    public static String getOtpCode(String email) {
        String userIdQuery = "SELECT id FROM users WHERE email = ?";
        String otpQuery = "SELECT code FROM password_reset_token WHERE user_id = ? ORDER BY id DESC LIMIT 1";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement userStatement = connection.prepareStatement(userIdQuery)) {

            // Получаем user_id по email
            userStatement.setString(1, email);
            try (ResultSet userResultSet = userStatement.executeQuery()) {
                if (userResultSet.next()) {
                    int userId = userResultSet.getInt("id");

                    // Получаем последнюю (самую новую) OTP по user_id
                    try (PreparedStatement otpStatement = connection.prepareStatement(otpQuery)) {
                        otpStatement.setInt(1, userId);
                        try (ResultSet otpResultSet = otpStatement.executeQuery()) {
                            if (otpResultSet.next()) {
                                return otpResultSet.getString("code"); // Возвращаем последний OTP
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Code not found"; // Если OTP не найден
    }

}

