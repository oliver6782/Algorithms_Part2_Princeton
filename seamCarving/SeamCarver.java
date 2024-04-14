import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture pictureCopy;
    private int height, width;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("null argument");
        this.pictureCopy = new Picture(picture);
        this.height = picture.height();
        this.width = picture.width();
    }

    // current picture
    public Picture picture() {
        return new Picture(pictureCopy);
    }

    // width of current picture
    public int width() {
        return pictureCopy.width();
    }

    // height of current picture
    public int height() {
        return pictureCopy.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x >= width || x < 0 || y >= height || y < 0)
            throw new IllegalArgumentException("invalid argument");

        if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
            return 1000.0;
        }
        else {
            int rgbUp = pictureCopy.getRGB(x, y - 1);
            int rUp = (rgbUp >> 16) & 0xFF;
            int gUp = (rgbUp >> 8) & 0xFF;
            int bUp = (rgbUp) & 0xFF;

            int rgbDown = pictureCopy.getRGB(x, y + 1);
            int rDown = (rgbDown >> 16) & 0xFF;
            int gDown = (rgbDown >> 8) & 0xFF;
            int bDown = (rgbDown) & 0xFF;

            int rgbLeft = pictureCopy.getRGB(x - 1, y);
            int rLeft = (rgbLeft >> 16) & 0xFF;
            int gLeft = (rgbLeft >> 8) & 0xFF;
            int bLeft = (rgbLeft) & 0xFF;

            int rgbRight = pictureCopy.getRGB(x + 1, y);
            int rRight = (rgbRight >> 16) & 0xFF;
            int gRight = (rgbRight >> 8) & 0xFF;
            int bRight = (rgbRight) & 0xFF;

            double rx = rLeft - rRight;
            double gx = gLeft - gRight;
            double bx = bLeft - bRight;
            double sqDeltaX = rx * rx + gx * gx + bx * bx;

            double ry = rUp - rDown;
            double gy = gUp - gDown;
            double by = bUp - bDown;
            double sqDeltaY = ry * ry + gy * gy + by * by;

            return Math.sqrt(sqDeltaX + sqDeltaY);
        }
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energy = new double[width][height];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                energy[col][row] = energy(col, row);
            }
        }
        int[] seam = new int[height];
        double[][] distTo = new double[width][height];
        int[][] edgeTo = new int[width][height];
        for (int col = 0; col < width; col++) {
            distTo[col][0] = energy[col][0];
            if (height > 1) {
                for (int row = 1; row < height; row++) {
                    distTo[col][row] = Double.POSITIVE_INFINITY;
                }
            }

        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                for (int i = -1; i <= 1; i++) {
                    if (col + i >= 0 && col + i < width && row > 0) {
                        if (distTo[col][row] > distTo[col + i][row - 1] + energy[col][row]) {
                            distTo[col][row] = distTo[col + i][row - 1] + energy[col][row];
                            edgeTo[col][row] = col + i;
                        }
                    }
                }
            }
        }

        double shortestPath = Double.POSITIVE_INFINITY;
        int endCol = 0;
        for (int i = 0; i < width; i++) {
            if (distTo[i][height - 1] < shortestPath) {
                shortestPath = distTo[i][height - 1];
                endCol = i;
            }
        }

        seam[height - 1] = endCol;
        if (height > 1) {
            for (int h = height - 2; h >= 0; h--) {
                seam[h] = edgeTo[seam[h + 1]][h + 1];
            }
        }
        return seam;
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("null argument");
        if (seam.length != width)
            throw new IllegalArgumentException("length should equal to picture width");

        validateSeam(seam);
        for (int s : seam) {
            if (s < 0 || s >= height) throw new IllegalArgumentException("seam out of bound");
        }
        transpose();
        removeVerticalSeam(seam);
        transpose();

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("null argument");
        if (seam.length != height)
            throw new IllegalArgumentException("length should equal to picture height");

        validateSeam(seam);
        for (int s : seam) {
            if (s < 0 || s >= width) throw new IllegalArgumentException("seam out of bound");
        }

        Picture newPicture = new Picture(width - 1, height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width - 1; col++) {
                if (col < seam[row]) {
                    newPicture.setRGB(col, row, pictureCopy.getRGB(col, row));
                }
                else {
                    newPicture.setRGB(col, row, pictureCopy.getRGB(col + 1, row));
                }
            }
        }
        pictureCopy = new Picture(newPicture);
        width--;

    }

    private void validateSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1)
                throw new IllegalArgumentException("invalid argument");
        }
    } // make sure that the neighboring seam indices are within 1 variance.


    private void transpose() {
        Picture transposedPicture = new Picture(height(), width());

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                transposedPicture.setRGB(col, row, pictureCopy.getRGB(row, col));
            }
        }
        pictureCopy = new Picture(transposedPicture);

        height = transposedPicture.height();
        width = transposedPicture.width();

    } // transpose a picture and the corresponding energy array


}
