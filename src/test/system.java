package test;

import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
import dsm.utils.SimpleUtils;
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
            p = Runtime.getRuntime().exec(new String[]{"cmd", "/c", "pg"});
            //p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void stacktrace(){

        System.out.println(Thread.currentThread().getStackTrace()[1].getClassName());
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        System.out.println(Thread.currentThread().getStackTrace()[1].getFileName());
        System.out.println(Thread.currentThread().getStackTrace()[1].getLineNumber());
        LogSystem log = LogSystemFactory.getLogSystem();
        log.info("aaa","test");
    }
}
