package test;

import app.dsm.base.start.ProjectStarter;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import org.junit.Test;

/**
 * @ClassName : test.starter
 * @Description :
 * @Date 2021-05-11 14:33:29
 * @Author ZhangHL
 */
public class starter {

    @Test
    public void test(){
        ProjectStarter starter =new ProjectStarter();
        starter.start();
    }

    @Test
    public void test2(){
        LogSystem log = LogSystemFactory.getLogSystem();
        log.info(null,"{}test{}test{}","1","2","3");
    }
}
