package nsu.shserg.dialogs;

import nsu.shserg.MainFrame;

import javax.swing.*;
import java.awt.image.AffineTransformOp;

public class InterpolationDialog extends JPanel {
    private final JRadioButton realSize = new JRadioButton("Real size");
    private final JRadioButton bilinear = new JRadioButton("Bilinear interpolation");
    private final JRadioButton neighbours = new JRadioButton("Nearest neighbor interpolation");
    private final JRadioButton bicubic = new JRadioButton("Bicubic interpolation");
    private final MainFrame frame;
    private int type = 1;

    public InterpolationDialog(MainFrame frame) {
        this.frame = frame;
        bilinear.setSelected(true);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(realSize);
        this.add(Box.createVerticalStrut(10));
        this.add(bilinear);
        this.add(Box.createVerticalStrut(10));
        this.add(neighbours);
        this.add(Box.createVerticalStrut(10));
        this.add(bicubic);
        realSize.addActionListener(e -> {
            if (realSize.isSelected()) {
                bilinear.setSelected(false);
                neighbours.setSelected(false);
                bicubic.setSelected(false);
                type = 0;
            }
        });
        neighbours.addActionListener(e -> {
            if (neighbours.isSelected()) {
                realSize.setSelected(false);
                bilinear.setSelected(false);
                bicubic.setSelected(false);
                type = AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
            }
        });
        bilinear.addActionListener(e -> {
            if (bilinear.isSelected()) {
                realSize.setSelected(false);
                neighbours.setSelected(false);
                bicubic.setSelected(false);
                type = AffineTransformOp.TYPE_BILINEAR;
            }
        });
        bicubic.addActionListener(e -> {
            if (bicubic.isSelected()) {
                realSize.setSelected(false);
                bilinear.setSelected(false);
                neighbours.setSelected(false);
                type = AffineTransformOp.TYPE_BICUBIC;
            }
        });


    }

    public int showDialog() {
        return JOptionPane.showConfirmDialog(frame, this, "Interpolation setting",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    public int getType() {
        return type;
    }
}
