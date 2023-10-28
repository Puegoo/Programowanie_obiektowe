import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Win_Register extends JFrame{
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
    private ButtonGroup genderGroup;

    public static void main(String[] args) {
        Win_Register register = new Win_Register();
        register.setVisible(true);
    }

    public void errorInfo (JTextField object, String message) {
        object.setBorder(new LineBorder(Color.RED));
        labelInfo.setForeground(new Color(240, 10, 10));
        labelInfo.setText(message);
        object.setText("");
    }

    public Win_Register() {
        super("Register");
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(550, 600);
        this.setResizable(false);
        iconAvatar.setIcon(Avatar);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(maleRadioBtn);
        buttonGroup.add(femaleRadioBtn);

        maleRadioBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Avatar = new ImageIcon("icons/avatarMan.png");
                iconAvatar.setIcon(Avatar);
            }
        });

        femaleRadioBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Avatar = new ImageIcon("icons/avatarWoman.png");
                iconAvatar.setIcon(Avatar);
            }
        });

        BtnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (walidacjaJestPoprawna()) {
                    String login = textLogin.getText();
                    String password = new String(textPasswd1.getPassword());
                    Win_Login loginForm = new Win_Login();
                    loginForm.setLogin(login);
                    loginForm.setPassword(password);
                    loginForm.setVisible(true);
                    dispose();
                }
            }
        });
    }
    private boolean walidacjaJestPoprawna() {
        String login = textLogin.getText();
        String name = textName.getText();
        String surname = textSurname.getText();
        String password1 = new String(textPasswd1.getPassword());
        String password2 = new String(textPasswd2.getPassword());
        String email = textEmail.getText();
        boolean isMaleSelected = maleRadioBtn.isSelected();
        boolean isFemaleSelected = femaleRadioBtn.isSelected();

        if (login.isEmpty()) {
            errorInfo(textLogin, "Login powinien mieć co najmniej 5 znaków!");
        }
        if (password1.isEmpty()) {
            errorInfo(textLogin, "Login powinien mieć co najmniej 5 znaków!");
        }
        
        if (login.length() < 5) {
            errorInfo(textLogin, "Login powinien mieć co najmniej 5 znaków!");
        }

        if (!password1.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
            errorInfo(textPasswd1, "Hasło musi spełniać wymagane kryteria!");
            return false;
        }

        if (!password1.equals(password2)) {
            errorInfo(textPasswd2, "Hasła nie pasują do siebie!");
            return false;
        }

        if (!email.contains("@") || email.lastIndexOf(".") < email.length() - 3) {
            errorInfo(textEmail, "Podany adres email jest nieprawidłowy!");
            return false;
        }
        return true;
    }
}
