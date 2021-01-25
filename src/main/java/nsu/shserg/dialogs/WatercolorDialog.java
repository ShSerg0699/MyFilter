package nsu.shserg.dialogs;

import nsu.shserg.MainFrame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class WatercolorDialog extends JPanel {
    private static final int MAX_MATRIX_SIZE = 11;
    private static final int MIN_MATRIX_SIZE = 5;
    private static final int DEFAULT_MATRIX_SIZE = 5;

    private final JSlider sliderMatrixSize = new JSlider(MIN_MATRIX_SIZE, MAX_MATRIX_SIZE, DEFAULT_MATRIX_SIZE);
    private final JSpinner spinnerMatrixSize = new JSpinner(new SpinnerNumberModel(DEFAULT_MATRIX_SIZE, MIN_MATRIX_SIZE, MAX_MATRIX_SIZE, 2));
    private final MainFrame frame;

    public WatercolorDialog(MainFrame frame) {
        this.frame = frame;
        sliderMatrixSize.setPaintTicks(true);
        sliderMatrixSize.setPaintLabels(true);
        sliderMatrixSize.setMajorTickSpacing(2);
        sliderMatrixSize.addChangeListener(e -> spinnerMatrixSize.setValue(sliderMatrixSize.getValue()));
        spinnerMatrixSize.addChangeListener(e -> sliderMatrixSize.setValue((int) spinnerMatrixSize.getValue()));
        spinnerMatrixSize.setToolTipText("Only odd numbers from 5 to 11");
        sliderMatrixSize.setToolTipText("Only odd numbers from 5 to 11");

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(10));

        JPanel matrixSize = this.createSliderSpinnerPanel(spinnerMatrixSize, sliderMatrixSize);
        matrixSize.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.WHITE),
                "Matrix Size", TitledBorder.LEADING, TitledBorder.TOP));
        this.add(matrixSize);
        this.add(Box.createVerticalStrut(15));
    }

    private JPanel createSliderSpinnerPanel(JSpinner spinner, JSlider slider) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(spinner);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(slider);
        return panel;
    }

    public int showDialog() {
        return JOptionPane.showConfirmDialog(frame, this, "Watercolor settings",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    public int getMatrixSize() {
        if (sliderMatrixSize.getValue() % 2 == 0) {
            JOptionPane.showMessageDialog(frame,
                    "<html><h2>incorrectly value </h2><i>Only odd numbers from 5 to 11 HTML</i>");
            return -1;
        }
        return sliderMatrixSize.getValue();
    }
}