package test;

import app.dsm.base.JSONTool;
import app.dsm.config.Configer;
import app.dsm.server.SelectorIO;
import app.dsm.server.Server;
import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.container.ServerContainer;
import app.dsm.server.container.ServerEntity;
import app.dsm.server.domain.BasePath;
import app.dsm.server.impl.SelectorIOImpl;
import app.dsm.server.impl.ServerImpl;
import app.dsm.server.vo.CalculatorReqVO;
import app.dsm.server.vo.GetTimeRspVO;
import app.dsm.server.vo.GetUserInfoReqVO;
import app.dsm.server.vo.GetUserInfoRspVO;
import app.utils.SimpleUtils;
import app.utils.listener.IListener;
import app.utils.listener.ThreadListener;
import app.utils.net.Sender;
import app.utils.special.RTimer;
import lombok.Data;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ListIterator;
public class server {



    @Test
    public void test2(){
//        RTimer rTimer = new RTimer();
//        rTimer.start();
        Configer configer = new Configer();

        Server server = new ServerImpl();
        server.initialize(configer.readConfig("queen.ip"),configer.readConfig("queen.port"));
        server.open();


        try {

            while (true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test3() {
        GetUserInfoReqVO getUserInfoReqVO = new GetUserInfoReqVO();
        GetUserInfoRspVO getUserInfoRspVO = new GetUserInfoRspVO();
        getUserInfoReqVO.setUserId("123");
        RTimer rTimer = new RTimer();
        rTimer.start();
        SimpleUtils.copyProperties(getUserInfoReqVO,getUserInfoRspVO);
        System.out.println(rTimer.end());
        System.out.println(getUserInfoRspVO.getUserId());
    }
    //平方根倒数速算
    public static float invSqrt(float x) {
        float xhalf = 0.5f * x;

        int i = Float.floatToIntBits(x);

        i = 0x5f3759df - (i >> 1);

        x = Float.intBitsToFloat(i);

        //第一次牛顿迭代
        x *= (1.5f - xhalf * x * x);


        return x;

    }

    @Test
    public void copyTest(){
        CalculatorReqVO calculatorReqVO = new CalculatorReqVO();
        calculatorReqVO.setX("10");
        calculatorReqVO.setY("100");
        RTimer rTimer = new RTimer();
        rTimer.start();
        CalculatorReqVO c1 = (CalculatorReqVO) SimpleUtils.duplicate(calculatorReqVO);
        System.out.println(rTimer.end());
        c1.setX("99");
        System.out.println(c1.getX());
        System.out.println(calculatorReqVO.getX());
    }
}

class testClass implements ThreadListener {

    private Object obj;

    private String[] args;
    @Override
    public void invoke(Object obj, String... args) {
        ListenerAdapter listenerAdapter = (ListenerAdapter) obj;
        byte[] data = listenerAdapter.getMessagePacket().getData();
        if(JSONTool.getProperty("option",data) .equals("setname")){
            SelectorIOImpl selectorIO = listenerAdapter.getSelectorIO();
            ServerContainer container = selectorIO.getServerContainer();
            List<ServerEntity> serverEntities = container.getServers();
            ListIterator<ServerEntity> iterator = serverEntities.listIterator();
            while (iterator.hasNext()){
                ServerEntity serverEntity = iterator.next();
                if(serverEntity.getSocketChannel() == listenerAdapter.getChannel()){
                    serverEntity.setName(JSONTool.getProperty("attachment",data));
                }
            }
        }
        System.out.println(new String(data));
    }

    @Override
    public void setArgs(Object obj, String... args) {
        this.obj = obj;
        this.args = args;
    }


}
@Data
class Pojo{
    private String option;

    private String attachment;
}

@Data
class ApiPojo extends BasePath {

    private String name;




}
