import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    // Connect to SQLite
    public static Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:booking.db");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 🔁 Initialize all tables
    public static void initializeAllTables() {
        createUserTable();
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            // Movies table
            stmt.execute("CREATE TABLE IF NOT EXISTS movies (" +
                    "movie_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "description TEXT," +
                    "theatre TEXT," +
                    "showtimes TEXT)");

            // Seats table
            stmt.execute("CREATE TABLE IF NOT EXISTS seats (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "movie TEXT," +
                    "showtime TEXT," +
                    "seat TEXT," +
                    "booked_by TEXT," +
                    "FOREIGN KEY (booked_by) REFERENCES users(username))");

            // Bookings table
            stmt.execute("CREATE TABLE IF NOT EXISTS bookings (" +
                    "booking_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT," +
                    "movie TEXT," +
                    "showtime TEXT," +
                    "seats TEXT," +
                    "amount_paid REAL," +
                    "payment_time TEXT," +
                    "FOREIGN KEY (username) REFERENCES users(username))");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 👤 Create users table
    public static void createUserTable() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT NOT NULL)";
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ➕ Insert new user
    public static boolean insertNewUser(String username, String password) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO users (username, password) VALUES (?, ?)")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false; // likely duplicate username
        }
    }

    // ✅ Check login
    public static boolean checkLogin(String username, String password) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE username=? AND password=?")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🎬 Get all movie names (optional future feature)
    public static List<String> getAllMovieNames() {
        List<String> movieList = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM movies")) {
            while (rs.next()) {
                movieList.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movieList;
    }

    // 🪑 Save booked seat
    public static void bookSeat(String movie, String time, String seat, String user) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO seats (movie, showtime, seat, booked_by) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, movie);
            pstmt.setString(2, time);
            pstmt.setString(3, seat);
            pstmt.setString(4, user);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🚫 Get already booked seats
    public static List<String> getOccupiedSeats(String movie, String time) {
        List<String> occupied = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT seat FROM seats WHERE movie = ? AND showtime = ?")) {
            pstmt.setString(1, movie);
            pstmt.setString(2, time);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                occupied.add(rs.getString("seat"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return occupied;
    }

    // 💳 Save payment/booking info
    public static void saveBooking(String username, String movie, String time, List<String> seats, double amount) {
        String seatList = String.join(",", seats);
        String paymentTime = java.time.LocalDateTime.now().toString();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO bookings (username, movie, showtime, seats, amount_paid, payment_time) " +
                             "VALUES (?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, username);
            pstmt.setString(2, movie);
            pstmt.setString(3, time);
            pstmt.setString(4, seatList);
            pstmt.setDouble(5, amount);
            pstmt.setString(6, paymentTime);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

