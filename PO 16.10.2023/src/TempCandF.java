import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TempCandF extends JFrame {
    private JTextField textBox2;
    private JButton przeliczBtn;
    private JButton clearBtn;
    private JPanel mainPanel;
    private JTextArea textArea2;

    public TempCandF(){
        setContentPane(mainPanel);
        setTitle("Konwerter temperatury");
        setSize (450, 300);
        setDefaultCloseOperation (WindowConstants. EXIT_ON_CLOSE);
        setVisible (true);

        przeliczBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stopF = textBox2.getText();

                float liczbaF = Float.parseFloat(stopF);

                if (!stopF.isEmpty()){
                    float C = 0;
                    C = (liczbaF - 32)/1.8f;
                    textArea2.setText(liczbaF + " w Skali Fahrenheita [°F] to " + C + " stopieni Celsjusza [°C]");
                }
            }
        });
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textBox2.setText("");
                textArea2.setText("");
            }
        });
    }

    public static void main(String[] args) {
        TempCandF myFrame = new TempCandF();
    }
}
