package nsu.shserg.dialogs;

import nsu.shserg.MainFrame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Hashtable;

public class DitheringDialog extends JPanel {
    private static final int MAX_QN = 128;
    private static final int MIN_QN = 2;
    private static final int DEFAULT_QN = 2;
    private boolean isFloydSteinberg = true;

    private final JRadioButton floydSteinberg = new JRadioButton("Floyd–Steinberg dithering");
    private final JRadioButton ordered = new JRadioButton("Ordered dithering");
    private final JSlider sliderRed = new JSlider(MIN_QN, MAX_QN, DEFAULT_QN);
    private final JSpinner spinnerRed = new JSpinner(new SpinnerNumberModel(DEFAULT_QN, MIN_QN, MAX_QN, 5));
    private final JSlider sliderGreen = new JSlider(MIN_QN, MAX_QN, DEFAULT_QN);
    private final JSpinner spinnerGreen = new JSpinner(new SpinnerNumberModel(DEFAULT_QN, MIN_QN, MAX_QN, 5));
    private final JSlider sliderBlue = new JSlider(MIN_QN, MAX_QN, DEFAULT_QN);
    private final JSpinner spinnerBlue = new JSpinner(new SpinnerNumberModel(DEFAULT_QN, MIN_QN, MAX_QN, 5));
    private final MainFrame frame;

    public DitheringDialog(MainFrame frame) {
        this.frame = frame;
        floydSteinberg.setSelected(true);
        floydSteinberg.addActionListener(e -> {
            if (floydSteinberg.isSelected()) {
                ordered.setSelected(false);
                isFloydSteinberg = true;
            }
        });
        ordered.addActionListener(e -> {
            if (ordered.isSelected()) {
                floydSteinberg.setSelected(false);
                isFloydSteinberg = false;
            }
        });

        Hashtable labelTable = new Hashtable();
        labelTable.put(2, new JLabel("2"));
        labelTable.put(32, new JLabel("32"));
        labelTable.put(64, new JLabel("64"));
        labelTable.put(96, new JLabel("96"));
        labelTable.put(128, new JLabel("128"));

        sliderRed.setLabelTable(labelTable);
        sliderRed.setPaintTicks(true);
        sliderRed.setPaintLabels(true);
        sliderRed.setMajorTickSpacing(32);
        sliderRed.addChangeListener(e -> spinnerRed.setValue(sliderRed.getValue()));
        spinnerRed.addChangeListener(e -> sliderRed.setValue((int) spinnerRed.getValue()));
        spinnerRed.setToolTipText("2 to 128");
        sliderRed.setToolTipText("2 to 128");

        sliderGreen.setLabelTable(labelTable);
        sliderGreen.setPaintTicks(true);
        sliderGreen.setPaintLabels(true);
        sliderGreen.setMajorTickSpacing(32);
        sliderGreen.addChangeListener(e -> spinnerGreen.setValue(sliderGreen.getValue()));
        spinnerGreen.addChangeListener(e -> sliderGreen.setValue((int) spinnerGreen.getValue()));
        spinnerGreen.setToolTipText("2 to 128");
        sliderGreen.setToolTipText("2 to 128");

        sliderBlue.setLabelTable(labelTable);
        sliderBlue.setPaintTicks(true);
        sliderBlue.setPaintLabels(true);
        sliderBlue.setMajorTickSpacing(32);
        sliderBlue.addChangeListener(e -> spinnerBlue.setValue(sliderBlue.getValue()));
        spinnerBlue.addChangeListener(e -> sliderBlue.setValue((int) spinnerBlue.getValue()));
        spinnerBlue.setToolTipText("2 to 128");
        sliderBlue.setToolTipText("2 to 128");

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(10));

        JPanel buttonPanel = createBorderTypePanel();
        this.add(buttonPanel);
        this.add(Box.createVerticalStrut(15));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panelRed = createSliderSpinnerPanel(spinnerRed, sliderRed);
        panelRed.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.WHITE),
                "Red", TitledBorder.LEADING, TitledBorder.TOP));
        panel.add(panelRed);
        panel.add(Box.createVerticalStrut(15));

        JPanel panelGreen = createSliderSpinnerPanel(spinnerGreen, sliderGreen);
        panelGreen.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.WHITE),
                "Green", TitledBorder.LEADING, TitledBorder.TOP));
        panel.add(panelGreen);
        panel.add(Box.createVerticalStrut(15));

        JPanel panelBlue = createSliderSpinnerPanel(spinnerBlue, sliderBlue);
        panelBlue.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.WHITE),
                "Blue", TitledBorder.LEADING, TitledBorder.TOP));
        panel.add(panelBlue);

        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.WHITE),
                "Number of quantization", TitledBorder.LEADING, TitledBorder.TOP));
        this.add(panel);

    }

    private JPanel createBorderTypePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(floydSteinberg);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(ordered);
        return panel;
    }

    private JPanel createSliderSpinnerPanel(JSpinner spinner, JSlider slider) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(spinner);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(slider);
        return panel;
    }

    public int showDialog() {
        return JOptionPane.showConfirmDialog(frame, this, "Dithering setting",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    public int getRedQuantizationNumber() {
        return sliderRed.getValue();
    }

    public int getGreenQuantizationNumber() {
        return sliderGreen.getValue();
    }

    public int getBlueQuantizationNumber() {
        return sliderBlue.getValue();
    }

    public boolean getDitheringType() {
        return isFloydSteinberg;
    }
}
