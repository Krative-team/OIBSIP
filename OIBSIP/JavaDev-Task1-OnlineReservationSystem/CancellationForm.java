import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CancellationForm extends JFrame {
    JTextField pnrF = new JTextField();
    JTextArea details = new JTextArea();

    public CancellationForm() {
        setTitle("Cancel Ticket");
        setSize(450, 350);
        setLocationRelativeTo(null);

        JButton fetchBtn = new JButton("Fetch");
        JButton confirmBtn = new JButton("Confirm Cancellation");

        setLayout(new BorderLayout(10,10));
        JPanel top = new JPanel(new GridLayout(1,3));
        top.add(new JLabel("PNR Number:")); top.add(pnrF); top.add(fetchBtn);
        add(top, BorderLayout.NORTH);
        details.setEditable(false);
        add(new JScrollPane(details), BorderLayout.CENTER);
        add(confirmBtn, BorderLayout.SOUTH);

        fetchBtn.addActionListener(e -> fetchDetails());
        confirmBtn.addActionListener(e -> cancelTicket());

        setVisible(true);
    }

    void fetchDetails() {
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM reservations WHERE pnr=?")) {
            ps.setString(1, pnrF.getText().trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                details.setText("PNR: " + rs.getString(1) +
                        "\nName: " + rs.getString(2) +
                        "\nTrain No: " + rs.getString(3) +
                        "\nTrain Name: " + rs.getString(4) +
                        "\nClass: " + rs.getString(5) +
                        "\nDOJ: " + rs.getString(6) +
                        "\nSource: " + rs.getString(7) +
                        "\nDestination: " + rs.getString(8));
            } else {
                details.setText("No booking found for PNR: " + pnrF.getText());
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    void cancelTicket() {
        if (pnrF.getText().isEmpty()) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection c = DatabaseManager.getConnection();
                 PreparedStatement ps = c.prepareStatement("DELETE FROM reservations WHERE pnr=?")) {
                ps.setString(1, pnrF.getText().trim());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Booking Cancelled Successfully");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "PNR Not Found");
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
}
