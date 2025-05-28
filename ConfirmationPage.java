import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ConfirmationPage extends JFrame {
    public ConfirmationPage(String movie, String showtime, List<String> seats, String cardName, double totalAmount) {
        setTitle("Booking Confirmation");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JTextArea confirmationArea = new JTextArea();
        confirmationArea.setEditable(false);

        // Set confirmation message
        confirmationArea.setText(
            "Booking Confirmed!\n\n" +
            "Movie: " + movie + "\n" +
            "Showtime: " + showtime + "\n" +
            "Seats: " + seats + "\n" +
            "Cardholder Name: " + cardName + "\n" +
            "Total Amount: ₹" + totalAmount
        );

        add(new JScrollPane(confirmationArea), BorderLayout.CENTER);

        setVisible(true);
    }
}

