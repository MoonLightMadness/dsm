package app.dsm.swarm.service;

import app.dsm.base.JSONTool;
import app.dsm.config.Configer;
import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.annotation.Path;
import app.dsm.server.http.HttpRequestBuilder;
import app.dsm.server.http.HttpResponseBuilder;
import app.dsm.swarm.vo.BeeRegisterReqVO;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.parser.impl.JSONParserImpl;
import app.utils.datastructure.ReflectIndicator;
import app.utils.net.Sender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ListIterator;

/**
 * @ClassName : app.dsm.swarm.service.BeeBaseService
 * @Description :
 * @Date 2021-08-19 13:31:29
 * @Author ZhangHL
 */
@Path(value = "/bee")
public class BeeBaseService {

    private ListenerAdapter listenerAdapter;

    private LogSystem log = LogSystemFactory.getLogSystem();

    @Path(value = "/register")
    public void register(String args){
        BeeRegisterReqVO getIp = (BeeRegisterReqVO) new JSONParserImpl().parser(args.getBytes(StandardCharsets.UTF_8),BeeRegisterReqVO.class);
        Configer configer = new Configer();
        String ip = configer.readConfig("queen.ip");
        String port = configer.readConfig("queen.port");
        ListIterator<ReflectIndicator> iterator = listenerAdapter.getSelectorIO().getIndicators().getIterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()){
            ReflectIndicator reflectIndicator = iterator.next();
            sb.append(reflectIndicator.getRelativePath()).append("&&");
        }
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        BeeRegisterReqVO beeRegisterReqVO = new BeeRegisterReqVO();
        beeRegisterReqVO.setPaths(sb.toString());
        beeRegisterReqVO.setCharacter("Bee");
        beeRegisterReqVO.setApproachIP(getIp.getApproachIP());
        beeRegisterReqVO.setApproachPort(getIp.getApproachPort());
        requestBuilder.setData(new String(JSONTool.toJson(beeRegisterReqVO)));
        requestBuilder.setRequestPath("/queen/register");
        try {
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(ip,Integer.parseInt(port)));
            socketChannel.configureBlocking(false);
            Sender.send(socketChannel, requestBuilder.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
