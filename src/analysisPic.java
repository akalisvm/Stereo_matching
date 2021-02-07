//19048370 Tianang Chen
//COMP0005 Algorithms Coursework2
//Stereo program JAVA source code

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

public class analysisPic {
    private static void getPicData(String path1, String path2) {
        try {
            BufferedImage image = ImageIO.read(new File(path1));
            File file = new File(path2);
            int[][] data = new int[image.getHeight()][image.getWidth()];
            int[] rgb = new int[3];
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    int pixel1 = image.getRGB(j, i);
                    rgb[0] = (pixel1 & 0xff0000) >> 16;
                    rgb[1] = (pixel1 & 0xff00) >> 8;
                    rgb[2] = (pixel1 & 0xff);
                    int gray = (int) (rgb[0] * 0.3 + rgb[1] * 0.59 + rgb[2] * 0.11);
                    data[i][j] = gray;
                }
            }
            FileWriter out = new FileWriter(file);
            int n = image.getHeight();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    out.write(data[i][j] + "\t");
                }
                out.write("\r\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void matrix(String path1, String path2, String path3, String path4) throws IOException {
        BufferedImage image = ImageIO.read(new File(path3));
        int[][] read1 = new int[image.getHeight()][image.getWidth()];
        int[][] read2 = new int[image.getHeight()][image.getWidth()];
        File file1 = new File(path1);
        File file2 = new File(path2);
        BufferedReader in1 = new BufferedReader(new FileReader(file1));
        BufferedReader in2 = new BufferedReader(new FileReader(file2));
        String line1;
        int row1 = 0;
        while ((line1 = in1.readLine()) != null) {
            String[] temp = line1.split("\t");
            for (int j = 0; j < temp.length; j++) {
                read1[row1][j] = (int) Double.parseDouble(temp[j]);
            }
            row1++;
        }
        in1.close();
        String line2;
        int row2 = 0;
        while ((line2 = in2.readLine()) != null) {
            String[] temp = line2.split("\t");
            for (int j = 0; j < temp.length; j++) {
                read2[row2][j] = (int) Double.parseDouble(temp[j]);
            }
            row2++;
        }
        in2.close();
        int[][] temp = new int[image.getHeight()][image.getHeight()];
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                temp[i][j] = read2[j][i];
            }
        }

        BufferedImage map = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = map.getGraphics();

        for(int k = 0; k < image.getHeight(); k++) {
            int n = image.getHeight() + 2;
            double[][] forward = new double[n][n];
            for(int i = 0; i < n;i++) {
                for(int j = 0;j < n;j++) {
                    if(i == 0) {
                        if(j == 0 || j == 1) { forward[i][j] = 0; }
                        if(j >= 2) { forward[i][j] = read1[k][j-2]; }
                    }
                    if(j == 0) {
                        if(i == 1){ forward[i][j] = 0; }
                        if(i >= 2){ forward[i][j] = temp[i-2][k]; }
                    }
                }
            }

            for(int i = 0; i < n;i++) {
                for(int j = 0; j < n;j++) {
                    if(i == 1) {
                        for(j = 1; j < n;j++){
                            int resultIntJ = (j - 1) * 38;
                            double resultJ = (double) resultIntJ / 10;
                            forward[i][j] = resultJ;
                        }
                    }
                }
            }

            for(int i = 0; i < n;i++) {
                for(int j = 0; j < n;j++) {
                    if(j == 1) {
                        for(i = 1; i < n; i++) {
                            int resultIntI = (i - 1) * 38;
                            double resultI = (double) resultIntI / 10;
                            forward[i][j] = resultI;
                        }
                    }
                }
            }

            int[][] backward = new int[image.getHeight()+1][image.getHeight()+1];

            for(int i = 0; i< n-1;i++) {
                for(int j = 0; j < n-1;j++) {
                    if(i == 0) { for(j = 0; j < n-1; j++) { backward[i][j] = 0; } }
                }
            }

            for(int i = 0; i< n-1;i++) {
                for(int j = 0; j < n-1;j++) {
                    if(j == 0) {
                        for(i = 0; i < n-1; i++) { backward[i][j] = 0; } }
                }
            }

            for(int i = 2; i < n; i++){
                for(int j = 2; j < n; j++){
                    double z1 = forward[0][j];
                    double z2 = forward[i][0];
                    double z = (z1 + z2) / 2;
                    double matchingCost = (z - z1) * (z - z2) / 16;
                    double absCost = Math.abs(matchingCost);
                    int resultInt1 = (int) ((forward[i-1][j]) * 10) + 38;
                    double result1 = (double) resultInt1 / 10;
                    int resultInt2 = (int) ((forward[i][j-1]) * 10) + 38;
                    double result2 = (double) resultInt2 / 10;
                    double min1 = forward[i-1][j-1] + absCost;
                    double min2;
                    min2 = result1;
                    double min3;
                    min3 = result2;
                    double min = Math.min(Math.min(min1,min2),min3);
                    forward[i][j] = min;
                    if(min1 == min) { backward[i-1][j-1] = 1; }
                    else if(min2 == min) { backward[i-1][j-1] = 2; }
                    else if(min3 == min) { backward[i-1][j-1] = 3; }
                }
            }

            int[][] sResult = new int[image.getHeight()][image.getHeight()];

            for(int i = 0; i < n-2;i++) {
                for(int j = 0; j < n-2; j++) { sResult[i][j] = 0; }
            }

            int p = n-2;
            int q = n-2;
            while (backward[p][q] != 0) {
                if (backward[p][q] == 1) {
                    sResult[p-1][q-1] = 1;
                    p--;
                    q--;
                } else if (backward[p][q] == 2) {
                    p--;
                } else if (backward[p][q] == 3) {
                    q--;
                }
            }

            int[] iResult = new int[image.getHeight()];
            for(int i = 0; i < n-2;i++) {
                for(int j = 0;j < n-2;j++) {
                    if(sResult[i][j] == 1) {
                        iResult[i] = Math.abs(i - j) + 128;
                    }
                }
            }

            for(int i = 0; i < n-2; i++) {
                g.setColor(new Color(iResult[i], iResult[i], iResult[i]));
                g.fillRect(i, k, 1, 1);
            }
        }
        ImageIO.write(map, "png", new File(path4));
        g.dispose();
    }

    public static void main(String[] args) throws IOException {
        getPicData("src\\randomDot\\L.png", "src\\randomDot\\L.txt");
        getPicData("src\\randomDot\\R.png", "src\\randomDot\\R.txt");
        matrix("src\\randomDot\\L.txt", "src\\randomDot\\R.txt", "src\\randomDot\\L.png", "src\\randomDot\\disparity.png");
        getPicData("src\\Pair 1\\view1.png", "src\\Pair 1\\view1.txt");
        getPicData("src\\Pair 1\\view2.png", "src\\Pair 1\\view2.txt");
        matrix("src\\Pair 1\\view1.txt", "src\\Pair 1\\view2.txt", "src\\Pair 1\\view1.png", "src\\Pair 1\\disparity.png");
        getPicData("src\\Pair 2\\view1.png", "src\\Pair 2\\view1.txt");
        getPicData("src\\Pair 2\\view2.png", "src\\Pair 2\\view2.txt");
        matrix("src\\Pair 2\\view1.txt", "src\\Pair 2\\view2.txt", "src\\Pair 2\\view1.png", "src\\Pair 2\\disparity.png");
        getPicData("src\\Pair 3\\view1.png", "src\\Pair 3\\view1.txt");
        getPicData("src\\Pair 3\\view2.png", "src\\Pair 3\\view2.txt");
        matrix("src\\Pair 3\\view1.txt", "src\\Pair 3\\view2.txt", "src\\Pair 3\\view1.png", "src\\Pair 3\\disparity.png");
    }
}