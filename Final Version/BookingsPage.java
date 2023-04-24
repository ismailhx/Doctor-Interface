import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookingsPage extends JFrame implements ActionListener {
    // Create JLabels for month and year input fields
    private JLabel monthLabel, yearLabel;
    // Create JComboBoxes for month and year selection
    private JComboBox<String> monthComboBox, yearComboBox;
    // Create JButton for submitting the form
    private JButton submitButton;
    // Create JTextArea for displaying booking details
    private JTextArea bookingDetails;
    // Create JTextArea for displaying booking results
    private JTextArea bookingResults;
    // Create string arrau to store qeury results
    private String[] queryResults;
    public Authentication auth;
    static int red = 7;
    static int green = 10;
    static int blue = 45;
    static Color darkblue = new Color(red, green, blue);
    static Color turquoise = new Color(0, 128, 128);

    // Constructor for the DoctorBookingViewGUI class
    public void Bookings(JFrame frame, Authentication auth) {
        this.auth = auth;
        String username = auth.getUsername();
        auth.frame.getContentPane().removeAll();
        auth.frame.getContentPane().setBackground(turquoise);
        // Create the JLabels for month and year
        final JLabel monthLabel;
        final JLabel yearLabel;
        monthLabel = new JLabel("Month: ");
        monthLabel.setForeground(turquoise);
        yearLabel = new JLabel("Year: ");
        yearLabel.setForeground(turquoise);

        // Create the JComboBoxes for month and year
        // Add appropriate values to the month and year dropdown menus
        String[] monthValues = { "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December" };
        JComboBox<String> monthComboBox = new JComboBox<>(monthValues);

        String[] yearValues = { "2023", "2024", "2025", "2026" };

        JComboBox<String> yearComboBox = new JComboBox<>(yearValues);

        monthComboBox.setForeground(darkblue);
        yearComboBox.setForeground(turquoise);
        // Create the submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(turquoise);
        submitButton.setForeground(darkblue);
        submitButton.setFont(new Font("Courier New", Font.BOLD, 30));

        // Create the JTextArea for booking details
        JTextArea bookingDetails = new JTextArea(10, 20);
        bookingDetails.setEditable(false);

        JTextArea bookingResults = new JTextArea(10, 20);
        bookingResults.setForeground(turquoise);
        bookingDetails.setEditable(false);

        // Add an action listener to the button
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String historyInsert = "INSERT INTO history VALUES ('" + username + "', 'Checked booking', '"
                        + dtf.format(now) + "')";

                // Get the selected month and year values from the dropdown menus
                String month = (String) monthComboBox.getSelectedItem();
                String year = (String) yearComboBox.getSelectedItem();

                // Generate booking details and display them in the JTextArea
                String bookingInfo = generateBookingDetails(month, year);
                bookingDetails.setText(bookingInfo);

                // connects to the database
                try (Connection conn = DriverManager.getConnection(
                        "jdbc:postgresql://localhost/hospital", "postgres", "janabap")) {
                    if (conn != null) {
                        // generate the SQL query for selected values of month and year
                        Statement st = conn.createStatement();
                        Statement stmt = conn.createStatement();
                        ResultSet rs = st.executeQuery("SELECT * FROM bookings WHERE booking_month = '" + month
                                + "' AND booking_year = '" + year + "'ORDER BY booking_date");

                        // goes through the result set and prints out to the Jframe
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();

                        while (rs.next()) {

                            // for (int i = 1; i <= columnsNumber; i++) {
                            // if (i > 1)
                            int booking_id = rs.getInt("booking_id");
                            String doctorName = rs.getString("doctorname");
                            int patientId = rs.getInt("patient_id");
                            String patientName = rs.getString("patient_name");
                            int booking_date = rs.getInt("booking_date");
                            String booking_month = rs.getString("booking_month");
                            int booking_year = rs.getInt("booking_year");
                            String booking_time = rs.getString("booking_time");
                            int phone = rs.getInt("phone");

                            bookingResults.append(booking_id + ", " + doctorName + ", " + patientId + ", " + patientName
                                    + ", " + booking_date + ", " + booking_month + ", " + booking_year + ", "
                                    + booking_time + ", " + phone + "\n");
                            // }
                            // bookingDetails.setText("");
                        }
                        // closes the result set and connection
                        rs.close();
                        st.close();
                    } else {
                        return;
                    }

                } catch (Exception x) {
                    x.printStackTrace();
                }

            }
        });

        // Create a JPanel to hold the form elements and the JTextArea
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(2, 2));
        // Add the form elements to the panel

        Font font = new Font("Courier New", Font.PLAIN, 14);
        monthLabel.setFont(font);
        monthLabel.setBackground(darkblue);
        yearLabel.setFont(font);
        yearLabel.setBackground(turquoise);
        monthComboBox.setFont(font);
        monthComboBox.setBackground(turquoise);
        yearComboBox.setFont(font);
        yearComboBox.setBackground(darkblue);
        bookingDetails.setFont(font);
        bookingDetails.setBackground(darkblue);
        bookingResults.setFont(font);
        bookingResults.setBackground(darkblue);

        formPanel.setBackground(darkblue);
        formPanel.add(monthLabel);
        formPanel.add(monthComboBox);
        formPanel.add(yearLabel);
        formPanel.add(yearComboBox);
        // Add the form panel and submit button to the main panel
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(submitButton, BorderLayout.CENTER);
        // Add the JTextArea to the main panel
        mainPanel.add(new JScrollPane(bookingDetails), BorderLayout.SOUTH);
        mainPanel.add(new JScrollPane(bookingResults), BorderLayout.SOUTH);

        // Add the main panel to the window
        frame.add(mainPanel);

        // Set the window size and make it visible
        frame.setSize(670, 670);
        frame.setVisible(true);
    }

    // ActionListener method for handling button clicks
    public void actionPerformed(ActionEvent e) {
    }

    // This method will be used to display our database records in the booking
    // details text area
    private static String generateBookingDetails(String month, String year) {
        // Replace this with your actual code for generating booking details
        return "Booking details for " + month + " " + year;
    }
}
