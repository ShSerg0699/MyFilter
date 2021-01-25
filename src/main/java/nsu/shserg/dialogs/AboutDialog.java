package nsu.shserg.dialogs;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {
    public AboutDialog(JFrame owner) {
        super(owner, "About My Filter", true);
        add(new JLabel("<html><h1><i>  My Filter</i></h1>"
                        + "Application for processing raster images."
                        + "The graphical interface of the program consists of a menu, a toolbar and an area for viewing the image."
                        + "<br><br>The user has the ability to open image files (PNG, JPEG, BMP, GIF)."
                        + "After loading, the selected image is displayed in the viewing area in bilinear interpolation mode."
                        + "The transition to the real size mode (i.e. pixel to pixel) can be done in the Interpolation setting dialog box."
                        + "The toolbar contains buttons for opening and saving a file, changing the display mode and all implemented image processing tools."
                        + "When clicking on any processing tool, the user is prompted in the dialog box to set the parameters of this tool (if any), then the processing result is displayed instead of the original image."
                        + "When you click on the corresponding menu items (Original), the displayed image changes to the original image. Each subsequent use of the processing tools is applied to the original image."
                        + "The user has the opportunity to save the current image processing result to a PNG file."
                        + "<br><br>The following processing tools are implemented:"
                        + "<br>• Translation of a color image in black and white (shades of gray)."
                        + "<br>• Convert the image to negative (inverse)."
                        + "<br>• Smoothing filter on the window from 3 × 3 to 11 × 11 at the choice of the user (according to Gauss)."
                        + "<br>• Sharpening filter."
                        + "<br>• Embossing."
                        + "<br>• Gamma correction. Limit the gamma parameter value from 0.1 to 10."
                        + "<br>• Boundary filters (Roberts and Sobel operators). The binarization parameter is selected."
                        + "<br>• Dithering by the Floyd-Steinberg algorithm. Have a choice of quantization numbers for each color (red, blue, and green). 2 to 128"
                        + "<br>• Orderly dithering. Have a choice of quantization numbers for each color (red, blue, and green). From 2 to 128. The matrix size should be determined automatically."
                        + "<br>• Watercolorization."
                        + "<br>• Vignette."
                        + "<br>• Rotate the image at an arbitrary angle. Limit the value of the parameter from -180 to +180 degrees. Rotation is performed relative to the center of the image."
                        + "<hr>By Shniakin Sergei, FIT NSU 17204<br>"
                        + "Build on March 3, 2020</html>"),
                BorderLayout.CENTER);

        JButton ok = new JButton("ok");
        ok.addActionListener(e -> setVisible(false));

        JPanel panel = new JPanel();
        panel.add(ok);
        add(panel, BorderLayout.SOUTH);
        setSize(550, 650);
    }
}