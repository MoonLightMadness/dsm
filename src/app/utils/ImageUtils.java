package app.utils;

import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.domain.RGB;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName : app.utils.ImageUtils
 * @Description :
 * @Date 2021-08-30 08:56:29
 * @Author ZhangHL
 */
public class ImageUtils {


    private BufferedImage bufferedImage;

    private LogSystem log = LogSystemFactory.getLogSystem();

    public void load(String src) {
        try {
            bufferedImage = ImageIO.read(new File(src));
        } catch (IOException e) {
            log.info("图片读取错误，原因：{}", e);
            e.printStackTrace();
        }
    }


    /**
     * 加权法灰度化（效果较好）
     * 图片灰化（参考：http://www.codeceo.com/article/java-image-gray.html）
     *
     * @return
     * @throws Exception
     */
    public BufferedImage grayImage() {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage grayBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // 计算灰度值
                final int color = bufferedImage.getRGB(x, y);
                final int r = (color >> 16) & 0xff;
                final int g = (color >> 8) & 0xff;
                final int b = color & 0xff;
                int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
//				int gray = (int) (0.21 * r + 0.71 * g + 0.07 * b);
                int newPixel = colorToRGB(255, gray, gray, gray);
                grayBufferedImage.setRGB(x, y, newPixel);
            }
        }
        return grayBufferedImage;
    }


    /**
     * 增亮图片
     *
     * @param increment 增量
     * @return
     * @author zhl
     * @date 2021-08-30 09:11
     * @version V1.0
     */
    public BufferedImage brighten(int increment) {
        //亮度调整，即调整像素的RGB值，0为白，最亮。255为黑，最暗
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage brightenBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final int color = bufferedImage.getRGB(x, y);
                final int r = ((color >> 16) & 0xff) + increment;
                final int g = ((color >> 8) & 0xff) + increment;
                final int b = (color & 0xff) + increment;
                //int newPixel = colorToRGB(255, r, g, b);
                int rgb = ((check(255) & 0xff) << 24) | ((check(r) & 0xff) << 16) | ((check(g) & 0xff) << 8)
                        | ((check(b) & 0xff));
                brightenBufferedImage.setRGB(x, y, rgb);
            }
        }
        return brightenBufferedImage;
    }

    /**
     * 使图片变暗
     *
     * @param increment 增量
     * @return @return {@link BufferedImage }
     * @author zhl
     * @date 2021-08-30 10:06
     * @version V1.0
     */
    public BufferedImage darker(int increment) {
        //亮度调整，即调整像素的RGB值，0为白，最亮。255为黑，最暗
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage darkerBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final int color = bufferedImage.getRGB(x, y);
                final int r = ((color >> 16) & 0xff) - increment;
                final int g = ((color >> 8) & 0xff) - increment;
                final int b = (color & 0xff) - increment;
                //int newPixel = colorToRGB(255, r, g, b);
                int rgb = ((check(255) & 0xff) << 24) | ((check(r) & 0xff) << 16) | ((check(g) & 0xff) << 8)
                        | ((check(b) & 0xff));
                darkerBufferedImage.setRGB(x, y, rgb);
            }
        }
        return darkerBufferedImage;
    }


    /**
     * 幻影坦克
     *
     * @param up   上层图片
     * @param down 下层图片
     * @return @return {@link BufferedImage }
     * @author zhl
     * @date 2021-08-30 10:08
     * @version V1.0
     */
    public BufferedImage phantom(BufferedImage up, BufferedImage down) {
        // 透明度 = 1 - （上层图片灰度值 - 下层图片灰度值）
        // 灰度值 = 下层图片透明度/透明度
        int width = up.getWidth();
        int height = up.getHeight();
        BufferedImage phtomBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int ucolor = up.getRGB(x, y);
                int dcolor = down.getRGB(x, y);
                int ualpha = (ucolor >> 24) & 0xff;
                int dalpha = (dcolor >> 24) & 0xff;
                final int ur = (ucolor >> 16) & 0xff;
                final int ug = (ucolor >> 8) & 0xff;
                final int ub = ucolor & 0xff;
                final int dr = (dcolor >> 16) & 0xff;
                final int dg = (dcolor >> 8) & 0xff;
                final int db = dcolor & 0xff;
                int ugray = (int) (0.3 * ur + 0.59 * ug + 0.11 * ub);
                int dgray = (int) (0.3 * dr + 0.59 * dg + 0.11 * db);
                int nalpha = (ualpha - dalpha) + 255;
                dgray += 50;
                if(dgray<=0){
                    dgray = 1;
                }
                int ngray = (ugray * 255 - 50) / (dgray);
                int newPixel = colorToRGB(nalpha, ngray, ngray, ngray);
                phtomBufferedImage.setRGB(x, y, newPixel);
            }
        }
        return phtomBufferedImage;
    }

    public void save(String path, String formatName, BufferedImage image) {
        try {
            ImageIO.write(image, formatName, new File(path));
        } catch (IOException e) {
            log.info("图片保存错误，原因:{}", e);
            e.printStackTrace();
        }
    }


    private int check(int pixel) {
        if (pixel > 255) {
            return 255;
        }
        if (pixel < 0) {
            return 0;
        }
        return pixel;
    }

    private RGB getRGB(int pixel) {
        int r = (pixel >> 16) & 0xff;
        int g = (pixel >> 8) & 0xff;
        int b = pixel & 0xff;
        RGB rgb = new RGB();
        rgb.setRed(r);
        rgb.setBlue(b);
        rgb.setGreen(g);
        rgb.setColor(pixel);
        return rgb;
    }


    /**
     * 颜色分量转换为RGB值
     *
     * @param alpha
     * @param red
     * @param green
     * @param blue
     * @return
     */
    private int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;
        return newPixel;
    }


}
