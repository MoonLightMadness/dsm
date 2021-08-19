package test;

import app.dsm.swarm.nest.Bee;
import org.junit.Test;

/**
 * @ClassName : test.swarm
 * @Description :
 * @Date 2021-08-19 13:39:54
 * @Author ZhangHL
 */
public class swarm {


    @Test
    public void test1(){
        Bee queen = new Bee("QUEEN");
        queen.initialize();

        Bee bee = new Bee("worker.bee.1");
        bee.initialize();


        try {
            while (true){
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
