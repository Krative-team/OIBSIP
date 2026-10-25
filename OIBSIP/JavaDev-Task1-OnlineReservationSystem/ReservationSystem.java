public class ReservationSystem {
    public static void main(String[] args) {
        try { Class.forName("org.sqlite.JDBC"); } catch (Exception e) {}
        DatabaseManager.initDB();
        new LoginForm();
    }
}
