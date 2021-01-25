package nsu.shserg.dialogs;

import nsu.shserg.MainFrame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Hashtable;

public class SmoothingDialog extends JPanel {
    private static final int MAX_MATRIX_SIZE = 11;
    private static final int MIN_MATRIX_SIZE = 3;
    private static final int DEFAULT_MATRIX_SIZE = 5;
    private static final double MAX_SIGMA = 10.0;
    private static final double MIN_SIGMA = 0.1;
    private static final double DEFAULT_SIGMA = 1.0;

    private final JSlider sliderMatrixSize = new JSlider(MIN_MATRIX_SIZE, MAX_MATRIX_SIZE, DEFAULT_MATRIX_SIZE);
    private final JSpinner spinnerMatrixSize = new JSpinner(new SpinnerNumberModel(DEFAULT_MATRIX_SIZE, MIN_MATRIX_SIZE, MAX_MATRIX_SIZE, 2));
    private final JSpinner spinnerSigma = new JSpinner(new SpinnerNumberModel(DEFAULT_SIGMA, MIN_SIGMA, MAX_SIGMA, 0.1));
    private final JSlider sliderSigma = new JSlider(1, 100, 10);
    private final MainFrame frame;

    public SmoothingDialog(MainFrame frame) {
        this.frame = frame;
        sliderMatrixSize.setPaintTicks(true);
        sliderMatrixSize.setPaintLabels(true);
        sliderMatrixSize.setMajorTickSpacing(2);
        sliderMatrixSize.addChangeListener(e -> spinnerMatrixSize.setValue(sliderMatrixSize.getValue()));
        spinnerMatrixSize.addChangeListener(e -> sliderMatrixSize.setValue((int) spinnerMatrixSize.getValue()));
        spinnerMatrixSize.setToolTipText("Only odd numbers from 3 to 11");
        sliderMatrixSize.setToolTipText("Only odd numbers from 3 to 11");

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
        sliderSigma.setLabelTable(labelTable);
        sliderSigma.setPaintTicks(true);
        sliderSigma.setPaintLabels(true);
        sliderSigma.setMajorTickSpacing(5);
        sliderSigma.addChangeListener(e -> spinnerSigma.setValue(sliderSigma.getValue() / 10.0));
        spinnerSigma.addChangeListener(e -> sliderSigma.setValue((int) ((double) spinnerSigma.getValue() * 10)));
        sliderSigma.setToolTipText("0.1 to 10");
        spinnerSigma.setToolTipText("0.1 to 10");

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(10));

        JPanel matrixSize = this.createSliderSpinnerPanel(spinnerMatrixSize, sliderMatrixSize);
        matrixSize.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.WHITE),
                "Matrix Size", TitledBorder.LEADING, TitledBorder.TOP));
        this.add(matrixSize);
        this.add(Box.createVerticalStrut(15));

        JPanel sigma = this.createSliderSpinnerPanel(spinnerSigma, sliderSigma);
        sigma.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.WHITE),
                "Sigma value", TitledBorder.LEADING, TitledBorder.TOP));
        this.add(sigma);
    }

    private JPanel createSliderSpinnerPanel(JSpinner spinner, JSlider slider) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(spinner);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(slider);
        return panel;
    }

    public int showDialog() {
        return JOptionPane.showConfirmDialog(frame, this, "Smooth settings",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    public int getMatrixSize() {
        if (sliderMatrixSize.getValue() % 2 == 0) {
            JOptionPane.showMessageDialog(frame,
                    "<html><h2>incorrectly value </h2><i>Only odd numbers from 3 to 11 HTML</i>");
            return -1;
        }
        return sliderMatrixSize.getValue();
    }

    public double getSigma() {
        return (double) spinnerSigma.getValue();
    }
}
