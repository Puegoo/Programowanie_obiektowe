import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;
import java.util.Random;
import java.util.function.Predicate;

public class Win_Register extends JFrame {
    private JPanel mainPanel;
    private JTextField textLogin;
    private JTextField textName;
    private JTextField textSurname;
    private JPasswordField textPasswd1;
    private JPasswordField textPasswd2;
    private JTextField textEmail;
    private JRadioButton maleRadioBtn;
    private JRadioButton femaleRadioBtn;
    private JLabel iconAvatar;
    private JButton BtnClear;
    private JButton BtnCreate;
    private JButton BtnBack;
    private JLabel labelInfo;
    ImageIcon Avatar = new ImageIcon("icons/avatarRegister.png");

    public static void main(String[] args) {
        Win_Register register = new Win_Register();
        register.setVisible(true);
    }

    public Win_Register() {
        super("Register");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 600);
        setResizable(false);
        iconAvatar.setIcon(Avatar);
        setLocationRelativeTo(null);

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioBtn);
        genderGroup.add(femaleRadioBtn);

        maleRadioBtn.addActionListener(e -> iconAvatar.setIcon(new ImageIcon("icons/avatarMan.png")));
        femaleRadioBtn.addActionListener(e -> iconAvatar.setIcon(new ImageIcon("icons/avatarWoman.png")));

        BtnCreate.addActionListener(e -> {
            if (validateAllFields()) {
                try {
                    String gender = maleRadioBtn.isSelected() ? "M" : "F";
                    if (insertUser(textLogin.getText(), textName.getText(), textSurname.getText(), new String(textPasswd1.getPassword()), textEmail.getText(), gender)) {
                        labelInfo.setForeground(new Color(0, 128, 0));
                        labelInfo.setText("Rejestracja pomyślna!");
                        new Win_Log().setVisible(true);
                        dispose();
                    } else {
                        labelInfo.setForeground(new Color(240, 10, 10));
                        labelInfo.setText("Błąd rejestracji.");
                    }
                } catch (SQLException ex) {
                    labelInfo.setForeground(new Color(240, 10, 10));
                    labelInfo.setText("Błąd bazy danych: " + ex.getMessage());
                }
            }
        });

        BtnClear.addActionListener(e -> clearForm(genderGroup));
        BtnBack.addActionListener(e -> {
            new Win_Log().setVisible(true);
            dispose();
        });

        setupValidation();
    }

    private void clearForm(ButtonGroup genderGroup) {
        textLogin.setText("");
        textName.setText("");
        textSurname.setText("");
        textPasswd1.setText("");
        textPasswd2.setText("");
        textEmail.setText("");
        genderGroup.clearSelection();
        iconAvatar.setIcon(new ImageIcon("icons/avatarRegister.png"));
        labelInfo.setText("");

        resetBorder(textLogin);
        resetBorder(textName);
        resetBorder(textSurname);
        resetBorder(textPasswd1);
        resetBorder(textPasswd2);
        resetBorder(textEmail);
    }

    private void resetBorder(JComponent component) {
        component.setBorder(UIManager.getBorder("TextField.border"));
    }

    private void setupValidation() {
        textLogin.addFocusListener(new ValidationFocusListener(textLogin, this::validateLogin));
        textName.addFocusListener(new ValidationFocusListener(textName, this::validateNotEmpty));
        textSurname.addFocusListener(new ValidationFocusListener(textSurname, this::validateNotEmpty));
        textPasswd1.addFocusListener(new ValidationFocusListener(textPasswd1, this::validatePassword));
        textPasswd2.addFocusListener(new ValidationFocusListener(textPasswd2, (component) -> validatePasswordMatch()));
        textEmail.addFocusListener(new ValidationFocusListener(textEmail, this::validateEmail));
    }

    private boolean validateAllFields() {
        boolean loginValid = validateLogin(textLogin);
        boolean nameValid = validateNotEmpty(textName);
        boolean surnameValid = validateNotEmpty(textSurname);
        boolean passwordValid = validatePassword(textPasswd1);
        boolean passwordMatchValid = validatePasswordMatch();
        boolean emailValid = validateEmail(textEmail);
        return loginValid && nameValid && surnameValid && passwordValid && passwordMatchValid && emailValid;
    }

    private boolean validateLogin(JComponent component) {
        try {
            JTextField textField = (JTextField) component;
            String login = textField.getText();
            if (login.length() < 5 || !isLoginAvailable(login)) {
                setError(textField, "Login is too short or unavailable");
                return false;
            }
            setSuccess(textField);
            return true;
        } catch (SQLException ex) {
            setError(component, "Database error");
            return false;
        }
    }

    private boolean validateNotEmpty(JComponent component) {
        JTextField textField = (JTextField) component;
        if (textField.getText().isEmpty()) {
            setError(textField, "Field cannot be empty");
            return false;
        }
        setSuccess(textField);
        return true;
    }

    private boolean validatePassword(JComponent component) {
        JPasswordField passwordField = (JPasswordField) component;
        String password = new String(passwordField.getPassword());
        if (!(password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*\\d.*") && password.length() >= 8)) {
            setError(passwordField, "Password does not meet criteria");
            return false;
        }
        setSuccess(passwordField);
        return true;
    }

    private boolean validatePasswordMatch() {
        String password = new String(textPasswd1.getPassword());
        String confirmPassword = new String(textPasswd2.getPassword());
        if (!confirmPassword.equals(password)) {
            setError(textPasswd2, "Passwords do not match");
            return false;
        }
        setSuccess(textPasswd2);
        return true;
    }

    private boolean validateEmail(JComponent component) {
        JTextField textField = (JTextField) component;
        String email = textField.getText();
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            setError(textField, "Invalid email address");
            return false;
        }
        setSuccess(textField);
        return true;
    }

    private boolean isLoginAvailable(String login) throws SQLException {
        String query = "SELECT COUNT(*) FROM Users WHERE Login = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/unity_bank", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        }
    }

    private boolean insertUser(String login, String name, String surname, String password, String email, String gender) throws SQLException {
        String accountNumber = generateAccountNumber();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/unity_bank", "root", "");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Users (Login, Imie, Nazwisko, Haslo, Email, Plec, NumerRachunku) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, login);
            stmt.setString(2, name);
            stmt.setString(3, surname);
            stmt.setString(4, password);
            stmt.setString(5, email);
            stmt.setString(6, gender);
            stmt.setString(7, accountNumber);
            return stmt.executeUpdate() > 0;
        }
    }

    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private void setError(JComponent component, String message) {
        component.setBorder(new LineBorder(Color.RED));
        labelInfo.setForeground(Color.RED);
        labelInfo.setText(message);
    }

    private void setSuccess(JComponent component) {
        component.setBorder(UIManager.getBorder("TextField.border"));
        labelInfo.setText("");
    }

    private class ValidationFocusListener implements FocusListener {
        private final JComponent component;
        private final Predicate<JComponent> validationFunction;

        public ValidationFocusListener(JComponent component, Predicate<JComponent> validationFunction) {
            this.component = component;
            this.validationFunction = validationFunction;
        }

        @Override
        public void focusGained(FocusEvent e) {}

        @Override
        public void focusLost(FocusEvent e) {
            validationFunction.test(component);
        }
    }
}
