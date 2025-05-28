import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SeatSelectionPage extends JFrame {
    ArrayList<JToggleButton> seatButtons = new ArrayList<>();
    JButton nextButton;
    JLabel selectionLabel;

    List<String> occupiedSeats;
    String username, movie, showtime;

    public SeatSelectionPage(String username, String movie, String showtime, String details) {
        this.username = username;
        this.movie = movie;
        this.showtime = showtime;

        setTitle("Select Your Seats");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Fetch occupied seats from DB
        occupiedSeats = DatabaseHelper.getOccupiedSeats(movie, showtime);

        JPanel seatPanel = new JPanel(new GridLayout(5, 5, 5, 5));
        char[] rows = {'A', 'B', 'C', 'D', 'E'};

        for (char row : rows) {
            for (int col = 1; col <= 5; col++) {
                String seat = row + String.valueOf(col);
                JToggleButton seatBtn = new JToggleButton(seat);
                seatBtn.setOpaque(true);
                seatBtn.setContentAreaFilled(true);

                if (occupiedSeats.contains(seat)) {
                    seatBtn.setEnabled(false);
                    seatBtn.setBackground(Color.RED);
                } else {
                    seatBtn.setBackground(Color.GREEN);
                    seatBtn.addItemListener(e -> updateSelection());
                }

                seatButtons.add(seatBtn);
                seatPanel.add(seatBtn);
            }
        }

        selectionLabel = new JLabel("Selected Seats: ");
        nextButton = new JButton("Proceed to Payment");

        nextButton.addActionListener(e -> {
            List<String> selected = new ArrayList<>();
            for (JToggleButton btn : seatButtons) {
                if (btn.isSelected()) {
                    selected.add(btn.getText());
                }
            }

            if (selected.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one seat.");
                return;
            }

            // Store selected seats in DB
            for (String seat : selected) {
                DatabaseHelper.bookSeat(movie, showtime, seat, username);
            }

            dispose();

            // ✅ Convert List to ArrayList when passing to PaymentPage
            new PaymentPage(username, movie, showtime, new ArrayList<>(selected));
        });

        add(new JLabel("Select Seats (Red = Occupied, Green = Available)"), BorderLayout.NORTH);
        add(seatPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(selectionLabel, BorderLayout.CENTER);
        bottomPanel.add(nextButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateSelection() {
        StringBuilder selected = new StringBuilder("Selected Seats: ");
        for (JToggleButton btn : seatButtons) {
            if (btn.isSelected()) {
                selected.append(btn.getText()).append(" ");
            }
        }
        selectionLabel.setText(selected.toString());
    }
}

