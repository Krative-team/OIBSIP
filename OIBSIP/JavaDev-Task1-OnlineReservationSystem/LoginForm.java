import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    public LoginForm() {
        setTitle("Login - Reservation System");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();
        JButton login = new JButton("Login");

        setLayout(new GridLayout(3, 2, 10, 10));
        add(new JLabel("Username:")); add(user);
        add(new JLabel("Password:")); add(pass);
        add(new JLabel()); add(login);

        login.addActionListener(e -> {
            if (DatabaseManager.validateLogin(user.getText(), new String(pass.getPassword()))) {
                dispose();
                new ReservationForm();
            } else {
                JOptionPane.showMessageDialog(this, "Access Denied - Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        setVisible(true);
    }
}
