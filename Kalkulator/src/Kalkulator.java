import javax.swing.*;

// ZADANIE DO SKOŃCZENIA (dodanie funkcji do wyłączonych przycisków, zmiana nazw zmiennych, (opcjonalnie) poprawa wyglądu kalkulatora)

public class Kalkulator extends JFrame {
    private JTextField MainAreaText;
    private JButton BtnProc;
    private JButton BtnCE;
    private JButton BtnC;
    private JButton BtnDelete;
    private JButton BtnUlam;
    private JButton a7Btn;
    private JButton a4Btn;
    private JButton a1Btn;
    private JButton Btnplumin;
    private JButton BtnDziel;
    private JButton BtnPier;
    private JButton BtnPot;
    private JButton a8Btn;
    private JButton a9Btn;
    private JButton BtnMnoz;
    private JButton BtnOdej;
    private JButton a6Btn;
    private JButton a5Btn;
    private JButton BtnDodawanie;
    private JButton a3Btn;
    private JButton a2Btn;
    private JButton BtnWynik;
    private JButton BtnDot;
    private JButton a0Btn;
    private JPanel MainPanel;
    private JTextField AreaText2;

    private String skladnik1 = "";
    private int liczba1;
    private String znak = "";
    private int wynik = 0;

    public Kalkulator() {
        setContentPane(MainPanel);
        setTitle("Kalkulator");
        setSize(350, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        // przyciski numeryczne
        a0Btn.addActionListener(e -> appendText("0"));
        a1Btn.addActionListener(e -> appendText("1"));
        a2Btn.addActionListener(e -> appendText("2"));
        a3Btn.addActionListener(e -> appendText("3"));
        a4Btn.addActionListener(e -> appendText("4"));
        a5Btn.addActionListener(e -> appendText("5"));
        a6Btn.addActionListener(e -> appendText("6"));
        a7Btn.addActionListener(e -> appendText("7"));
        a8Btn.addActionListener(e -> appendText("8"));
        a9Btn.addActionListener(e -> appendText("9"));

        // przyciski działań
        BtnDodawanie.addActionListener(e -> dodajOperator(" +"));
        BtnOdej.addActionListener(e -> dodajOperator(" -"));
        BtnDziel.addActionListener(e -> dodajOperator(" /"));
        BtnMnoz.addActionListener(e -> dodajOperator(" x"));

        // przycisk usuwania znaku
        BtnDelete.addActionListener(e -> DeleteSymbol());

        // przycisk =
        BtnWynik.addActionListener(e -> Oblicz());

        // reszta przycisków
        BtnProc.addActionListener(e -> {});
        BtnCE.addActionListener(e -> {
            MainAreaText.setText("");
            AreaText2.setText("");
        });
        BtnC.addActionListener(e -> {
            MainAreaText.setText("");
        });
        BtnPier.addActionListener(e -> {});
        BtnPot.addActionListener(e -> {});
        Btnplumin.addActionListener(e -> {});
        BtnUlam.addActionListener(e -> {});
        BtnDot.addActionListener(e -> {});
    }

    // funkcja od dodawania cyfr
    public void appendText(String number) {
        MainAreaText.setText(MainAreaText.getText() + number);
    }

    // funkcja od dodawania operatorów działania
    public void dodajOperator(String operator) {
        skladnik1 = MainAreaText.getText();
        liczba1 = Integer.parseInt(skladnik1);
        AreaText2.setText(skladnik1 + operator);
        MainAreaText.setText("");
        znak = operator;
    }

    // funkcja od usuwania znaku
    public void DeleteSymbol(){
        if (!MainAreaText.getText().isEmpty()) {
            MainAreaText.setText(MainAreaText.getText().substring(0, MainAreaText.getText().length() - 1));
        }
    }

    // funckja od liczenia wyniku
    public void Oblicz() {
        String skladnik2 = MainAreaText.getText();
        int liczba2 = Integer.parseInt(skladnik2);
        wynik = 0;
        switch (znak) {
            case " +":
                wynik = liczba1 + liczba2;
                break;
            case " -":
                wynik = liczba1 - liczba2;
                break;
            case " /":
                if (liczba2 != 0) {
                    wynik = liczba1 / liczba2;
                } else {
                    AreaText2.setText("Błąd: Dzielenie przez zero");
                    return;
                }
                break;
            case " x":
                wynik = liczba1 * liczba2;
                break;
        }
        MainAreaText.setText("");
        AreaText2.setText(Integer.toString(wynik));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Kalkulator());
    }
}