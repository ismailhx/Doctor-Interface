import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Authors:
 * Ismail Hendryx
 * Jordan Burns
 * Leonardo Donato
 * Opeyemi
 * Ben Perry
 * 
 * 
 * This is the authentication class which has the login page consisting of a
 * loging button and two textfields (username and password). If the username or
 * password does not match the password in the database, the user cannot log in.
 * Upon logging in you are navigated to the homepage. There is a menubar which
 * contains a booking Jmenu item. Upon clicking this you are directed to the
 * bookings page
 */

// Authentication

public class Authentication {

    // The current Instance
    private static Authentication currentInstance;

    // Parts

    // Password and username
    static JTextField enteredUsername = new JTextField();
    static JTextField enteredPassword = new JPasswordField();

    // Menu Items
    static JMenuBar menubar = new JMenuBar();
    static JMenu BookingParentMenu = new JMenu("Bookings");
    static JMenu LogoutParentMenu = new JMenu("Logout");
    static JMenuItem logout = new JMenuItem("Logout");
    static JMenuItem bookings = new JMenuItem(" Booking Date Search");
    static JMenuItem viewAndEditBookings = new JMenuItem("View / Edit your patients Bookings");
    static JMenuItem viewAllpatients = new JMenuItem("Patient Search");
    static JMenuItem message = new JMenuItem("Message");
    static JMenuItem history = new JMenuItem("Login History");

    // Framework
    static JFrame frame = new JFrame();
    static Container maincontentPane = frame.getContentPane();
    static JPanel nestedPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    // Colors
    static int red = 7;
    static int green = 10;
    static int blue = 45;
    static Color turquoise = new Color(0, 128, 128);
    static Color darkBlueColor = new Color(red, green, blue);

    // Time and the Date
    static LocalTime currentTime = LocalTime.now();
    static LocalDate currentDate = LocalDate.now();

    // Flags
    static boolean isClicked = false;
    static boolean isvPatients = false;

    // Labels
    static JLabel titleLabel;
    static JLabel sideLabel = new JLabel("" + currentTime);
    static JLabel usernameLabel, passwordLabel;
    static JLabel label2 = new JLabel("Please enter your login details below to enter the Doctor System");
    static JLabel doctorInstructionMessage;
    static JLabel TimeandDate;

    // Make a login button
    static JButton loginButton = new JButton(" Login ");

    // Load the background image from a URL
    URL url = new URL(
            "https://t4.ftcdn.net/jpg/02/71/49/95/360_F_271499542_62qISfiaoQTlBXSmlo4E1YPBGAVWMipI.jpg");

    // Create an Authentication instance
    public Authentication() throws IOException {
        currentInstance = this;
        authenticate();
    }

    public void authenticate() throws IOException {

        BufferedImage image = ImageIO.read(url);

        // Create an instance of the ImagePanel class, passing the image as a parameter
        ImagePanel backgroundPanel = new ImagePanel(image);
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setBackground(darkBlueColor);

        // Create an instance of the JFrame class
        frame = new JFrame();
        frame.setContentPane(backgroundPanel);

        // Create a JPanel with a BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Title Label Styling
        titleLabel = new JLabel("Welcome");
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(turquoise);

        // Username Label Styling
        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Courier New", Font.BOLD, 18));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setForeground(turquoise);

        // Password Label Styling
        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Courier New", Font.BOLD, 18));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setForeground(turquoise);

        // Frame Styling
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setTitle("Doctor Login Page");
        frame.setBackground(Color.RED);

        // Front page Label 'Please enter your login details below to enter the Doctor
        // System' Styling
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        label2.setFont(new Font("Courier New", Font.BOLD, 14));
        label2.setForeground(turquoise);

        // Username Box Styling
        enteredUsername.setFont(new Font("Courier New", Font.BOLD, 18));
        enteredUsername.setForeground(turquoise);
        enteredUsername.setBorder(BorderFactory.createLineBorder(turquoise));
        enteredUsername.setBackground(darkBlueColor);
        enteredUsername.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Password Box Styling
        enteredPassword.setFont(new Font("Courier New", Font.BOLD, 18));
        enteredPassword.setForeground(turquoise);
        enteredPassword.setBorder(BorderFactory.createLineBorder(turquoise));
        enteredPassword.setBackground(darkBlueColor);
        enteredPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Login Button Styling
        loginButton.setFont(new Font("Courier New", Font.BOLD, 18));
        loginButton.setForeground(turquoise);
        loginButton.setBorder(BorderFactory.createLineBorder(turquoise));
        loginButton.setBackground(darkBlueColor);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adding components to the Login Page Frame
        frame.add(titleLabel);
        frame.add(Box.createVerticalStrut(20));
        frame.add(label2);
        frame.add(Box.createVerticalStrut(20));
        frame.add(usernameLabel);
        frame.add(enteredUsername);
        frame.add(Box.createVerticalStrut(20));
        frame.add(passwordLabel);
        frame.add(enteredPassword);
        frame.add(Box.createVerticalStrut(20));
        frame.add(loginButton);
        frame.add(Box.createVerticalStrut(10));

        // When pressing enter, focus is shifted to next field (the password field)
        enteredUsername.addActionListener(e -> enteredPassword.requestFocus());

        // on button press text from username and password fields are sent to validation
        loginButton.addActionListener(e -> validation(enteredUsername.getText(),
                enteredPassword.getText()));

        // Set The Frame Size | close on clicking X | Set visibility to true
        frame.setSize(670, 670);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(darkBlueColor);

        frame.setVisible(true);
    }

