import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterPage extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton registerButton;

    public RegisterPage() {
        setTitle("Register New User");
        setSize(300, 150);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("New Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("New Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        registerButton = new JButton("Register");
        add(new JLabel());
        add(registerButton);

        registerButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty");
            } else {
                boolean success = DatabaseHelper.insertNewUser(user, pass);
                if (success) {
                    JOptionPane.showMessageDialog(this, "User registered!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Username already exists.");
                }
            }
        });

        setVisible(true);
    }
}

