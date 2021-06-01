package test;

import app.dsm.log.LogSystem;
import app.dsm.log.LogSystemFactory;
import app.dsm.utils.TimeFormatter;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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
    @Test
    public void uuid(){
        String id = UUID.randomUUID().toString().replace("-","");
        System.out.println(id);
        Date d = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat(TimeFormatter.SEC_LEVEL);
        long time = d.getTime();
        String date=sdf.format(d);
        System.out.println(time+"\n"+date);
    }
}
