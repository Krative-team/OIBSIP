import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:reservation.db";
    private static Map<String, String> trains = new HashMap<>();

    static {
        trains.put("12951", "Mumbai Rajdhani");
        trains.put("12002", "Bhopal Shatabdi");
        trains.put("12627", "Karnataka Express");
        trains.put("12301", "Howrah Rajdhani");
        trains.put("12430", "New Delhi Rajdhani");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initDB() {
        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT)");
            s.execute("CREATE TABLE IF NOT EXISTS reservations (pnr TEXT PRIMARY KEY, name TEXT, train_no TEXT, train_name TEXT, class_type TEXT, doj TEXT, source TEXT, destination TEXT)");
            s.execute("INSERT OR IGNORE INTO users VALUES ('admin','admin123')");
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static boolean validateLogin(String u, String p) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
            ps.setString(1, u); ps.setString(2, p);
            return ps.executeQuery().next();
        } catch (Exception e) { return false; }
    }

    public static String getTrainName(String no) {
        return trains.getOrDefault(no, "");
    }
}
