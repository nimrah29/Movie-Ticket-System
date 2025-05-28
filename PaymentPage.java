import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PaymentPage extends JFrame {
    String username, movieName, showtime;
    ArrayList<String> seats;

    JTextField nameField, cardField;
    JButton payButton;

    public PaymentPage(String username, String movie, String showtime, ArrayList<String> selectedSeats) {
        this.username = username;
        this.movieName = movie;
        this.showtime = showtime;
        this.seats = selectedSeats;

        setTitle("Payment");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Cardholder Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Card Number:"));
        cardField = new JTextField();
        add(cardField);

        payButton = new JButton("Pay & Confirm");
        payButton.addActionListener(e -> {
            if (nameField.getText().isEmpty() || cardField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            } else {
                // Price per seat
                double totalAmount = seats.size() * 200.0;

                // Save booking in database
                DatabaseHelper.saveBooking(username, movieName, showtime, seats, totalAmount);

                dispose();

                // Pass total amount to ConfirmationPage
                new ConfirmationPage(movieName, showtime, seats, nameField.getText(), totalAmount);
            }
        });

        add(new JLabel()); // Filler cell
        add(payButton);

        setVisible(true);
    }
}

