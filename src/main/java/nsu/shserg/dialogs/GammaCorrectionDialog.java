package nsu.shserg.dialogs;

import nsu.shserg.MainFrame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Hashtable;

public class GammaCorrectionDialog extends JPanel {
    private static final double MAX_P = 10.0;
    private static final double MIN_P = 0.1;
    private static final double DEFAULT_P = 1.0;

    private final JSpinner spinnerP = new JSpinner(new SpinnerNumberModel(DEFAULT_P, MIN_P, MAX_P, 0.1));
    private final JSlider sliderP = new JSlider(1, 100, 10);
    private final MainFrame frame;

    public GammaCorrectionDialog(MainFrame frame) {
        this.frame = frame;
        Hashtable labelTable = new Hashtable();
        labelTable.put(1, new JLabel("0.1"));
        labelTable.put(10, new JLabel("1"));
        labelTable.put(20, new JLabel("2"));
        labelTable.put(30, new JLabel("3"));
        labelTable.put(40, new JLabel("4"));
        labelTable.put(50, new JLabel("5"));
        labelTable.put(60, new JLabel("6"));
        labelTable.put(70, new JLabel("7"));
        labelTable.put(80, new JLabel("8"));
        labelTable.put(90, new JLabel("9"));
        labelTable.put(100, new JLabel("10"));
        sliderP.setLabelTable(labelTable);
        sliderP.setPaintTicks(true);
        sliderP.setPaintLabels(true);
        sliderP.setMajorTickSpacing(5);
        sliderP.addChangeListener(e -> spinnerP.setValue(sliderP.getValue() / 10.0));
        spinnerP.addChangeListener(e -> sliderP.setValue((int) ((double) spinnerP.getValue() * 10)));
        sliderP.setToolTipText("0.1 to 10");
        spinnerP.setToolTipText("0.1 to 10");

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(10));

        JPanel panel = this.createSliderSpinnerPanel(spinnerP, sliderP);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.WHITE),
                "P value", TitledBorder.LEADING, TitledBorder.TOP));
        this.add(panel);

    }

    private JPanel createSliderSpinnerPanel(JSpinner spinner, JSlider slider) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(spinner);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(slider);
        return panel;
    }

    public int showDialog() {
        return JOptionPane.showConfirmDialog(frame, this, "Gamma correction settings",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    public double getP() {
        return (double) spinnerP.getValue();
    }
}
