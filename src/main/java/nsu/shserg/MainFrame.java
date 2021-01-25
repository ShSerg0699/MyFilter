package nsu.shserg;

import nsu.shserg.dialogs.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

public class MainFrame extends JFrame {
    private AboutDialog aboutDialog;
    private final JToolBar toolBar = new JToolBar("Toolbar");
    private final ButtonGroup menuBarGroup = new ButtonGroup();
    private final ButtonGroup toolBarGroup = new ButtonGroup();
    private JPanel statusBar = new JPanel();
    private JLabel statusLabel = new JLabel("My Filter");
    private JFileChooser fileChooser = new JFileChooser();
    private Filters filters;
    private BufferedImage image = null;
    private BufferedImage resultImage = null;
    private  boolean isOriginal;
    private JScrollPane scroll;
    private ImagePanel imagePanel;
    private Point lastPress;

    private SmoothingDialog smoothingDialog = new SmoothingDialog(this);
    private GammaCorrectionDialog gammaCorrectionDialog = new GammaCorrectionDialog(this);
    private BorderDialog borderDialog = new BorderDialog(this);
    private DitheringDialog ditheringDialog = new DitheringDialog(this);
    private RotateDialog rotateDialog = new RotateDialog(this);
    private InterpolationDialog interpolationDialog = new InterpolationDialog(this);
    private WatercolorDialog watercolorDialog = new WatercolorDialog(this);
    private VignetteDialog vignetteDialog = new VignetteDialog(this);

