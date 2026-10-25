import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Random;

public class ReservationForm extends JFrame {
    JTextField nameF = new JTextField();
    JTextField trainNoF = new JTextField();
    JTextField trainNameF = new JTextField();
    JComboBox<String> classBox = new JComboBox<>(new String[]{"Sleeper", "3AC", "2AC", "1AC", "General"});
    JTextField dojF = new JTextField("YYYY-MM-DD");
    JTextField sourceF = new JTextField();
    JTextField destF = new JTextField();

    public ReservationForm() {
        setTitle("Book Ticket");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        trainNameF.setEditable(false);
        trainNoF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                trainNameF.setText(DatabaseManager.getTrainName(trainNoF.getText().trim()));
            }
        });

        JButton bookBtn = new JButton("Insert / Book");
        JButton cancelPageBtn = new JButton("Go to Cancellation");

        setLayout(new GridLayout(9, 2, 8, 8));
        add(new JLabel("Passenger Name:")); add(nameF);
        add(new JLabel("Train Number:")); add(trainNoF);
        add(new JLabel("Train Name:")); add(trainNameF);
        add(new JLabel("Class Type:")); add(classBox);
        add(new JLabel("Date of Journey:")); add(dojF);
        add(new JLabel("Source:")); add(sourceF);
        add(new JLabel("Destination:")); add(destF);
        add(bookBtn); add(cancelPageBtn);

        bookBtn.addActionListener(e -> bookTicket());
        cancelPageBtn.addActionListener(e -> new CancellationForm());

        setVisible(true);
    }

    void bookTicket() {
        if (nameF.getText().isEmpty() || trainNoF.getText().isEmpty() || dojF.getText().isEmpty() || sourceF.getText().isEmpty() || destF.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No empty required fields allowed");
            return;
        }
        if (!trainNoF.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Train number must be numeric");
            return;
        }
        if (!dojF.getText().matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Date must be YYYY-MM-DD");
            return;
        }
        if (trainNameF.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid Train Number");
            return;
        }

        String pnr = "PNR" + (100000 + new Random().nextInt(900000));
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO reservations VALUES (?,?,?,?,?,?,?,?)")) {
            ps.setString(1, pnr);
            ps.setString(2, nameF.getText());
            ps.setString(3, trainNoF.getText());
            ps.setString(4, trainNameF.getText());
            ps.setString(5, (String) classBox.getSelectedItem());
            ps.setString(6, dojF.getText());
            ps.setString(7, sourceF.getText());
            ps.setString(8, destF.getText());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Booking Confirmed!\nPNR: " + pnr +
                "\nName: " + nameF.getText() +
                "\nTrain: " + trainNameF.getText() +
                "\n" + sourceF.getText() + " -> " + destF.getText() +
                "\nDate: " + dojF.getText(),
                "Confirmation", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
