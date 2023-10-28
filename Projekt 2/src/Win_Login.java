import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Win_Login extends JFrame{
    private JLabel iconAvatar;
    private JPanel mainPanel;
    private JTextField textLogin;
    private JPasswordField textPasswd;
    private JButton BtnSignUp;
    private JButton BtnSignIn;
    private JLabel labelError;
    private String password = "admin";
    private String login = "admin";
    ImageIcon Avatar = new ImageIcon("icons/avatarLogin.png");

    public static void main(String[] args) {
        Win_Login login = new Win_Login();
        login.setVisible(true);
    }


    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public Win_Login(){
        super("Log in to your account");
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 350);
        this.setResizable(false);
        iconAvatar.setIcon(Avatar);

        BtnSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String temporary_Login = textLogin.getText().toLowerCase();
                String temporary_Passwd = new String(textPasswd.getPassword());

                if (temporary_Login.isEmpty()){
                    labelError.setText("Wprowadź login!!! ");
                }else if (temporary_Passwd.isEmpty()) {
                    labelError.setText("Wprowadź hasło!!! ");
                }else if (!temporary_Login.equals(login.toLowerCase()) || !temporary_Passwd.equals(password)) {
                    labelError.setForeground(new Color(240, 10, 10));
                    labelError.setText("Błąd!!! ");
                }else {
                    labelError.setText("Brawo!!! ");
                }
            }
        });

        BtnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Win_Register().setVisible(true);
            }
        });
    }
}
