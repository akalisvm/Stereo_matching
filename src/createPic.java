//19048370 Tianang Chen
//COMP0005 Algorithms Coursework2
//create random dot pictures

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;


public class createPic
{
    private void drawA() throws IOException
    {
        int imageWidth = 512;
        int imageHeight = 512;
        int[][] a = new int [imageWidth][imageHeight];
        Random r = new Random();
        BufferedImage image= new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        for(int i = 0;i < imageHeight;i++)
        {
            for(int j = 0; j < imageHeight;j++)
            {
                a[i][j] = r.nextInt(2);
                if (a[i][j] == 0)
                {
                    g.setColor(Color.BLACK);
                    g.fillRect(i,j,1,1);
                }
                else if(a[i][j] == 1)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect(i,j,1,1);
                }
            }
        }
        System.out.println("A is successful to write.");
        ImageIO.write(image,"png",new File("src\\randomDot\\A.png"));
        g.dispose();
    }

    private void drawB() throws IOException
    {
        int imageWidth = 256;
        int imageHeight = 256;
        int[][] a = new int [imageWidth][imageHeight];
        Random r = new Random();
        BufferedImage image= new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        for(int i = 0;i < imageHeight;i++)
        {
            for(int j = 0; j < imageHeight;j++)
            {
                a[i][j] = r.nextInt(2);
                if (a[i][j] == 0)
                {
                    g.setColor(Color.BLACK);
                    g.fillRect(i,j,1,1);
                }
                else if(a[i][j] == 1)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect(i,j,1,1);
                }
            }
        }
        System.out.println("B is successful to write.");
        ImageIO.write(image,"png",new File("src\\randomDot\\B.png"));
        g.dispose();
    }

    private void drawL() throws IOException {
        File fa = new File("src\\randomDot\\A.png");
        BufferedImage imageA = ImageIO.read(fa);
        File fb = new File("src\\randomDot\\B.png");
        BufferedImage imageB = ImageIO.read(fb);
        Graphics g = imageA.getGraphics();
        g.drawImage(imageB, 124, 128, 256, 256, null);
        g.dispose();
        ImageIO.write(imageA,"png",new File("src\\randomDot\\L.png"));
        System.out.println("L is successful to write.");
    }

    private void drawR() throws IOException{
        File fa = new File("src\\randomDot\\A.png");
        BufferedImage imageA = ImageIO.read(fa);
        File fb = new File("src\\randomDot\\B.png");
        BufferedImage imageB = ImageIO.read(fb);
        Graphics g = imageA.getGraphics();
        g.drawImage(imageB, 132, 128, 256, 256, null);
        g.dispose();
        ImageIO.write(imageA,"png",new File("src\\randomDot\\R.png"));
        System.out.println("R is successful to write.");
    }
    private void drawPic() throws IOException
    {
        drawA();
        drawB();
        drawL();
        drawR();
    }

    public static void main(String[] args) throws IOException
    {
        createPic c = new createPic();
        c.drawPic();
    }
}
