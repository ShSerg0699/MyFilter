package nsu.shserg;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;

public class Filters {
    private MainFrame frame;

    public Filters(MainFrame frame) {
        this.frame = frame;
    }

    public void rotateImage(int angle) {
        BufferedImage imageIn = frame.getImagePanel().getImage();
        BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), imageIn.getType());
        double radians = angle * Math.PI / 180.0;
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        Point center = new Point(imageOut.getWidth() / 2, imageOut.getHeight() / 2);
        for (int y = 0; y < imageOut.getHeight(); ++y) {
            for (int x = 0; x < imageOut.getWidth(); ++x) {
                int srcX = center.x + (int) ((x - center.x) * cos + (y - center.y) * sin);
                int srcY = center.y + (int) (-(x - center.x) * sin + (y - center.y) * cos);
                if (srcX < 0 || srcX >= imageIn.getWidth() || srcY < 0 || srcY >= imageIn.getHeight()) {
                    imageOut.setRGB(x, y, Color.WHITE.getRGB());
                    continue;
                }
                int color = imageIn.getRGB(srcX, srcY);
                imageOut.setRGB(x, y, color);
            }
        }
        frame.getImagePanel().setImage(imageOut);
        frame.setResultImage(imageOut);
    }

    public void rgbToGray() {
        BufferedImage imageIn = frame.getImagePanel().getImage();
        BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imageIn.getHeight(); y++) {
            for (int x = 0; x < imageIn.getWidth(); x++) {
                int rgb = imageIn.getRGB(x, y);
                int red = (int) (getRed(rgb) * 0.299);
                int green = (int) (getGreen(rgb) * 0.587);
                int blue = (int) (getBlue(rgb) * 0.114);
                imageOut.setRGB(x, y, ((red + green + blue) << 16 | (red + green + blue) << 8 | (red + green + blue)));
            }
        }
        frame.getImagePanel().setImage(imageOut);
        frame.setResultImage(imageOut);
    }

    public void vignette(int intensity) {
        float max_intensity = 50;
        BufferedImage imageIn = frame.getImagePanel().getImage();
        BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imageIn.getHeight(); y++) {
            for (int x = 0; x < imageIn.getWidth(); x++) {
                double l_x = Math.abs(imageIn.getWidth() / 2.0 - x);
                double l_y = Math.abs(imageIn.getHeight() / 2.0 - y);

                int rgb = imageIn.getRGB(x, y);
                int red = getRed(rgb);
                int green = getGreen(rgb);
                int blue = getBlue(rgb);
                double koef_a = -Math.log(1 - l_x / (imageIn.getWidth()));
                double koef_b = -Math.log(1 - l_y / (imageIn.getHeight()));
                double koef = ((max_intensity - intensity * (koef_a + koef_b) / 2) / max_intensity);
                red *= koef;
                green *= koef;
                blue *= koef;
                red = checkBoundaryValues(red);
                green = checkBoundaryValues(green);
                blue = checkBoundaryValues(blue);
                imageOut.setRGB(x, y, (red << 16 | green << 8 | blue));
            }
        }
        frame.getImagePanel().setImage(imageOut);
        frame.setResultImage(imageOut);
    }

    public void inversion() {
        BufferedImage imageIn = frame.getImagePanel().getImage();
        BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imageIn.getHeight(); y++) {
            for (int x = 0; x < imageIn.getWidth(); x++) {
                int rgb = imageIn.getRGB(x, y);
                int red = getRed(rgb);
                int green = getGreen(rgb);
                int blue = getBlue(rgb);
                imageOut.setRGB(x, y, ((255 - red) << 16 | (255 - green) << 8 | (255 - blue)));
            }
        }
        frame.getImagePanel().setImage(imageOut);
        frame.setResultImage(imageOut);
    }

    public void gammaCorrection(double p) {
        BufferedImage imageIn = frame.getImagePanel().getImage();
        BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imageIn.getHeight(); y++) {
            for (int x = 0; x < imageIn.getWidth(); x++) {
                int rgb = imageIn.getRGB(x, y);
                int red = getRed(rgb);
                int green = getGreen(rgb);
                int blue = getBlue(rgb);
                red = checkBoundaryValues((int) Math.round(255 * Math.pow(red / 255.0, 1.0 / p)));
                green = checkBoundaryValues((int) Math.round(255 * Math.pow(green / 255.0, 1.0 / p)));
                blue = checkBoundaryValues((int) Math.round(255 * Math.pow(blue / 255.0, 1.0 / p)));
                imageOut.setRGB(x, y, (red << 16 | green << 8 | blue));
            }
        }
        frame.getImagePanel().setImage(imageOut);
        frame.setResultImage(imageOut);
    }

    public void borderByRobers(int binarizationValue) {
        BufferedImage imageIn = frame.getImagePanel().getImage();
        BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imageIn.getHeight(); y++) {
            for (int x = 0; x < imageIn.getWidth(); x++) {
                int newX = x + 1, newY = y + 1;
                if (newX >= imageIn.getWidth()) newX = imageIn.getWidth() - 1;
                if (newY >= imageIn.getHeight()) newY = imageIn.getHeight() - 1;
                int rgb0 = imageIn.getRGB(x, y);
                int rgb1 = imageIn.getRGB(newX, y);
                int rgb2 = imageIn.getRGB(x, newY);
                int rgb3 = imageIn.getRGB(newX, newY);
                int red0 = getRed(rgb0);
                int red1 = getRed(rgb1);
                int red2 = getRed(rgb2);
                int red3 = getRed(rgb3);
                int red = Math.abs(red0 - red3) + Math.abs(red1 - red2);
                if (red > binarizationValue) {
                    red = 255;
                } else {
                    red = 0;
                }
                int green0 = getGreen(rgb0);
                int green1 = getGreen(rgb1);
                int green2 = getGreen(rgb2);
                int green3 = getGreen(rgb3);
                int green = Math.abs(green0 - green3) + Math.abs(green1 - green2);
                if (green > binarizationValue) {
                    green = 255;
                } else {
                    green = 0;
                }
                int blue0 = getBlue(rgb0);
                int blue1 = getBlue(rgb1);
                int blue2 = getBlue(rgb2);
                int blue3 = getBlue(rgb3);
                int blue = Math.abs(blue0 - blue3) + Math.abs(blue1 - blue2);
                if (blue > binarizationValue) {
                    blue = 255;
                } else {
                    blue = 0;
                }
                red = checkBoundaryValues(red);
                green = checkBoundaryValues(green);
                blue = checkBoundaryValues(blue);
                imageOut.setRGB(x, y, red << 16 | green << 8 | blue);
            }
        }
        frame.getImagePanel().setImage(imageOut);
        frame.setResultImage(imageOut);
    }

    public void borderBySobel(int binarizationValue) {
        BufferedImage imageIn = frame.getImagePanel().getImage();
        BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imageIn.getHeight(); y++) {
            for (int x = 0; x < imageIn.getWidth(); x++) {
                int pX = x + 1, pY = y + 1, mX = x - 1, mY = y - 1;
                if (pX >= imageIn.getWidth()) pX = imageIn.getWidth() - 1;
                if (pY >= imageIn.getHeight()) pY = imageIn.getHeight() - 1;
                if (mX < 0) mX = 0;
                if (mY < 0) mY = 0;
                int a = imageIn.getRGB(mX, mY);
                int b = imageIn.getRGB(x, mY);
                int c = imageIn.getRGB(pX, mY);
                int d = imageIn.getRGB(mX, y);
                int f = imageIn.getRGB(pX, y);
                int g = imageIn.getRGB(mX, pY);
                int h = imageIn.getRGB(x, pY);
                int i = imageIn.getRGB(pX, pY);
                int reda = getRed(a);
                int redb = getRed(b);
                int redc = getRed(c);
                int redd = getRed(d);
                int redf = getRed(f);
                int redg = getRed(g);
                int redh = getRed(h);
                int redi = getRed(i);

                int red = Math.abs((redc + 2 * redf + redi) - (reda + 2 * redd + redg)) + Math.abs((redg + 2 * redh + redi) - (reda + 2 * redb + redc));
                if (red > binarizationValue) {
                    red = 255;
                } else {
                    red = 0;
                }

                int greena = getGreen(a);
                int greenb = getGreen(b);
                int greenc = getGreen(c);
                int greend = getGreen(d);
                int greenf = getGreen(f);
                int greeng = getGreen(g);
                int greenh = getGreen(h);
                int greeni = getGreen(i);
                int green = Math.abs((greenc + 2 * greenf + greeni) - (greena + 2 * greend + greeng)) + Math.abs((greeng + 2 * greenh + greeni) - (greena + 2 * greenb + greenc));
                if (green > binarizationValue) {
                    green = 255;
                } else {
                    green = 0;
                }
                int bluea = getBlue(a);
                int blueb = getBlue(b);
                int bluec = getBlue(c);
                int blued = getBlue(d);
                int bluef = getBlue(f);
                int blueg = getBlue(g);
                int blueh = getBlue(h);
                int bluei = getBlue(i);
                int blue = Math.abs((bluec + 2 * bluef + bluei) - (bluea + 2 * blued + blueg)) + Math.abs((blueg + 2 * blueh + bluei) - (bluea + 2 * blueb + bluec));
                if (blue > binarizationValue) {
                    blue = 255;
                } else {
                    blue = 0;
                }
                red = checkBoundaryValues(red);
                green = checkBoundaryValues(green);
                blue = checkBoundaryValues(blue);
                imageOut.setRGB(x, y, red << 16 | green << 8 | blue);
            }
        }
        frame.getImagePanel().setImage(imageOut);
        frame.setResultImage(imageOut);
    }

    public void smoothing(int n, double sigma) {
        n /= 2;
        float sum = 0;
        double[][] G = new double[2 * n + 1][2 * n + 1];
        for (int y = -n, i = 0; i < 2 * n + 1; y++, i++) {
            for (int x = -n, j = 0; j < 2 * n + 1; x++, j++) {
                G[i][j] = (1 / (2 * Math.PI * sigma * sigma)) * Math.pow(Math.E, -(double) (x * x + y * y) / (2 * sigma * sigma));
                sum += G[i][j];
            }
        }
        BufferedImage imageIn = frame.getImagePanel().getImage();
        BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imageIn.getHeight(); y++) {
            for (int x = 0; x < imageIn.getWidth(); x++) {
                float red = 0, green = 0, blue = 0;
                for (int u = -n, i = 0; i < 2 * n + 1; u++, i++) {
                    for (int v = -n, j = 0; j < 2 * n + 1; v++, j++) {
                        int newX = x + u;
                        if (newX < 0) newX = 0;
                        else if (newX >= imageIn.getWidth()) newX = imageIn.getWidth() - 1;
                        int newY = y + v;
                        if (newY < 0) newY = 0;
                        else if (newY >= imageIn.getHeight()) newY = imageIn.getHeight() - 1;
                        int rgb = imageIn.getRGB(newX, newY);
                        red += G[i][j] * getRed(rgb);
                        green += G[i][j] * getGreen(rgb);
                        blue += G[i][j] * getBlue(rgb);
                    }
                }
                red /= sum;
                green /= sum;
                blue /= sum;
                red = checkBoundaryValues(red);
                green = checkBoundaryValues(green);
                blue = checkBoundaryValues(blue);
                imageOut.setRGB(x, y, (Math.round(red) << 16 | Math.round(green) << 8 | Math.round(blue)));
            }
        }
        frame.getImagePanel().setImage(imageOut);
        frame.setResultImage(imageOut);
    }

    public void harshness() {
        int[][] H = {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}};
        int n = 1;
        BufferedImage imageIn = frame.getImagePanel().getImage();
        BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imageIn.getHeight(); y++) {
            for (int x = 0; x < imageIn.getWidth(); x++) {
                int red = 0, green = 0, blue = 0;
                for (int u = -n, i = 0; i < 2 * n + 1; u++, i++) {
                    for (int v = -n, j = 0; j < 2 * n + 1; v++, j++) {
                        int newX = x + u;
                        if (newX < 0) newX = 0;
                        else if (newX >= imageIn.getWidth()) newX = imageIn.getWidth() - 1;
                        int newY = y + v;
                        if (newY < 0) newY = 0;
                        else if (newY >= imageIn.getHeight()) newY = imageIn.getHeight() - 1;
                        int rgb = imageIn.getRGB(newX, newY);
                        red += H[i][j] * getRed(rgb);
                        green += H[i][j] * getGreen(rgb);
                        blue += H[i][j] * getBlue(rgb);
                    }
                }
                red = checkBoundaryValues(red);
                green = checkBoundaryValues(green);
                blue = checkBoundaryValues(blue);
                imageOut.setRGB(x, y, (red << 16 | green << 8 | blue));
            }
        }
        frame.getImagePanel().setImage(imageOut);
        frame.setResultImage(imageOut);
    }

    public void watercolor(int n) {
        n /= 2;
        BufferedImage imageIn = frame.getImagePanel().getImage();
        BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imageIn.getHeight(); y++) {
            for (int x = 0; x < imageIn.getWidth(); x++) {
                int count = 0;
                int[] listRed = new int[(2 * n + 1) * (2 * n + 1)];
                int[] listGreen = new int[(2 * n + 1) * (2 * n + 1)];
                int[] listBlue = new int[(2 * n + 1) * (2 * n + 1)];
                Arrays.fill(listRed, 0);
                Arrays.fill(listGreen, 0);
                Arrays.fill(listBlue, 0);
                for (int u = -n, i = 0; i < 2 * n + 1; u++, i++) {
                    for (int v = -n, j = 0; j < 2 * n + 1; v++, j++) {
                        int newX = x + u;
                        if (newX < 0) newX = 0;
                        else if (newX >= imageIn.getWidth()) newX = imageIn.getWidth() - 1;
                        int newY = y + v;
                        if (newY < 0) newY = 0;
                        else if (newY >= imageIn.getHeight()) newY = imageIn.getHeight() - 1;
                        listRed[count] = getRed(imageIn.getRGB(newX, newY));
                        listGreen[count] = getGreen(imageIn.getRGB(newX, newY));
                        listBlue[count] = getBlue(imageIn.getRGB(newX, newY));
                        count++;
                    }
                }
                Arrays.sort(listRed, 0, count);
                Arrays.sort(listGreen, 0, count);
                Arrays.sort(listBlue, 0, count);

                int red = listRed[count / 2];
                int green = listGreen[count / 2];
                int blue = listBlue[count / 2];
                imageOut.setRGB(x, y, (red << 16 | green << 8 | blue));
            }
        }
        frame.getImagePanel().setImage(imageOut);
        harshness();
    }

    public void emboss() {
        int[][] H = {{0, 1, 0}, {-1, 0, 1}, {0, -1, 0}};
        int n = 1;
        BufferedImage imageIn = frame.getImagePanel().getImage();
        BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imageIn.getHeight(); y++) {
            for (int x = 0; x < imageIn.getWidth(); x++) {
                int red = 0, green = 0, blue = 0;
                for (int u = -n, i = 0; i < 2 * n + 1; u++, i++) {
                    for (int v = -n, j = 0; j < 2 * n + 1; v++, j++) {
                        int newX = x + u;
                        if (newX < 0) newX = 0;
                        else if (newX >= imageIn.getWidth()) newX = imageIn.getWidth() - 1;
                        int newY = y + v;
                        if (newY < 0) newY = 0;
                        else if (newY >= imageIn.getHeight()) newY = imageIn.getHeight() - 1;
                        int rgb = imageIn.getRGB(newX, newY);
                        red += H[i][j] * getRed(rgb);
                        green += H[i][j] * getGreen(rgb);
                        blue += H[i][j] * getBlue(rgb);
                    }
                }
                red += 128;
                green += 128;
                blue += 128;
                red = checkBoundaryValues(red);
                green = checkBoundaryValues(green);
                blue = checkBoundaryValues(blue);
                imageOut.setRGB(x, y, (red << 16 | green << 8 | blue));
            }
        }
        frame.getImagePanel().setImage(imageOut);
        frame.setResultImage(imageOut);
    }

    public void dithering(int redQuantizationNumber, int greenQuantizationNumber, int blueQuantizationNumber, boolean isFloydSteinberg) {
        BufferedImage imageIn = frame.getImagePanel().getImage();
        BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_INT_RGB);
        int[][] componentRed = new int[imageIn.getHeight()][imageIn.getWidth()];
        int[][] componentGreen = new int[imageIn.getHeight()][imageIn.getWidth()];
        int[][] componentBlue = new int[imageIn.getHeight()][imageIn.getWidth()];
        for (int y = 0; y < imageIn.getHeight(); y++) {
            for (int x = 0; x < imageIn.getWidth(); x++) {
                componentRed[y][x] = getRed(imageIn.getRGB(x, y));
                componentGreen[y][x] = getGreen(imageIn.getRGB(x, y));
                componentBlue[y][x] = getBlue(imageIn.getRGB(x, y));
            }
        }
        if (isFloydSteinberg) {
            componentRed = componentFloydSteinbergDithering(componentRed, redQuantizationNumber - 1);
            componentGreen = componentFloydSteinbergDithering(componentGreen, greenQuantizationNumber - 1);
            componentBlue = componentFloydSteinbergDithering(componentBlue, blueQuantizationNumber - 1);
        } else {
            componentRed = componentOrderedDithering(componentRed, redQuantizationNumber);
            componentGreen = componentOrderedDithering(componentGreen, greenQuantizationNumber);
            componentBlue = componentOrderedDithering(componentBlue, blueQuantizationNumber);
        }
        for (int y = 0; y < imageIn.getHeight(); y++) {
            for (int x = 0; x < imageIn.getWidth(); x++) {
                imageOut.setRGB(x, y, (checkBoundaryValues(componentRed[y][x]) << 16 | checkBoundaryValues(componentGreen[y][x]) << 8 | checkBoundaryValues(componentBlue[y][x])));
            }
        }
        frame.getImagePanel().setImage(imageOut);
        frame.setResultImage(imageOut);
    }

    private int[][] componentFloydSteinbergDithering(int[][] component, int quantizationNumber) {
        for (int i = 0; i < component.length; i++) {
            for (int j = 0; j < component[i].length; j++) {
                int newValue = Math.round(Math.round(1.0f * quantizationNumber / 255 * component[i][j]) * 255.0f / quantizationNumber);
                int error = component[i][j] - newValue;
                component[i][j] = newValue;
                if (j + 1 < component[i].length) {
                    component[i][j + 1] += error * 7 / 16;
                }
                if (i + 1 < component.length) {
                    if (j - 1 >= 0) {
                        component[i + 1][j - 1] += error * 3 / 16;
                    }
                    if (j + 1 < component[i].length) {
                        component[i + 1][j + 1] += error / 16;
                    }
                    component[i + 1][j] += error * 5 / 16;
                }

            }
        }
        return component;
    }

    private int[][] componentOrderedDithering(int[][] component, int quantizationNumber) {
        HashMap<Integer, Integer> colors = new HashMap<>();
        int step = 255 / (quantizationNumber - 1);
        for (int i = 0, value = 0; i <= 255; i++) {
            if (i == value + step) {
                value += step;
            }
            colors.put(i, value);
        }
        int n;
        int[][] matrix = {{0, 2}, {3, 1}};
        if (quantizationNumber <= 4) {
            n = 16;
        } else if (quantizationNumber <= 16) {
            n = 8;
        } else if (quantizationNumber <= 64) {
            n = 4;
        } else {
            n = 2;
        }
        matrix = matrixBuild(matrix, 2, n);
        if (n < 16) {
            int v = 256 / (n * n);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] *= v;
                }
            }
        }
        for (int i = 0; i < component.length; i++) {
            for (int j = 0; j < component[i].length; j++) {
                if (component[i][j] > matrix[i % n][j % n]) {
                    component[i][j] = colors.get(component[i][j]) + step;
                } else {
                    component[i][j] = colors.get(component[i][j]);
                }
            }
        }
        return component;
    }

    private int[][] matrixBuild(int[][] matrix, int n, int needN) {
        int[][] newMatrix = new int[n * 2][n * 2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newMatrix[i][j] = 4 * matrix[i][j];
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = n; j < n * 2; j++) {
                newMatrix[i][j] = 4 * matrix[i][j - n] + 2;
            }
        }
        for (int i = n; i < n * 2; i++) {
            for (int j = 0; j < n; j++) {
                newMatrix[i][j] = 4 * matrix[i - n][j] + 3;
            }
        }
        for (int i = n; i < n * 2; i++) {
            for (int j = n; j < n * 2; j++) {
                newMatrix[i][j] = 4 * matrix[i - n][j - n] + 1;
            }
        }
        n *= 2;
        if (n < needN) {
            return matrixBuild(newMatrix, n, needN);
        } else {
            return newMatrix;
        }
    }

    private int checkBoundaryValues(int c) {
        if (c < 0) {
            return 0;
        }
        if (c > 255) {
            return 255;
        }
        return c;
    }

    private float checkBoundaryValues(float c) {
        if (c < 0) {
            return 0;
        }
        if (c > 255) {
            return 255;
        }
        return c;
    }

    private int getRed(int rgb) {
        return (rgb >> 16) & 0x000000FF;
    }

    private int getGreen(int rgb) {
        return (rgb >> 8) & 0x000000FF;
    }

    private int getBlue(int rgb) {
        return (rgb) & 0x000000FF;
    }
}
