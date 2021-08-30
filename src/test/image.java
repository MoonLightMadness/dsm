package test;

import app.utils.ImageUtils;
import org.junit.Test;

import java.awt.image.BufferedImage;

/**
 * @ClassName : test.image
 * @Description :
 * @Date 2021-08-30 09:23:05
 * @Author ZhangHL
 */
public class image {

    @Test
    public void test1(){
        ImageUtils up = new ImageUtils();
        ImageUtils down = new ImageUtils();
        up.load("C:\\Users\\Administrator\\Pictures\\1620823640366.jpg");
        down.load("C:\\Users\\Administrator\\Pictures\\1620823640376.jpg");
        BufferedImage brighten = up.brighten(0);
        BufferedImage darker = down.darker(0);
        BufferedImage mix = up.phantom(brighten,darker);
        up.save("./test.png","png",mix);
//        BufferedImage brigten = imageUtils.brighten(50);
//        imageUtils.save("./test.jpg","jpg",brigten);
//        BufferedImage grey = imageUtils.grayImage();
//        imageUtils.save("./test.jpg","jpg",grey);

    }
}
