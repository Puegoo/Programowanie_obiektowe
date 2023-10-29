import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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
    boolean createAccount = false;

    public static void main(String[] args) {
        Win_Register register = new Win_Register();
        register.setVisible(true);
    }

    public void errorInfo (JTextField object, String message) {
        object.setBorder(new LineBorder(Color.RED));
        labelInfo.setForeground(new Color(240, 10, 10));
        labelInfo.setText(message);
        createAccount = false;
    }
    public void correctInfo (JTextField object) {
        object.setBorder(new LineBorder(Color.GREEN));
        labelInfo.setText(" ");
        if (!textLogin.getText().isEmpty() && !textName.getText().isEmpty() && !textSurname.getText().isEmpty() && !textEmail.getText().isEmpty()){
            createAccount = true;
        }
    }
    public void basicAppearance (JTextField object) {
        object.setBorder(null);
        object.setText("");
        labelInfo.setText(" ");
    }

    public class MyFocusListener implements FocusListener {
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
                if(textLogin.getText().length() < 5){
                    errorInfo(textLogin, "Login pownien mieć co najmniej 5 znaków!");
                }else
                    correctInfo(textLogin);
            } else if (addattribute.equals(textName)) {
                if(textName.getText().isEmpty()){
                    errorInfo(textName, "Wprowadź dane!");
                }else
                    correctInfo(textName);
            } else if (addattribute.equals(textSurname)) {
                if (textSurname.getText().isEmpty()) {
                    errorInfo(textSurname, "Wprowadź dane!");
                } else
                    correctInfo(textSurname);
            } else if (addattribute.equals(textPasswd1)){
                String passwd = new String (textPasswd1.getPassword());
                if (passwd.matches(".*[A-Z].*") && passwd.matches(".*[a-z].*") && passwd.matches(".*\\d.*") && passwd.length() >= 8) {
                    correctInfo(textPasswd1);
                } else
                    errorInfo(textPasswd1,"Wprowadź poprawne hasło");
            } else if (addattribute.equals(textPasswd2)) {
                String passwd1 = new String (textPasswd1.getPassword());
                String passwd2 = new String (textPasswd2.getPassword());
                if (!passwd1.isEmpty()){
                    if (!passwd2.equals(passwd1)){
                        errorInfo(textPasswd2, "Hasła nie są jednakowe!");
                    }else
                        correctInfo(textPasswd2);
                }
            } else if (addattribute.equals(textEmail)){
                if (!textEmail.getText().contains("@") || textEmail.getText().lastIndexOf(".") < textEmail.getText().length() - 3) {
                    errorInfo(textEmail, "Podany adres email jest nieprawidłowy!");
                }else
                    correctInfo(textEmail);
            }
        }
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
            if (createAccount){
                if(femaleRadioBtn.isSelected() || maleRadioBtn.isSelected()){
                    labelInfo.setText(" ");
                    String login = textLogin.getText();
                    String password = new String(textPasswd1.getPassword());
                    Win_Login loginForm = new Win_Login();
                    loginForm.setLogin(login);
                    loginForm.setPassword(password);
                    loginForm.setVisible(true);
                    dispose();
                }else {
                    labelInfo.setForeground(new Color(240, 10, 10));
                    labelInfo.setText("Podaj płeć!");
                }
            }
        });

        BtnClear.addActionListener(e -> {
            basicAppearance(textLogin);
            basicAppearance(textName);
            basicAppearance(textSurname);
            basicAppearance(textPasswd1);
            basicAppearance(textPasswd2);
            basicAppearance(textEmail);
            genderGroup.clearSelection();
            Avatar = new ImageIcon("icons/avatarRegister.png");
            iconAvatar.setIcon(Avatar);
        });

        BtnBack.addActionListener(e -> {
            new Win_Login().setVisible(true);
            dispose();
        });

        textLogin.addFocusListener(new MyFocusListener(textLogin));
        textName.addFocusListener(new MyFocusListener(textName));
        textSurname.addFocusListener(new MyFocusListener(textSurname));
        textPasswd1.addFocusListener(new MyFocusListener(textPasswd1));
        textPasswd2.addFocusListener(new MyFocusListener(textPasswd2));
        textEmail.addFocusListener(new MyFocusListener(textEmail));

    }

}
