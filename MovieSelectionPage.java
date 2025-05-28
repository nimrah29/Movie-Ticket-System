import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class MovieSelectionPage extends JFrame {
    JComboBox<String> movieDropdown, showtimeDropdown;
    JTextArea movieDetails;
    JButton nextButton;

    String[] movies = {
        "Avengers: Endgame",
        "Inception",
        "Interstellar",
        "Spirited Away"
    };

    String[][] showtimes = {
        {"6:00 PM", "9:00 PM"},
        {"12:00 PM", "3:00 PM"},
        {"2:30 PM", "5:30 PM"},
        {"11:00 AM", "1:00 PM"}
    };

    HashMap<String, String> movieDescriptions = new HashMap<>();

    public MovieSelectionPage() {
        setTitle("Select a Movie");
        setSize(450, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        movieDescriptions.put("Avengers: Endgame", "Genre: Action | Duration: 3h 2m\nTheatre: Screen 1");
        movieDescriptions.put("Inception", "Genre: Sci-Fi | Duration: 2h 28m\nTheatre: Screen 2");
        movieDescriptions.put("Interstellar", "Genre: Sci-Fi | Duration: 2h 49m\nTheatre: Screen 3");
        movieDescriptions.put("Spirited Away", "Genre: Animation | Duration: 2h 5m\nTheatre: Screen 4");

        movieDropdown = new JComboBox<>(movies);
        movieDropdown.addActionListener(e -> updateShowtimes());

        showtimeDropdown = new JComboBox<>(showtimes[0]);
        showtimeDropdown.addActionListener(e -> updateDetails());

        movieDetails = new JTextArea(6, 30);
        movieDetails.setEditable(false);
        updateDetails();

        nextButton = new JButton("Proceed to Seat Selection");
        nextButton.addActionListener(e -> {
            String selectedMovie = (String) movieDropdown.getSelectedItem();
            String selectedTime = (String) showtimeDropdown.getSelectedItem();
            String details = movieDetails.getText();

            // ✅ Updated constructor call with username
            new SeatSelectionPage(CurrentUser.username, selectedMovie, selectedTime, details);

            dispose(); // close movie selection page
        });

        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        topPanel.add(new JLabel("Select Movie:"));
        topPanel.add(movieDropdown);
        topPanel.add(new JLabel("Select Showtime:"));
        topPanel.add(showtimeDropdown);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(movieDetails), BorderLayout.CENTER);
        add(nextButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateShowtimes() {
        int movieIndex = movieDropdown.getSelectedIndex();
        showtimeDropdown.setModel(new DefaultComboBoxModel<>(showtimes[movieIndex]));
        showtimeDropdown.setSelectedIndex(0);
        updateDetails();
    }

    private void updateDetails() {
        String selectedMovie = (String) movieDropdown.getSelectedItem();
        String selectedTime = (String) showtimeDropdown.getSelectedItem();
        String baseDetails = movieDescriptions.get(selectedMovie);
        movieDetails.setText(selectedMovie + "\nShowtime: " + selectedTime + "\n" + baseDetails);
    }
}

