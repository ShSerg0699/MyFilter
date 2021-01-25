package nsu.shserg.dialogs;

import nsu.shserg.MainFrame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class RotateDialog extends JPanel {
    private static final int MAX_ANGLE = 180;
    private static final int MIN_ANGLE = -180;
    private static final int DEFAULT_ANGLE = 0;

    private final JSpinner spinnerAngle = new JSpinner(new SpinnerNumberModel(DEFAULT_ANGLE, MIN_ANGLE, MAX_ANGLE, 1));
    private final JSlider sliderAngle = new JSlider(MIN_ANGLE, MAX_ANGLE, DEFAULT_ANGLE);
    private final MainFrame frame;

    public RotateDialog(MainFrame frame) {
        this.frame = frame;
        sliderAngle.setPaintTicks(true);
        sliderAngle.setPaintLabels(true);
        sliderAngle.setMajorTickSpacing(60);
        sliderAngle.addChangeListener(e -> spinnerAngle.setValue(sliderAngle.getValue()));
        spinnerAngle.addChangeListener(e -> sliderAngle.setValue((int) spinnerAngle.getValue()));
        sliderAngle.setToolTipText("-180 to 180");
        spinnerAngle.setToolTipText("-180 to 180");

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(10));

        JPanel panel = this.createSliderSpinnerPanel(spinnerAngle, sliderAngle);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.WHITE),
                "Angle", TitledBorder.LEADING, TitledBorder.TOP));
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
        return JOptionPane.showConfirmDialog(frame, this, "Rotate settings",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    public int getAngle() {
        return sliderAngle.getValue();
    }
}
