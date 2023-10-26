import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Passwd extends JFrame{
    private JPanel mainPanel;
    private JButton BtnOK;
    private JPasswordField passwdArea1;
    private JPasswordField passwdArea2;

    public Passwd(){
        setContentPane(mainPanel);
        setTitle("Check password");
        setSize(350, 200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        BtnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text1 = new String (passwdArea1.getPassword());
                String text2 = new String (passwdArea2.getPassword());
                if (text1.length() != text2.length()){
                    JOptionPane.showMessageDialog(null, "Wrong password!", "Error" , JOptionPane.ERROR_MESSAGE);
                } else if (!text1.equals(text2)) {
                    JOptionPane.showMessageDialog(null, "Passwords are not matched!", "Error", JOptionPane.ERROR_MESSAGE);
                }else
                    JOptionPane.showMessageDialog(null, "Congratulations! You entered correct password.", "Nice :)", JOptionPane.INFORMATION_MESSAGE);
                }
        });
    }
    public static void main(String[] args) {
        Passwd password = new Passwd();
    }
}
