import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class History extends JFrame {
    public Authentication auth;

    public History(Authentication auth) {
        this.auth = auth;
        // GUI here

        PrintHistory(auth);
    }

    private void PrintHistory(Authentication auth) {
        Object date = null;
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/hospital", "postgres", "janabap")) {
            if (conn != null) {
                // generate the SQL query for selected values of month and year
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM history");
                int counter = 0;
                while (rs.next()) {
                    String username = rs.getString("username");
                    String function = rs.getString("function");
                    date = rs.getDate("time");
                    date.toString();
                    System.out.println(username + " " + function + " " + date + " ");
                }
                // if rs does not find anything the counter will be 0 so name doesnt exist
                if (counter == 0) {
                    System.out.println("name does not exist");
                }
                // closes the result set and connection
                rs.close();
                st.close();
            }
        } catch (Exception error) {
            error.printStackTrace();
            System.out.println("Failed to connect to database");
        }
    }
}
