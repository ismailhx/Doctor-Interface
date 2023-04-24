import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ViewPatientDetails extends JFrame {
    private JPanel searchPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton viewAllButton;
    private JTextArea resultsArea;
    public Authentication auth;
    static int red = 7;
    static int green = 10;
    static int blue = 45;
    static Color darkblue = new Color(red, green, blue);
    static Color turquoise = new Color(0, 128, 128);

    public ViewPatientDetails(Authentication auth) {
        this.auth = auth;
        iniComponents(auth);
    }

    public void iniComponents(Authentication authentication) {
        auth.frame.getContentPane().removeAll();

        // Set up the search panel

        searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchField.setBackground(darkblue);
        searchField.setForeground(turquoise);
        searchPanel.setBackground(turquoise);
        searchButton = new JButton("Search");
        viewAllButton = new JButton("View All Patients");
        searchButton.setBackground(darkblue);
        searchButton.setForeground(turquoise);
        viewAllButton.setBackground(darkblue);
        viewAllButton.setForeground(turquoise);
        searchButton.addActionListener(new SearchButtonListener());
        viewAllButton.addActionListener(new ViewAllButtonListener());
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(viewAllButton);

        // Set up the results area
        resultsArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        resultsArea.setBackground(darkblue);
        resultsArea.setForeground(turquoise);
        scrollPane.setBackground(turquoise);
        Font courierNewFont = new Font("Courier New", Font.PLAIN, 12);
        searchField.setFont(courierNewFont);
        searchButton.setFont(courierNewFont);
        viewAllButton.setFont(courierNewFont);
        resultsArea.setFont(courierNewFont);

        // Add the components to the frame
        auth.frame.getContentPane().add(searchPanel, BorderLayout.NORTH);
        auth.frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Set the frame properties
        auth.frame.setTitle("All the Patient's Details");

    }

    private class SearchButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String username = auth.getUsername();
            String historyInsert = "INSERT INTO history VALUES ('" + username + "', 'Searched for patient', '"
                    + dtf.format(now) + "')";
            String searchText = searchField.getText();
            resultsArea.setText(" ");
            searchText = searchText.toLowerCase();
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost/hospital", "postgres", "janabap")) {
                if (conn != null) {
                    // generate the SQL query for selected values of month and year
                    Statement st = conn.createStatement();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM patient WHERE first_name = '" + searchText + "'");
                    resultsArea.setText(" ");
                    int counter = 0;
                    while (rs.next()) {
                        String doctorname = rs.getString("doctorname");
                        int patient_id = rs.getInt("patient_id");
                        String first_name = rs.getString("first_name");
                        String surname = rs.getString("surname");
                        int age = rs.getInt("age");
                        String address = rs.getString("address");
                        int telephone = rs.getInt("telephone");
                        resultsArea.append(doctorname + ", " + patient_id + ", " + first_name +
                                ", " + surname + ", " + age + ", " + address + ", " +
                                telephone + "\n");
                        counter++;
                    }
                    // if rs does not find anything the counter will be 0 so name doesnt exist
                    if (counter == 0) {
                        System.out.println("name does not exist");
                    } else {
                        stmt.executeUpdate(historyInsert);
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

    private class ViewAllButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String username = auth.getUsername();
            String historyInsert = "INSERT INTO history VALUES ('" + username + "', 'Viewed all patients', '"
                    + dtf.format(now) + "')";
            resultsArea.setText(" ");
            resultsArea.setText("Here are the details of all the patients: \n");
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost/hospital", "postgres", "janabap")) {
                if (conn != null) {
                    // generate the SQL query for selected values of month and year
                    Statement st = conn.createStatement();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM patient");
                    int counter = 0;
                    while (rs.next()) {
                        String doctorname = rs.getString("doctorname");
                        int patient_id = rs.getInt("patient_id");
                        String first_name = rs.getString("first_name");
                        String surname = rs.getString("surname");
                        int age = rs.getInt("age");
                        String address = rs.getString("address");
                        int telephone = rs.getInt("telephone");
                        resultsArea.append(doctorname + ", " + patient_id + ", " + first_name +
                                ", " + surname + ", " + age + ", " + address + ", " +
                                telephone + "\n");
                        counter++;
                    }
                    // if rs does not find anything the counter will be 0 so name doesnt exist
                    if (counter == 0) {
                        System.out.println("name does not exist");
                    } else {
                        stmt.executeUpdate(historyInsert);
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
}