    public static void validation(String username, String password) {

        // If the username is less than 6 characters, the username is not valid
        if (username.length() < 6) {
            titleLabel.setForeground(Color.RED);
            titleLabel.setText("Invalid Username");
            titleLabel.setFont(new Font("Courier New", Font.BOLD, 15));
            maincontentPane.setBackground(Color.BLACK);
            return;
        }

        // If the password is less than 6 characters, the username is not valid
        else if (password.length() < 8) {
            titleLabel.setForeground(Color.RED);
            titleLabel.setText("Invalid Password");
            titleLabel.setFont(new Font("Courier New", Font.BOLD, 15));
            maincontentPane.setBackground(Color.BLACK);
            return;
        }

        // password must contain a capital letter, a special character and a number

        else if (!password.matches("^(.*\\d+.*)$")) {
            titleLabel.setForeground(Color.RED);
            titleLabel.setText("Invalid Password");
            titleLabel.setFont(new Font("Courier New", Font.BOLD, 15));
            maincontentPane.setBackground(Color.BLACK);
            return;

        } else if (!password.matches("^(.*[A-Z]+.*)$")) {
            titleLabel.setForeground(Color.RED);
            titleLabel.setText("Invalid Password");
            titleLabel.setFont(new Font("Courier New", Font.BOLD, 15));
            maincontentPane.setBackground(Color.BLACK);
            return;

        } else if (!password.matches("^(.*[!@#$%^&*(),.?\"':{}|<>]+.*)$")) {
            titleLabel.setForeground(Color.RED);
            titleLabel.setText("Invalid Password");
            titleLabel.setFont(new Font("Courier New", Font.BOLD, 15));
            maincontentPane.setBackground(Color.BLACK);
            return;

        } else {
            String authResult = checkDatabase(username, password);
            authResult = "success";
            if (authResult.equals("success")) {
                // If the databse can be connected to:

                // Change the title of the frame
                frame.setTitle(
                        "The Bookings Page");

                // Add the sideLabel showing the time
                nestedPanel.setBackground(darkBlueColor);
                nestedPanel.add(sideLabel);

                // Make a menu bar
                frame.setJMenuBar(menubar);

                // Style the menu items color
                bookings.setBackground(darkBlueColor);
                message.setBackground(turquoise);
                history.setBackground(darkBlueColor);
                history.setForeground(turquoise);
                viewAndEditBookings.setBackground(turquoise);
                viewAllpatients.setBackground(turquoise);

                logout.setBackground(Color.BLACK);

                // Style the menu items FONT
                logout.setFont(new Font("Courier New", Font.ITALIC, 14));
                bookings.setFont(new Font("Courier New", Font.ITALIC, 14));
                message.setFont(new Font("Courier New", Font.ITALIC, 14));
                history.setFont(new Font("Courier New", Font.ITALIC, 14));

                viewAndEditBookings.setFont(new Font("Courier New", Font.ITALIC, 14));
                viewAllpatients.setFont(new Font("Courier New", Font.ITALIC, 14));
                BookingParentMenu.setFont(new Font("Courier New", Font.ITALIC, 14));
                LogoutParentMenu.setFont(new Font("Courier New", Font.ITALIC, 14));

                // Font colours for menu items
                BookingParentMenu.setForeground(darkBlueColor);
                logout.setForeground(Color.RED);
                viewAndEditBookings.setForeground(darkBlueColor);
                bookings.setForeground(turquoise);

                // Add Jmenuitems to a JMenu nested tab
                BookingParentMenu.add(bookings);
                BookingParentMenu.add(viewAndEditBookings);

                LogoutParentMenu.add(logout);
                LogoutParentMenu.add(history);

                // Add the menu Items to the menu bar

                menubar.add(LogoutParentMenu);
                menubar.add(viewAllpatients);
                menubar.add(BookingParentMenu);
                menubar.add(message);

                doctorInstructionMessage = new JLabel(
                        " Welcome " + enteredUsername.getText());
                doctorInstructionMessage.setFont(new Font("Courier New", Font.BOLD, 19));
                doctorInstructionMessage.setForeground(turquoise);

                TimeandDate = new JLabel(
                        " Login time: " + currentTime + " Login Date: " + currentDate + " ");
                TimeandDate.setFont(new Font("Courier New", Font.ITALIC, 10));
                TimeandDate.setForeground(turquoise);

                // makes the grid layout with a space of 1 between rows and columns

                // Clear all the stuff from the login page frame
                frame.remove(titleLabel);
                frame.remove(label2);
                frame.add(doctorInstructionMessage);
                frame.add(Box.createVerticalStrut(470));
                frame.add(TimeandDate);
                frame.remove(enteredPassword);
                frame.remove(enteredUsername);
                frame.remove(usernameLabel);
                frame.remove(passwordLabel);
                frame.remove(loginButton);

                frame.setTitle(
                        "Welcome to the Homepage " + enteredUsername.getText());
                frame.add(Box.createVerticalStrut(200));
                menubar.add(nestedPanel);
                timer.start();
                logout.addActionListener(ev -> confirmation());

                BookingsPage bookingsPage = new BookingsPage();

                bookings.addActionListener(ev -> {

                    bookingsPage.Bookings(frame, currentInstance);

                });

                // Initially enable the JMenuItem
                bookings.setEnabled(true);

                viewAllpatients.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        ViewPatientDetails gui = new ViewPatientDetails(currentInstance);

                    }
                });

