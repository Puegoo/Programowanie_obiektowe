import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    private JLabel JLabel_Sum;
    private JButton Btn_Logout;
    private JTextField JLabel_DepositSum;
    private JButton Btn_Deposit;
    private JButton Btn_Clear;
    private JPanel mainPanel_T;
    private JComboBox JComboBox_T;
    private JTextField JLabel_TSum;
    private JButton Btn_Transfer;
    private JLabel JLabel_TBalance;
    private JTable JTable_H;

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/unity_bank";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "";
    private User currentUser;

    public static void main(String[] args) {
        User user = new User(1, "sampleuser", "password", "user@example.com", "John", "Doe", 'M', "1234567890", 1000.0);
        Win_Profil profil = new Win_Profil(user);
        profil.setVisible(true);
    }

    public Win_Profil(User user) {
        super("Your Profile");
        this.currentUser = user;
        setContentPane(this.mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        displayUserData();
        setupActions();
        initializeTable();
        loadTransactionHistory();
    }

    private void initializeTable() {
        JTable_H.setModel(new DefaultTableModel(new Object[]{"Account Number", "Amount", "Date", "Status", "Type"}, 0));
    }

    private void displayUserData() {
        JLabel_Name.setText(currentUser.getFirstName());
        JLabel_Surname.setText(currentUser.getLastName());
        JLabel_Email.setText(currentUser.getEmail());
        JLabel_Anumber.setText(currentUser.getAccountNumber());
        JLabel_ANum.setText(currentUser.getAccountNumber());
        JLabel_Sum.setText("Account Balance: $" + currentUser.getAccountBalance());
        updateImageLabel();
    }

    private void updateImageLabel() {
        ImageIcon imageIcon = (currentUser.getGender() == 'M') ? new ImageIcon("icons/avatarMan.png") : new ImageIcon("icons/avatarWoman.png");
        JLabel_Plec.setIcon(imageIcon);
    }

    private void setupActions() {
        Btn_Logout.addActionListener(e -> logout());
        Btn_Deposit.addActionListener(e -> deposit());
        Btn_Clear.addActionListener(e -> JLabel_DepositSum.setText(""));
        Btn_Transfer.addActionListener(e -> transfer());
        loadAccountNumbers();
    }

    private void logout() {
        dispose();
        new Win_Log().setVisible(true);
    }

    private void deposit() {
        try {
            double depositAmount = Double.parseDouble(JLabel_DepositSum.getText());
            if (depositAmount < 0) {
                JOptionPane.showMessageDialog(this, "Amount cannot be less than 0", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double currentBalance = currentUser.getAccountBalance();
            double newBalance = currentBalance + depositAmount;
            updateAccountBalance(currentUser.getID(), newBalance);
            currentUser.setAccountBalance(newBalance);
            JLabel_Sum.setText("Account Balance: $" + newBalance);
            JLabel_DepositSum.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateAccountBalance(int userID, double newBalance) throws SQLException {
        String query = "UPDATE AccountBalances SET Balance = ? WHERE UserID = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, newBalance);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
        }
    }

    private void transfer() {
        try {
            double transferAmount = Double.parseDouble(JLabel_TSum.getText());
            if (transferAmount <= 0 || transferAmount > currentUser.getAccountBalance()) {
                JOptionPane.showMessageDialog(this, "Invalid transfer amount", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String receiverAccountNumber = (String) JComboBox_T.getSelectedItem();
            int receiverID = getUserIDByAccountNumber(receiverAccountNumber);
            if (receiverID == -1) {
                JOptionPane.showMessageDialog(this, "Receiver account not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double currentBalance = currentUser.getAccountBalance();
            double newSenderBalance = currentBalance - transferAmount;
            updateAccountBalance(currentUser.getID(), newSenderBalance);
            currentUser.setAccountBalance(newSenderBalance);
            JLabel_Sum.setText("Account Balance: $" + newSenderBalance);
            JLabel_TBalance.setText("Account Balance: $" + newSenderBalance);
            double newReceiverBalance = getAccountBalanceFromDatabase(receiverID) + transferAmount;
            updateAccountBalance(receiverID, newReceiverBalance);
            recordTransaction(currentUser.getID(), receiverID, transferAmount);
            JLabel_TSum.setText("");
            loadTransactionHistory();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void recordTransaction(int senderID, int receiverID, double amount) throws SQLException {
        String query = "INSERT INTO Transactions (IDNadawcy, IDOdbiorcy, Kwota, Data, Status) VALUES (?, ?, ?, NOW(), 'Completed')";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, senderID);
            stmt.setInt(2, receiverID);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
        }
    }

    private int getUserIDByAccountNumber(String accountNumber) throws SQLException {
        String query = "SELECT ID FROM Users WHERE NumerRachunku = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        }
        return -1;
    }

    private double getAccountBalanceFromDatabase(int userID) throws SQLException {
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
        }
        return accountBalance;
    }

    private void loadAccountNumbers() {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT NumerRachunku FROM Users WHERE ID != " + currentUser.getID())) {
            while (rs.next()) {
                JComboBox_T.addItem(rs.getString("NumerRachunku"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTransactionHistory() {
        DefaultTableModel model = (DefaultTableModel) JTable_H.getModel();
        model.setRowCount(0);

        String query = "SELECT u.NumerRachunku, t.Kwota, t.Data, t.Status, t.IDNadawcy FROM Transactions t JOIN Users u ON (t.IDOdbiorcy = u.ID AND t.IDNadawcy = ?) OR (t.IDNadawcy = u.ID AND t.IDOdbiorcy = ?) ORDER BY t.Data DESC;";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, currentUser.getID());
            stmt.setInt(2, currentUser.getID());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String accountNumber = rs.getString("NumerRachunku");
                    double amount = rs.getDouble("Kwota");
                    Timestamp date = rs.getTimestamp("Data");
                    String status = rs.getString("Status");
                    int senderID = rs.getInt("IDNadawcy");
                    String type = (senderID == currentUser.getID()) ? "-" : "+";

                    model.addRow(new Object[]{accountNumber, amount, date, status, type});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
