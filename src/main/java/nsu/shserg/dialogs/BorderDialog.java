package nsu.shserg.dialogs;

import nsu.shserg.MainFrame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Hashtable;

public class BorderDialog extends JPanel{
    private static final int MAX_BV = 255;
    private static final int MIN_BV = 0;
    private static final int DEFAULT_BV = 32;
    private boolean isRobers = true;

    private final JRadioButton robers = new JRadioButton("Robers");
    private final JRadioButton sobel = new JRadioButton("Sobel");
    private final JSlider slider = new JSlider(MIN_BV, MAX_BV, DEFAULT_BV);
    private final JSpinner spinner = new JSpinner(new SpinnerNumberModel(DEFAULT_BV, MIN_BV, MAX_BV, 5));
    private final MainFrame frame;

    public BorderDialog(MainFrame frame) {
        this.frame = frame;
        robers.setSelected(true);
        sobel.addActionListener(e -> {
            if (sobel.isSelected()) {
                robers.setSelected(false);
                isRobers = false;
            }
        });
        robers.addActionListener(e -> {
            if (robers.isSelected()) {
                sobel.setSelected(false);
                isRobers = true;
            }
        });

        Hashtable labelTable = new Hashtable();
        labelTable.put(0, new JLabel("0") );
        labelTable.put(64, new JLabel("64") );
        labelTable.put(128, new JLabel("128") );
        labelTable.put(196, new JLabel("196") );
        labelTable.put(255, new JLabel("255") );

        slider.setLabelTable(labelTable);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(64);
        slider.addChangeListener(e -> spinner.setValue(slider.getValue()));
        spinner.addChangeListener(e -> slider.setValue((int) spinner.getValue()));
        spinner.setToolTipText("0 to 255");
        slider.setToolTipText("0 to 255");

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(10));

        JPanel buttonPanel = createBorderTypePanel();
        this.add(buttonPanel);
        this.add(Box.createVerticalStrut(15));

        JPanel panel = createSliderSpinnerPanel(spinner, slider);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.WHITE),
                "Binarization value", TitledBorder.LEADING, TitledBorder.TOP));
        this.add(panel);
        this.add(Box.createVerticalStrut(15));


    }

    private JPanel createBorderTypePanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(robers);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(sobel);
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
        return JOptionPane.showConfirmDialog(frame, this, "Border setting",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    public int getBinarizationValue(){
        return slider.getValue();
    }

    public  boolean getBorderType(){
        return isRobers;
    }
}
