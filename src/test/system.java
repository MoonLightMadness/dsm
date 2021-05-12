package test;

import org.junit.Test;

import java.io.IOException;

/**
 * @ClassName : test.system
 * @Description :
 * @Date 2021-05-11 13:56:29
 * @Author ZhangHL
 */
public class system {
    @Test
    public void home(){
        String home;
        home = System.getProperties().getProperty("user.home");
        System.out.println(home);
    }
    @Test
    public void cmd(){
        Process p;
        try {
            p = Runtime.getRuntime().exec(new String[]{"cmd", "/c", "notepad"});
            //p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
