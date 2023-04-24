import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PatientDetails extends JFrame implements ActionListener {
    // GUI components
    private JTextField searchField, diagnosisField, allergiesField, treatmentField, changeDocFied;
    private static JButton searchButton;
    private JButton editButton;
    private static JButton changeDocButton;
    private JButton cancelButton;
    private static JButton submitButton;
    private JPanel editPanel, docSwitchPanel;
    public Authentication auth;
    static int red = 7;
    static int green = 10;
    static int blue = 45;
    static Color darkblue = new Color(red, green, blue);
    static Color turquoise = new Color(0, 128, 128);
    private JTextArea textArea = new JTextArea();

    private static String patientName = "";

    public PatientDetails(Authentication auth) {
        // Remove all components from the main frame
        auth.frame.getContentPane().removeAll();

        // Set up the GUI
        auth.frame.setTitle("Your Patients Details");
        setLayout(new BorderLayout());

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBackground(turquoise);
        searchField = new JTextField(20);
        searchField.setBackground(turquoise);
        searchField.setForeground(darkblue);

        searchButton = new JButton("View own patients");
        searchButton.setBackground(darkblue);
        searchButton.setForeground(turquoise);
        searchButton.addActionListener(this);
        editButton = new JButton("Edit");
        editButton.setBackground(darkblue);
        editButton.setForeground(turquoise);
        editButton.addActionListener(this);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(editButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.RED);
        searchPanel.add(cancelButton);

        // Doctor switch panel ------ IM NEW -------
        docSwitchPanel = new JPanel(new GridLayout(4, 2));
        docSwitchPanel.setVisible(false);
        docSwitchPanel.setBackground(turquoise);

        docSwitchPanel.add(new JLabel("To swap out the patient entered above, please enter a doctors name here:"));
        changeDocFied = new JTextField();
        changeDocFied.setBackground(darkblue);
        docSwitchPanel.add(changeDocFied);
        changeDocButton = new JButton("Submit");
        changeDocButton.setBackground(darkblue);
        changeDocButton.addActionListener(this);
        docSwitchPanel.add(changeDocButton); // hopefully can put it in better location one day

        // Edit panel
        editPanel = new JPanel(new GridLayout(4, 2));
        editPanel.setVisible(false);
        editPanel.setBackground(turquoise);

        // Add labels and text fields to the edit panel
        editPanel.add(new JLabel(" Diagnosis:"));
        diagnosisField = new JTextField();
        diagnosisField.setBackground(darkblue);
        diagnosisField.setPreferredSize(new Dimension(70, 80));
        editPanel.add(diagnosisField);
        editPanel.add(new JLabel(" Allergies:"));
        allergiesField = new JTextField();
        allergiesField.setBackground(darkblue);
        editPanel.add(allergiesField);
        editPanel.add(new JLabel(" Treatment:"));
        treatmentField = new JTextField();
        treatmentField.setBackground(darkblue);
        editPanel.add(treatmentField);
        submitButton = new JButton(" Submit");
        submitButton.setBackground(darkblue);
        submitButton.setForeground(turquoise);
        submitButton.addActionListener(this);
        editPanel.add(submitButton);

        /// Set font and background color for all components
        Font courierFont = new Font("Courier", Font.PLAIN, 12);
        searchField.setFont(courierFont);
        searchButton.setFont(courierFont);
        editButton.setFont(courierFont);
        cancelButton.setFont(courierFont);
        submitButton.setFont(courierFont);
        diagnosisField.setForeground(turquoise);
        diagnosisField.setFont(courierFont);
        allergiesField.setForeground(turquoise);
        allergiesField.setFont(courierFont);
        treatmentField.setFont(courierFont);
        treatmentField.setForeground(turquoise);

        JPanel scrollPanel = new JPanel(new BorderLayout());
        textArea.setBackground(darkblue);
        textArea.setFont(courierFont);
        textArea.setForeground(turquoise);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPanel.add(scrollPane, BorderLayout.CENTER);

        Component[] components = editPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                ((JLabel) component).setBackground(darkblue);
                ((JLabel) component).setFont(courierFont);
            }
        }

        components = docSwitchPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                ((JLabel) component).setBackground(darkblue);
                ((JLabel) component).setFont(courierFont);
            }
        }

        // Add the search and edit panels to the main frame
        auth.frame.getContentPane().add(searchPanel, BorderLayout.NORTH);
        auth.frame.getContentPane().add(scrollPanel, BorderLayout.NORTH);
        auth.frame.getContentPane().add(editPanel, BorderLayout.CENTER);
        auth.frame.getContentPane().add(docSwitchPanel, BorderLayout.CENTER);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            // Perform a search
            String query = searchField.getText();
            viewPatientDetails(query);

        } else if (e.getSource() == editButton) {
            // Show the edit panel
            editPanel.setVisible(true);
        } else if (e.getSource() == submitButton) {
            // Save the edited patient information
            String diagnosis = diagnosisField.getText();
            String allergies = allergiesField.getText();
            String treatments = treatmentField.getText();
            JOptionPane.showMessageDialog(this, "Patient information saved:\nDiagnosis: "
                    + diagnosis + "\nAllergies: " + allergies + "\nTreatment: " + treatments
                    + "\nBooking recieved by patient");
            editPatientDetails(searchField.getText(), diagnosis, allergies, treatments);
            // Hide the edit panel
            editPanel.setVisible(false);
        } else if (e.getSource() == cancelButton) {
            editPanel.setVisible(false);
        } else if (e.getSource() == changeDocButton) {
            updateDoctor(changeDocFied.getText(), patientName);
        }
    }

    private void viewPatientDetails(String fullname) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String username = auth.getUsername();
        String historyInsert = "INSERT INTO history VALUES ('" + username + "', 'Viewed own patient information', '"
                + dtf.format(now) + "')";
        // updates a table via sql

        if (!fullname.contains(" ")) {
            textArea.setText("Invalid fullname entered in text area");
            System.out.println("Invalid fullname entered in text area");
            return;
        }
        textArea.setText("You searched for: " + fullname);
        System.out.println(fullname);

        String firstname = fullname.split(" ")[0];
        String secondname = fullname.split(" ")[1];

        System.out.println(firstname);
        System.out.println(secondname);

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/hospital", "postgres", "janabap")) {
            if (conn != null) {
                // generate the SQL query for selected values of month and year
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM patient WHERE first_name = '" + firstname
                        + "' AND surname = '" + secondname + "'");

                int counter = 0;
                while (rs.next()) {

                    int patient_id = rs.getInt("patient_id");
                    String doctorname = rs.getString("doctorname");
                    String first_name = rs.getString("first_name");
                    String surname = rs.getString("surname");
                    int age = rs.getInt("age");
                    String address = rs.getString("address");
                    int telephone = rs.getInt("telephone");

                    textArea.append(
                            doctorname + "," + patient_id + "," + first_name + "," + surname + "," + age + "," + address
                                    + "," + telephone);
                    counter++;
                }
                // if rs does not find anything the counter will be 0 so name doesnt exist
                if (counter == 0) {
                    System.out.println("name does not exist");
                } else {
                    st.executeUpdate(historyInsert);
                    patientName = fullname;
                    docSwitchPanel.setVisible(true);
                }
                // closes the result set and connection
                rs.close();
                st.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database");
        }

    }

    private void editPatientDetails(String fullname, String diagnosis, String allergies, String treatments) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String username = auth.getUsername();
        // updates a table via sql
        if (!fullname.contains(" ")) {
            textArea.setText("invalid fullname entered in text area");
            System.out.println("invalid fullname entered in text area");
            return;
        }
        textArea.setText("You searched for: " + fullname);
        System.out.println(fullname);

        String firstname = fullname.split(" ")[0];
        String secondname = fullname.split(" ")[1];

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/hospital", "postgres", "janabap")) {
            if (conn != null) {
                // generate the SQL query for selected values of month and year
                Statement st = conn.createStatement();
                if (!diagnosis.equals(""))
                    st.executeUpdate("UPDATE patient SET diagnosis='" + diagnosis + "' WHERE first_name = '"
                            + firstname + "' AND surname = '" + secondname + "'");
                st.executeUpdate("INSERT INTO history VALUES ('" + username + "', 'Edited own patient diagnosis', '"
                        + dtf.format(now) + "')");
                if (!allergies.equals(""))
                    st.executeUpdate("UPDATE patient SET allergies='" + allergies + "' WHERE first_name = '"
                            + firstname + "' AND surname = '" + secondname + "'");
                st.executeUpdate("INSERT INTO history VALUES ('" + username + "', 'Edited own patient allergies', '"
                        + dtf.format(now) + "')");

                if (!treatments.equals(""))
                    st.executeUpdate("UPDATE patient SET treatments='" + treatments + "' WHERE first_name = '"
                            + firstname + "' AND surname = '" + secondname + "'");
                st.executeUpdate("INSERT INTO history VALUES ('" + username + "', 'Edited own patient treatments', '"
                        + dtf.format(now) + "')");

                // closes the result set and connection
                st.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database");
        }
    }

    private void updateDoctor(String docName, String fullname) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String username = auth.getUsername();

        if (!fullname.contains(" ")) {
            textArea.setText("invalid fullname entered in text area");
            System.out.println("invalid fullname entered in text area");
            return;
        }

        String firstname = fullname.split(" ")[0];
        String secondname = fullname.split(" ")[1];

        if (!docName.contains(" ")) {
            textArea.setText("invalid doctor name");
            System.out.println("invalid doctor name");
            return;
        } else if (!docName.contains("Dr")) {
            textArea.setText("invalid doctor name");
            System.out.println("invalid doctor name");
            return;
        }

        // write sql to update patient table
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/hospital", "postgres", "janabap")) {
            if (conn != null) {
                // generate the SQL query for selected values of month and year
                Statement st = conn.createStatement();
                st.executeUpdate("UPDATE patient SET doctorname='" + docName + "' WHERE first_name = '"
                        + firstname + "' AND surname = '" + secondname + "'");
                st.executeUpdate("INSERT INTO history VALUES ('" + username + "', 'swaped patients', '"
                        + dtf.format(now) + "')");
                JOptionPane.showConfirmDialog(null, fullname + " will be sent a confirmation message", "confirmation",
                        JOptionPane.OK_CANCEL_OPTION);

                // closes the result set and connection
                st.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database");
        }
    }

    // for testing
    public String testViewPatientDetails(String fullname) {
        if (!fullname.contains(" ")) {
            return "invalid fullname entered in text area";
        }

        String firstname = fullname.split(" ")[0];
        String secondname = fullname.split(" ")[1];

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/hospital", "postgres", "janabap")) {
            if (conn != null) {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM patient WHERE first_name = '" + firstname
                        + "' AND surname = '" + secondname + "'");

                int counter = 0;
                while (rs.next()) {

                    int patient_id = rs.getInt("patient_id");
                    int doctor_id = rs.getInt("doctor_id");
                    String first_name = rs.getString("first_name");
                    String surname = rs.getString("surname");
                    int age = rs.getInt("age");
                    String address = rs.getString("address");
                    int telephone = rs.getInt("telephone");

                    textArea.append(
                            doctor_id + "," + patient_id + "," + first_name + "," + surname + "," + age + "," + address
                                    + "," + telephone);
                    counter++;
                }
                if (counter == 0) {
                    return "name does not exist";
                } else {
                    patientName = fullname;
                }
                rs.close();
                st.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to connect to database";
        }
        return "name exists!";
    }

    public String testEditPatientDetails(String fullname, String diagnosis, String allergies, String treatments) {
        String results = "";
        if (!fullname.contains(" ")) {
            return "invalid fullname entered in text area";
        }

        String firstname = fullname.split(" ")[0];
        String secondname = fullname.split(" ")[1];

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/hospital", "postgres", "janabap")) {
            if (conn != null) {
                // generate the SQL query for selected values of month and year
                Statement st = conn.createStatement();
                if (!diagnosis.equals("")) {
                    st.executeUpdate("UPDATE patient SET diagnosis='" + diagnosis + "' WHERE first_name = '"
                            + firstname + "' AND surname = '" + secondname + "'");
                    results += "|value entered:" + diagnosis + " , database value:"
                            + st.executeQuery(
                                    "SELECT * FROM patient WHERE diagnosis=" + diagnosis + "' WHERE first_name = '"
                                            + firstname + "' AND surname = '" + secondname + "'")
                                    .getString("diagnosis");
                }
                if (!allergies.equals("")) {
                    st.executeUpdate("UPDATE patient SET allergies='" + allergies + "' WHERE first_name = '"
                            + firstname + "' AND surname = '" + secondname + "'");
                    results += "|value entered:" + allergies + " , database value:"
                            + st.executeQuery(
                                    "SELECT * FROM patient WHERE allergies" + allergies + "' WHERE first_name = '"
                                            + firstname + "' AND surname = '" + secondname + "'")
                                    .getString("allergies");
                }
                if (!treatments.equals("")) {
                    st.executeUpdate("UPDATE patient SET treatments='" + treatments + "' WHERE first_name = '"
                            + firstname + "' AND surname = '" + secondname + "'");
                    results += "|value entered:" + treatments + " , database value:"
                            + st.executeQuery(
                                    "SELECT * FROM patient WHERE treatments=" + treatments + "' WHERE first_name = '"
                                            + firstname + "' AND surname = '" + secondname + "'")
                                    .getString("treatments");
                }

                // closes the result set and connection
                st.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to connect to database";
        }
        return "edit patient details done successfully!" + results;
    }

    public String testUpdateDoctor(String docName, String fullname) {
        String result = "";

        if (!fullname.contains(" ")) {
            return "invalid fullname entered in text area";
        }

        String firstname = fullname.split(" ")[0];
        String secondname = fullname.split(" ")[1];

        if (!docName.contains(" ")) {
            return "invalid doctor name";
        } else if (!docName.contains("Dr")) {
            return "invalid doctor name";
        }

        // write sql to update patient table
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/hospital", "postgres", "janabap")) {
            if (conn != null) {
                // generate the SQL query for selected values of month and year
                Statement st = conn.createStatement();
                st.executeUpdate("UPDATE patient SET doctorname='" + docName + "' WHERE first_name = '"
                        + firstname + "' AND surname = '" + secondname + "'");
                JOptionPane.showConfirmDialog(null, fullname + " will be sent a confirmation message", "confirmation",
                        JOptionPane.OK_CANCEL_OPTION);
                result = st.executeQuery("SELECT * FROM patient WHERE doctorname='" + docName + "' WHERE first_name = '"
                        + firstname + "' AND surname = '" + secondname + "'").getString("doctorname");
                // closes the result set and connection
                st.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to connect to database";
        }
        return "done" + " doctor name entered:" + docName + " doctor name in database:" + result;
    }
}