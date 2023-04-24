import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApplicationMessager {

    private JFrame frame;
    private JComboBox<String> contactsBox;
    private JButton openButton;
    private String username;

    private String[] contacts = { "Admin: John", "Admin: Mary", " Receptionist: Tom", " Receptionist: Jane",
            " Receptionist: Bob" };

    public ApplicationMessager(Authentication auth) {
        username = auth.getUsername();
        frame = new JFrame("Messaging System");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel contactsPanel = new JPanel(new FlowLayout());
        contactsBox = new JComboBox<>(contacts);
        openButton = new JButton("Message");
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String contact = (String) contactsBox.getSelectedItem();
                openMessagingHistory(contact);
            }
        });
        contactsPanel.add(new JLabel("Contacts: "));
        contactsPanel.add(contactsBox);
        contactsPanel.add(openButton);
        frame.add(contactsPanel, BorderLayout.NORTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void openMessagingHistory(String contact) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String historyInsert = "INSERT INTO history VALUES ('" + username + "', 'login', '" + dtf.format(now) + "')";
        JFrame messagingHistory = new JFrame("Messaging History with " + contact);
        messagingHistory.setLayout(new BorderLayout());

        JTextArea chatArea = new JTextArea(20, 40);
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Helvetica", Font.PLAIN, 14));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane chatScroll = new JScrollPane(chatArea);
        messagingHistory.add(chatScroll, BorderLayout.CENTER);

        JPanel messagePanel = new JPanel(new BorderLayout());
        JTextField messageField = new JTextField(30);
        messageField.setFont(new Font("Helvetica", Font.PLAIN, 14));
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText().trim();
                if (!message.isEmpty()) {
                    String timestamp = getTimestamp();
                    chatArea.append("You (" + timestamp + "): " + message + "\n");
                    messageField.setText("");
                    simulateResponse(contact, chatArea);
                    try (Connection conn = DriverManager.getConnection(
                            "jdbc:postgresql://localhost/hospital", "postgres", "janabap")) { // connects to the
                                                                                              // database
                        if (conn != null) {
                            Statement st = conn.createStatement();
                            st.executeUpdate("INSERT INTO history VALUES ('" + username + "', 'sent a message', '"
                                    + dtf.format(now) + "')");
                            st.close();
                        } else {
                            System.out.println("Failed to connect to database");
                        }

                    } catch (Exception k) {
                        k.printStackTrace();
                        System.out.println("Failed to connect to database");
                    }
                }
            }
        });
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);
        messagingHistory.add(messagePanel, BorderLayout.SOUTH);

        messagingHistory.pack();
        messagingHistory.setLocationRelativeTo(null);
        messagingHistory.setVisible(true);
    }

    private String getTimestamp() {
        return java.time.LocalDateTime.now().toString();
    }

    private void simulateResponse(String contact, JTextArea chatArea) {
        String timestamp = getTimestamp();
        chatArea.append(contact + " (" + timestamp + "): Sent\n");
    }
}
