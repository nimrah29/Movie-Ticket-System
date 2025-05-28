import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton, registerButton;

    public LoginPage() {
        setTitle("Login");
        setSize(300, 170);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        add(loginButton);
        add(registerButton);

        loginButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (DatabaseHelper.checkLogin(user, pass)) {
                CurrentUser.username = user; // store logged-in user
                dispose();
                new MovieSelectionPage(); // user will be passed from CurrentUser
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }
        });

        registerButton.addActionListener(e -> new RegisterPage());

        setVisible(true);

        // ✅ Create required tables at start
        DatabaseHelper.initializeAllTables();

        // ❌ This is removed (no longer needed):
        // DatabaseHelper.insertDefaultUser();
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}

