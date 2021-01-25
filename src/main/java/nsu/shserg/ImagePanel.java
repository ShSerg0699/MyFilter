package nsu.shserg;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private JScrollPane scroll;
    private int scaleOpType = AffineTransformOp.TYPE_BILINEAR;
    private final static int REAL_SIZE = 0;

    ImagePanel(JScrollPane scroll) {
        this.scroll = scroll;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        if (scaleOpType == REAL_SIZE) {
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        } else {
            AffineTransform affineTransform = new AffineTransform();
            if (image.getWidth() >= image.getHeight()) {
                double aspectRatio = (double) image.getHeight() / image.getWidth();
                if (scroll.getViewport().getHeight() >= scroll.getViewport().getWidth() * aspectRatio) {
                    affineTransform.scale((double) scroll.getViewport().getWidth() / image.getWidth(), (double) scroll.getViewport().getWidth() * aspectRatio / image.getHeight());
                } else{
                    aspectRatio = (double) image.getWidth() / image.getHeight();
                    affineTransform.scale((double) scroll.getViewport().getHeight() * aspectRatio / image.getWidth(), (double) scroll.getViewport().getHeight() / image.getHeight());
                }
            } else {
                double aspectRatio = (double) image.getWidth() / image.getHeight();
                if (scroll.getViewport().getWidth() >= scroll.getViewport().getHeight() * aspectRatio) {
                    affineTransform.scale((double) scroll.getViewport().getHeight() * aspectRatio / image.getWidth(), (double) scroll.getViewport().getHeight() / image.getHeight());
                } else{
                    aspectRatio = (double) image.getHeight() / image.getWidth();
                    affineTransform.scale((double) scroll.getViewport().getWidth() / image.getWidth(), (double) scroll.getViewport().getWidth() * aspectRatio / image.getHeight());

                }
            }
            AffineTransformOp scaleOp = new AffineTransformOp(affineTransform, scaleOpType);
            g2.drawImage(image, scaleOp, 0, 0);
        }
    }

    public void setInterpolationType(int newType) {
        if (newType == REAL_SIZE && null != image) {
            this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        } else {
            this.setPreferredSize(scroll.getViewport().getSize());
        }

        scaleOpType = newType;
        revalidate();
        repaint();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    public int getScaleOpType() {
        return scaleOpType;
    }

    public int getRealSize() {
        return REAL_SIZE;
    }
}
