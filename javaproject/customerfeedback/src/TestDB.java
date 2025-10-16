import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("Connected to MySQL!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