                viewAndEditBookings.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        PatientDetails gui2 = new PatientDetails(currentInstance);

                    }
                });

                message.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ApplicationMessager messager = new ApplicationMessager(currentInstance);

                    }
                });

                history.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        History login_history = new History(currentInstance);

                    }
                });

            }
        }
    }

    private static String checkDatabase(String username, String password) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String historyInsert = "INSERT INTO history VALUES ('" + username + "', 'login', '" + dtf.format(now) + "')";
        Boolean found = false;

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/hospital", "postgres", "janabap")) { // connects to the database

            if (conn != null) {
                System.out.println("Connected to database");

                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT username,password FROM doctorLogin WHERE username='" + username
                        + "' AND password='" + password + "'");
                // SQL statement to check if username and password are correct

                if (rs != null && rs.next()) {
                    found = true;
                }

                if (found == true) {
                    System.out.println("success!");
                    st.executeUpdate(historyInsert);
                } else {
                    System.out.println("error, username or password not found!");
                    titleLabel.setForeground(turquoise);
                    titleLabel.setText("error, username or password not found!");
                    titleLabel.setFont(new Font("Courier New", Font.BOLD, 15));
                    return "error, username or password not found!";
                }

                rs.close();
                st.close();
            } else {
                System.out.println("Failed to connect to database");
                titleLabel.setForeground(turquoise);
                titleLabel.setText("Failed to connect to database");
                titleLabel.setFont(new Font("Courier New", Font.BOLD, 15));
                return "Failed to connect to database";
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database");
            titleLabel.setForeground(turquoise);
            titleLabel.setText("Failed to connect to database");
            titleLabel.setFont(new Font("Courier New", Font.BOLD, 15));
            return "Failed to connect to database";
        }

        return "success";
    }

    // function for making time work
    static Timer timer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {

            LocalDate currentDate = LocalDate.now();
            sideLabel.setText("" + currentDate.getDayOfMonth() + "/" + currentDate.getMonthValue() + "/"
                    + currentDate.getYear() + " " + LocalTime.now().withSecond(0).withNano(0));
            sideLabel.setAlignmentX(Component.BOTTOM_ALIGNMENT);
            sideLabel.setFont(new Font("Courier New", Font.ITALIC, 14));
            sideLabel.setForeground(turquoise);
        }
    });

    // A function needed for adding images to the frame
    class ImagePanel extends JPanel {
        private BufferedImage image;

        public ImagePanel(BufferedImage image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static String getUsername() {
        String username = enteredUsername.getText();
        return username;

    }

    // Logout Confirmation
    static void confirmation() {
        String username = enteredUsername.getText();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to Logout?", "Logout",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost/hospital", "postgres", "janabap")) { // connects to the database
                if (conn != null) {
                    Statement st = conn.createStatement();
                    st.executeUpdate(
                            "INSERT INTO history VALUES ('" + username + "', 'logout', '" + dtf.format(now) + "')");
                    st.close();
                } else {
                    System.out.println("Failed to connect to database");
                    titleLabel.setForeground(turquoise);
                    titleLabel.setText("Failed to connect to database");
                    titleLabel.setFont(new Font("Courier New", Font.BOLD, 15));
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to connect to database");
                titleLabel.setForeground(turquoise);
                titleLabel.setText("Failed to connect to database");
                titleLabel.setFont(new Font("Courier New", Font.BOLD, 15));
            }
            frame.dispose();
        } else {
            return;
        }
    }
}
