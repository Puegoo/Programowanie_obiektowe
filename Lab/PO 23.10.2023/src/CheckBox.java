import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckBox extends JFrame {
    private JCheckBox checkBoxJava;
    private JCheckBox checkBoxC2;
    private JCheckBox checkBoxC1;
    private JCheckBox checkBoxPython;
    private JPanel mainPanel;
    private JButton BtnOK;

    public static void main(String[] args) {
        CheckBox checkBox = new CheckBox();
        checkBox.setVisible(true);
    }

    public CheckBox() {
        super("Kursy programowania");
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 200);
        BtnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float cena = 0;
                String msg = "";
                if (checkBoxJava.isSelected()) {
                    cena += 3500;
                    msg += "Java #price 3500PLN\n";
                }
                if (checkBoxPython.isSelected()) {
                    cena += 5000;
                    msg += "Python #price 5000PLN\n";
                }
                if (checkBoxC1.isSelected()) {
                    cena += 4000;
                    msg += "C++ #price 4000PLN\n";
                }
                if (checkBoxC2.isSelected()) {
                    cena += 3000;
                    msg += "Java #price 3000PLN\n";
                }
                msg += " ==================================\n";
                JOptionPane.showMessageDialog(null, msg + "Razem: " + cena);
            }
        });
    }
}
