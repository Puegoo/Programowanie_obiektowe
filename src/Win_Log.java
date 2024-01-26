import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class Win_Log extends JFrame {
    private JPanel mainPanel;
    private JTextField textLogin;
    private JPasswordField textPasswd;
    private JButton BtnSignUp;
    private JButton BtnSignIn;
    private JLabel labelError;
    private JLabel JTextWelcome;
    private JLabel iconAvatar;
    ImageIcon Unity_bank = new ImageIcon("icons/UnityBankWel.png");
    ImageIcon Avatar = new ImageIcon("icons/avatarLogin.png");

    public static void main(String[] args) {
        Win_Log login = new Win_Log();
        login.setVisible(true);
    }

    public Win_Log() {
        super("Log in to your account");
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setResizable(false);
        iconAvatar.setIcon(Avatar);
        Image unityBankImage = Unity_bank.getImage();
        Image scaledUnityBankImage = unityBankImage.getScaledInstance(400, 600, Image.SCALE_SMOOTH);
        Unity_bank = new ImageIcon(scaledUnityBankImage);
        JTextWelcome.setIcon(Unity_bank);
        setLocationRelativeTo(null);

        setupPlaceholder(textLogin, " login");
        textPasswd.setEchoChar((char) 0);
        textPasswd.setText(" password");
        textPasswd.setForeground(Color.GRAY);

        textPasswd.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(textPasswd.getPassword()).equals(" password")) {
                    textPasswd.setText("");
                    textPasswd.setEchoChar('*');
                    textPasswd.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (textPasswd.getPassword().length == 0) {
                    textPasswd.setText(" password");
                    textPasswd.setEchoChar((char) 0);
                    textPasswd.setForeground(Color.GRAY);
                }
            }
        });

        BtnSignIn.addActionListener(e -> {
            String temporary_Login = textLogin.getText().toLowerCase();
            String temporary_Passwd = new String(textPasswd.getPassword());

            if (temporary_Login.isEmpty() || temporary_Login.equals("login")) {
                labelError.setText("Wprowadź login!!!");
            } else if (temporary_Passwd.isEmpty() || temporary_Passwd.equals("hasło")) {
                labelError.setText("Wprowadź hasło!!!");
            } else {
                try {
                    User user = authenticate(temporary_Login, temporary_Passwd);
                    if (user != null) {
                        dispose();
                        new Win_Profil(user).setVisible(true);
                    } else {
                        labelError.setForeground(new Color(240, 10, 10));
                        labelError.setText("Błąd!!!");
                    }
                } catch (SQLException ex) {
                    labelError.setForeground(new Color(240, 10, 10));
                    labelError.setText("Błąd bazy danych!");
                }
            }
        });

        BtnSignUp.addActionListener(e -> {
            dispose();
            new Win_Register().setVisible(true);
        });

        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                mainPanel.requestFocusInWindow();
            }
        });
    }

    private void setupPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }

    private User authenticate(String login, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/unity_bank", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE Login = ? AND Haslo = ?")) {

            stmt.setString(1, login);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(rs.getInt("ID"), rs.getString("Login"), rs.getString("Haslo"),
                            rs.getString("Email"), rs.getString("Imie"), rs.getString("Nazwisko"),
                            rs.getString("Plec").charAt(0), rs.getString("NumerRachunku"),
                            getAccountBalanceFromDatabase(rs.getInt("ID")));
                    return user;
                }
            }
        }
        return null;
    }

    private double getAccountBalanceFromDatabase(int userID) {
        double accountBalance = 0.0;
        String query = "SELECT Balance FROM AccountBalances WHERE UserID = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/unity_bank", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    accountBalance = rs.getDouble("Balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountBalance;
    }
}
