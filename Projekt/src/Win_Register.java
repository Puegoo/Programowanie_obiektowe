import javax.swing.*;
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
    private JButton button1;
    private JButton button2;
    private JButton BtnBack;
    ImageIcon Avatar = new ImageIcon("icons/avatarRegister.png");
    private ButtonGroup genderGroup;

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
    }

}
