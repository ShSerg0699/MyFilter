package nsu.shserg.dialogs;

import nsu.shserg.MainFrame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Hashtable;

public class VignetteDialog extends JPanel {
    private static final int MAX_INTENSITY = 100;
    private static final int MIN_INTENSITY = 0;
    private static final int DEFAULT_INTENSITY = 40;

    private final JSpinner spinnerIntensity = new JSpinner(new SpinnerNumberModel(DEFAULT_INTENSITY, MIN_INTENSITY, MAX_INTENSITY, 5));
    private final JSlider sliderIntensity = new JSlider(MIN_INTENSITY, MAX_INTENSITY, DEFAULT_INTENSITY);
    private final MainFrame frame;

    public VignetteDialog(MainFrame frame) {
        this.frame = frame;

        sliderIntensity.setPaintTicks(true);
        sliderIntensity.setPaintLabels(true);
        sliderIntensity.setMajorTickSpacing(10);
        sliderIntensity.addChangeListener(e -> spinnerIntensity.setValue(sliderIntensity.getValue()));
        spinnerIntensity.addChangeListener(e -> sliderIntensity.setValue((int) spinnerIntensity.getValue()));
        sliderIntensity.setToolTipText("0 to 100");
        spinnerIntensity.setToolTipText("0 to 100");

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(10));

        JPanel sigma = this.createSliderSpinnerPanel(spinnerIntensity, sliderIntensity);
        sigma.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.WHITE),
                "Intensity value", TitledBorder.LEADING, TitledBorder.TOP));
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
        return JOptionPane.showConfirmDialog(frame, this, "Vignette settings",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    public int getIntensity() {
        return sliderIntensity.getValue();
    }
}

