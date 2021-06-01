package test;

import app.dsm.log.LogSystem;
import app.dsm.log.LogSystemFactory;
import org.junit.Test;

/**
 * @ClassName : test.log
 * @Description :
 * @Date 2021-05-01 21:06:45
 * @Author ZhangHL
 */
public class log {
    @Test
    public void logTest1(){
        LogSystem log= LogSystemFactory.getLogSystem();
        log.immediatelySaveMode(true);
        log.info(this.getClass().getName(),"this is a test");
    }
}
