import javax.swing.*;
import java.sql.*;

public class Win_Profil extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane JPanel;
    private JPanel JPanel_Profil;
    private JPanel JPanel_History;
    private JPanel JPanel_Transfer;
    private JPanel JPanel_Account;
    private JPanel mainPanel_A;
    private JPanel mainPanel_H;
    private JLabel JLabel_Plec;
    private JLabel JLabel_Name;
    private JLabel JLabel_Surname;
    private JLabel JLabel_Email;
    private JLabel JLabel_Anumber;
    private JLabel JLabel_ANum;
    private JLabel JLabel_Sum; // Dodane pole JLabel do wyświetlania salda konta

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/unity_bank";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "";
    private User currentUser;

    public static void main(String[] args) {
        // Zakładając, że istnieje obiekt User
        User user = new User(1, "sampleuser", "password", "user@example.com", "John", "Doe", 'M', "1234567890", 1000.0); // Przykładowe dane użytkownika
        Win_Profil profil = new Win_Profil(user);
        profil.setVisible(true);
    }

    public Win_Profil(User user) {
        super("Your profile");
        this.currentUser = user;
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setResizable(false);
        setLocationRelativeTo(null);

        displayUserData(user);
    }

    private void displayUserData(User user) {
        String login = user.getLogin();
        String email = getEmailFromDatabase(login);
        String imie = getImieFromDatabase(login);
        String nazwisko = getNazwiskoFromDatabase(login);
        String numerRachunku = getNumerRachunkuFromDatabase(login);
        double saldo = getAccountBalanceFromDatabase(user.getID());

        JLabel_Name.setText("Name: " + imie);
        JLabel_Surname.setText("Surname: " + nazwisko);
        JLabel_Email.setText("Email: " + email);
        JLabel_Anumber.setText("Account Number: " + numerRachunku);
        JLabel_ANum.setText(numerRachunku); // Ustawiamy numer rachunku w JLabel_ANum
        JLabel_Sum.setText("Account Balance: $" + saldo);

        updateImageLabel();
    }


    private void updateImageLabel() {
        String plec = getPlecFromDatabase();
        ImageIcon imageIcon;
        if ("M".equals(plec)) {
            imageIcon = new ImageIcon("icons/avatarMan.png");
        } else {
            imageIcon = new ImageIcon("icons/avatarWoman.png");
        }
        JLabel_Plec.setIcon(imageIcon);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private String getPlecFromDatabase() {
        String plec = "";
        String query = "SELECT Plec FROM Users WHERE Login = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, currentUser.getLogin());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    plec = rs.getString("Plec");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plec;
    }

    private String getEmailFromDatabase(String login) {
        String email = "";
        String query = "SELECT Email FROM Users WHERE Login = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    email = rs.getString("Email");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return email;
    }

    private String getImieFromDatabase(String login) {
        String imie = "";
        String query = "SELECT Imie FROM Users WHERE Login = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    imie = rs.getString("Imie");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return imie;
    }

    private String getNazwiskoFromDatabase(String login) {
        String nazwisko = "";
        String query = "SELECT Nazwisko FROM Users WHERE Login = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nazwisko = rs.getString("Nazwisko");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nazwisko;
    }

    private String getNumerRachunkuFromDatabase(String login) {
        String numerRachunku = "";
        String query = "SELECT NumerRachunku FROM Users WHERE Login = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    numerRachunku = rs.getString("NumerRachunku");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numerRachunku;
    }

    private double getAccountBalanceFromDatabase(int userID) {
        double accountBalance = 0.0;
        String query = "SELECT Balance FROM AccountBalances WHERE UserID = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
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
