import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;
import java.util.Random;

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

    boolean createAccount = false;

    public static void main(String[] args) {
        Win_Register register = new Win_Register();
        register.setVisible(true);
    }

    public Win_Register() {
        super("Register");
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(550, 600);
        this.setResizable(false);
        iconAvatar.setIcon(Avatar);
        setLocationRelativeTo(null);

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioBtn);
        genderGroup.add(femaleRadioBtn);

        maleRadioBtn.addActionListener(e -> {
            Avatar = new ImageIcon("icons/avatarMan.png");
            iconAvatar.setIcon(Avatar);
        });

        femaleRadioBtn.addActionListener(e -> {
            Avatar = new ImageIcon("icons/avatarWoman.png");
            iconAvatar.setIcon(Avatar);
        });

        BtnCreate.addActionListener(e -> {
            if (createAccount) {
                if (femaleRadioBtn.isSelected() || maleRadioBtn.isSelected()) {
                    try {
                        String gender = maleRadioBtn.isSelected() ? "M" : "F";
                        String login = textLogin.getText();
                        String name = textName.getText();
                        String surname = textSurname.getText();
                        String email = textEmail.getText();
                        String password = new String(textPasswd1.getPassword());

                        if (insertUser(login, name, surname, password, email, gender)) {
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
                } else {
                    labelInfo.setForeground(new Color(240, 10, 10));
                    labelInfo.setText("Podaj płeć!");
                }
            }
        });

        BtnClear.addActionListener(e -> {
            textLogin.setText("");
            textName.setText("");
            textSurname.setText("");
            textPasswd1.setText("");
            textPasswd2.setText("");
            textEmail.setText("");
            genderGroup.clearSelection();
            Avatar = new ImageIcon("icons/avatarRegister.png");
            iconAvatar.setIcon(Avatar);
            labelInfo.setText("");
        });

        BtnBack.addActionListener(e -> {
            new Win_Log().setVisible(true);
            dispose();
        });

        textLogin.addFocusListener(new MyFocusListener(textLogin));
        textName.addFocusListener(new MyFocusListener(textName));
        textSurname.addFocusListener(new MyFocusListener(textSurname));
        textPasswd1.addFocusListener(new MyFocusListener(textPasswd1));
        textPasswd2.addFocusListener(new MyFocusListener(textPasswd2));
        textEmail.addFocusListener(new MyFocusListener(textEmail));
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
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
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

    private class MyFocusListener implements FocusListener {
        private final JTextField addattribute;

        public MyFocusListener(JTextField attribute) {
            this.addattribute = attribute;
        }

        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (addattribute.equals(textLogin)) {
                if (textLogin.getText().length() < 5) {
                    errorInfo(textLogin, "Login powinien mieć co najmniej 5 znaków!");
                } else {
                    correctInfo(textLogin);
                }
            } else if (addattribute.equals(textName)) {
                if (textName.getText().isEmpty()) {
                    errorInfo(textName, "Wprowadź dane!");
                } else {
                    correctInfo(textName);
                }
            } else if (addattribute.equals(textSurname)) {
                if (textSurname.getText().isEmpty()) {
                    errorInfo(textSurname, "Wprowadź dane!");
                } else {
                    correctInfo(textSurname);
                }
            } else if (addattribute.equals(textPasswd1)) {
                String passwd = new String(textPasswd1.getPassword());
                if (passwd.matches(".*[A-Z].*") && passwd.matches(".*[a-z].*") && passwd.matches(".*\\d.*") && passwd.length() >= 8) {
                    correctInfo(textPasswd1);
                } else {
                    errorInfo(textPasswd1, "Wprowadź poprawne hasło");
                }
            } else if (addattribute.equals(textPasswd2)) {
                String passwd1 = new String(textPasswd1.getPassword());
                String passwd2 = new String(textPasswd2.getPassword());
                if (!passwd1.isEmpty()) {
                    if (!passwd2.equals(passwd1)) {
                        errorInfo(textPasswd2, "Hasła nie są jednakowe!");
                    } else {
                        correctInfo(textPasswd2);
                    }
                }
            } else if (addattribute.equals(textEmail)) {
                if (!textEmail.getText().contains("@") || textEmail.getText().lastIndexOf(".") < textEmail.getText().length() - 3) {
                    errorInfo(textEmail, "Podany adres email jest nieprawidłowy!");
                } else {
                    correctInfo(textEmail);
                }
            }
        }
    }

    private void errorInfo(JTextField object, String message) {
        object.setBorder(new LineBorder(Color.RED));
        labelInfo.setForeground(new Color(240, 10, 10));
        labelInfo.setText(message);
        createAccount = false;
    }

    private void correctInfo(JTextField object) {
        object.setBorder(new LineBorder(Color.GREEN));
        labelInfo.setText(" ");
        if (!textLogin.getText().isEmpty() && !textName.getText().isEmpty() && !textSurname.getText().isEmpty() && !textEmail.getText().isEmpty()) {
            createAccount = true;
        }
    }
}