    public MainFrame() {
        super("My Filter");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(640, 480));
        filters = new Filters(this);
        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.add(statusBar, BorderLayout.SOUTH);
        statusBar.setPreferredSize(new Dimension(this.getWidth(), 16));
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusBar.add(statusLabel);
        toolBar.setRollover(true);
        this.add(toolBar, BorderLayout.PAGE_START);
        toolBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                statusLabel.setText(null);
            }
        });

        createImageScrollPane();
        JMenuBar menu = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuView = new JMenu("View");
        JMenu menuHelp = new JMenu("Help");

        JMenuItem itemOpen = createMenuItem("Open...", "Open image", KeyEvent.VK_O, "Open.png", event -> openImage(), false);
        menuFile.add(itemOpen);
        toolBar.add(createToolBarButton(itemOpen, false));

        JMenuItem itemSave = createMenuItem("Save", "Save image", KeyEvent.VK_S, "Save.png", event -> saveImage(), false);
        menuFile.add(itemSave);
        toolBar.add(createToolBarButton(itemSave, false));
        toolBar.addSeparator();

        JMenuItem itemExit = createMenuItem("Exit", "Exit", -1, "Exit.png", event -> System.exit(0), false);
        menuFile.add(itemExit);

        JMenuItem itemAbout = createMenuItem("About", "Show about program", -1, null, event -> openAboutDialog(), false);
        menuHelp.add(itemAbout);

        JMenuItem itemInterpolation = createMenuItem("Interpolation", "Choose interpolation type", KeyEvent.VK_I, "Interpolation.png", event -> openInterpolationDialog(), false);
        menuView.add(itemInterpolation);
        itemInterpolation.setEnabled(false);
        menuBarGroup.add(itemInterpolation);
        AbstractButton toolInterpolation = createToolBarButton(itemInterpolation, false);
        toolBar.add(toolInterpolation);
        toolInterpolation.setEnabled(false);
        toolBarGroup.add(toolInterpolation);

        createNewMenuElement("Rotate", "Rotate image", KeyEvent.VK_0, "Rotate.png", event -> openRotateDialog(), true, menuView);
        createNewMenuElement("Black and White", "Convert a color image to black and white", KeyEvent.VK_1, "BlackAndWhite.png", event -> convertToBlackAndWhite(), true, menuView);
        createNewMenuElement("Negative", "Convert the image to negative", KeyEvent.VK_2, "Negative.png", event -> convertToNegative(), true, menuView);
        createNewMenuElement("Smoothing", "Smoothing filter", KeyEvent.VK_3, "Smoothing.png", event -> openSmoothingDialog(), true, menuView);
        createNewMenuElement("Harshness", "Harshness filter", KeyEvent.VK_4, "Harshness.png", event -> harshness(), true, menuView);
        createNewMenuElement("Emboss", "Emboss filter", KeyEvent.VK_5, "Emboss.png", event -> emboss(), true, menuView);
        createNewMenuElement("Border", "Border selection filter", KeyEvent.VK_6, "Border.png", event -> openBorderDialog(), true, menuView);
        createNewMenuElement("Gamma Correction", "Gamma correction", KeyEvent.VK_7, "GammaCorrection.png", event -> openGammaCorrectionDialog(), true, menuView);
        createNewMenuElement("Dithering", "Dithering", KeyEvent.VK_8, "Dithering.png", event -> openDitheringDialog(), true, menuView);
        createNewMenuElement("Watercolor", "Watercolor filter", KeyEvent.VK_9, "Watercolor.png", event -> openWatercolorDialog(), true, menuView);
        createNewMenuElement("Vignette", "Vignette filter", KeyEvent.VK_W, "Vignette.png", event -> openVignetteDialog(), true, menuView);
        menu.add(menuFile);
        menu.add(menuView);
        menu.add(menuHelp);

        this.setJMenuBar(menu);

        this.pack();
        this.setVisible(true);
    }

    protected void createNewMenuElement(String title, String tooltip, int mnemonic, String icon, ActionListener listener, boolean isRadioButton, JMenu menu) {
        JMenuItem item = createMenuItem(title, tooltip, mnemonic, icon, listener, isRadioButton);
        menu.add(item);
        item.setEnabled(false);
        AbstractButton tool = createToolBarButton(item, true);
        toolBar.add(tool);
        tool.setEnabled(false);
    }

    protected JMenuItem createMenuItem(String title, String tooltip, int mnemonic, String icon, ActionListener listener, boolean isRadioButton) {
        JMenuItem item;
        if (isRadioButton) {
            item = new JRadioButtonMenuItem(title);
            this.menuBarGroup.add(item);
            item.addActionListener(event -> this.setSelectedByGroup(item, toolBarGroup));

        } else item = new JMenuItem(title);
        if (mnemonic != -1) {
            item.setMnemonic(mnemonic);
        }
        item.setToolTipText(tooltip);
        item.setActionCommand(title);
        if (icon != null) {
            item.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource(icon), title));
        }
        item.addActionListener(listener);
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                statusLabel.setText(item.getToolTipText());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                statusLabel.setText(null);
            }
        });
        return item;
    }

    protected AbstractButton createToolBarButton(JMenuItem item, boolean isToggleButton) {
        AbstractButton button;
        if (isToggleButton) {
            button = new JToggleButton(item.getIcon());
            this.toolBarGroup.add(button);
            button.addActionListener(event -> this.setSelectedByGroup(button, menuBarGroup));
        } else button = new JButton(item.getIcon());
        for (ActionListener listener : item.getActionListeners()) {
            button.addActionListener(listener);
        }
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                statusLabel.setText(item.getToolTipText());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                statusLabel.setText(null);
            }
        });
        button.setToolTipText(item.getToolTipText());
        button.setActionCommand(item.getText());
        return button;
    }

    private void setSelectedByGroup(AbstractButton item, ButtonGroup group) {
        Enumeration<AbstractButton> elements = group.getElements();
        while (elements.hasMoreElements()) {
            AbstractButton button = elements.nextElement();
            if (button.getActionCommand().equals(item.getActionCommand())) {
                button.setSelected(true);
            }
        }
    }

    private void createImageScrollPane() {
        scroll = new JScrollPane();
        imagePanel = new ImagePanel(scroll);
        scroll.getViewport().add(imagePanel);

        Border border = BorderFactory.createDashedBorder(Color.BLACK, 5, 5);
        Border indent = BorderFactory.createEmptyBorder(4, 4, 4, 4);
        scroll.setViewportBorder(BorderFactory.createCompoundBorder(indent, border));

        scroll.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (imagePanel.getRealSize() != imagePanel.getScaleOpType()) {
                    imagePanel.setPreferredSize(scroll.getViewport().getSize());
                    revalidate();
                }
            }
        });
        this.add(scroll, BorderLayout.CENTER);

        scroll.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastPress = e.getPoint();
            }

        });
        scroll.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                JScrollBar horizontalScrollBar = scroll.getHorizontalScrollBar();
                JScrollBar verticalScrollBar = scroll.getVerticalScrollBar();

                double sensitivity = 1;
                int horizontalOffset = (int) ((lastPress.x - e.getX()) * sensitivity);
                int verticalOffset = (int) ((lastPress.y - e.getY()) * sensitivity);
                lastPress = e.getPoint();

                horizontalScrollBar.setValue(horizontalScrollBar.getValue() + horizontalOffset);
                verticalScrollBar.setValue(verticalScrollBar.getValue() + verticalOffset);
            }
        });
    }

    private void openImage() {
        int ret = fileChooser.showDialog(this, "Open...");
        if ((ret == JFileChooser.APPROVE_OPTION) && fileChooser.getSelectedFile() != null) {
            File file = fileChooser.getSelectedFile();
            statusLabel.setText(file.getName());
            try {
                BufferedImage image = ImageIO.read(file);
                this.image = image;
                scroll.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if(resultImage != null) {
                            if (isOriginal) {
                                imagePanel.setImage(resultImage);
                                isOriginal = false;
                            } else {
                                imagePanel.setImage(image);
                                isOriginal = true;
                            }
                        }
                    }
                });
                imagePanel.setImage(image);
                Enumeration<AbstractButton> elements = toolBarGroup.getElements();
                while (elements.hasMoreElements()) {
                    AbstractButton button = elements.nextElement();
                    button.setEnabled(true);
                }
                elements = menuBarGroup.getElements();
                while (elements.hasMoreElements()) {
                    AbstractButton button = elements.nextElement();
                    button.setEnabled(true);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    private void saveImage() {
        fileChooser.setDialogTitle("Save as...");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            File file = new File(fileToSave.getAbsolutePath());
            try {
                ImageIO.write(imagePanel.getImage(), "png", file);
            } catch (IOException ex) {
                ex.printStackTrace();

            }
        }
    }

    private void openInterpolationDialog() {
        interpolationDialog.showDialog();
        imagePanel.setInterpolationType(interpolationDialog.getType());
    }

    private void openAboutDialog() {
        if (aboutDialog == null)
            aboutDialog = new AboutDialog(MainFrame.this);
        aboutDialog.setVisible(true);
    }

    private void openRotateDialog() {
        if(imagePanel.getImage().equals(resultImage)){
            return;
        }
        imagePanel.setImage(image);
        int result = rotateDialog.showDialog();
        if (result == 2) {
            return;
        }
        filters.rotateImage(rotateDialog.getAngle());
    }

    private void convertToBlackAndWhite() {
        imagePanel.setImage(image);
        filters.rgbToGray();
    }

    private void convertToNegative() {
        imagePanel.setImage(image);
        filters.inversion();
    }

    private void openSmoothingDialog() {
        imagePanel.setImage(image);
        int result = smoothingDialog.showDialog();
        if (result == 2) {
            return;
        }
        if (smoothingDialog.getMatrixSize() == -1) {
            return;
        }
        filters.smoothing(smoothingDialog.getMatrixSize(), smoothingDialog.getSigma());
    }

    private void harshness() {
        imagePanel.setImage(image);
        filters.harshness();
    }

    private void emboss() {
        imagePanel.setImage(image);
        filters.emboss();
    }

    private void openBorderDialog() {
        imagePanel.setImage(image);
        int result = borderDialog.showDialog();
        if (result == 2) {
            return;
        }
        if (borderDialog.getBorderType()) {
            filters.borderByRobers(borderDialog.getBinarizationValue());
        } else {
            filters.borderBySobel(borderDialog.getBinarizationValue());
        }
    }

    private void openGammaCorrectionDialog() {
        imagePanel.setImage(image);
        int result = gammaCorrectionDialog.showDialog();
        if (result == 2) {
            return;
        }
        filters.gammaCorrection(gammaCorrectionDialog.getP());
    }

    private void openDitheringDialog() {
        imagePanel.setImage(image);
        int result = ditheringDialog.showDialog();
        if (result == 2) {
            return;
        }
        filters.dithering(ditheringDialog.getRedQuantizationNumber(), ditheringDialog.getGreenQuantizationNumber(), ditheringDialog.getBlueQuantizationNumber(), ditheringDialog.getDitheringType());
    }

    private void openWatercolorDialog() {
        imagePanel.setImage(image);
        int result = watercolorDialog.showDialog();
        if (result == 2) {
            return;
        }
        if (watercolorDialog.getMatrixSize() == -1) {
            return;
        }
        filters.watercolor(watercolorDialog.getMatrixSize());
    }

    private void openVignetteDialog() {
        imagePanel.setImage(image);
        int result = vignetteDialog.showDialog();
        if (result == 2) {
            return;
        }
        filters.vignette(vignetteDialog.getIntensity());
    }

    public void setResultImage(BufferedImage image){
        this.resultImage = image;
        isOriginal = false;
    }

    public ImagePanel getImagePanel() {
        return imagePanel;
    }
}
