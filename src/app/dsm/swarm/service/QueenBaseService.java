package app.dsm.swarm.service;

import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.annotation.Path;
import app.dsm.server.constant.Indicators;
import app.dsm.server.vo.BaseRspVO;
import app.dsm.swarm.vo.BeeRegisterReqVO;
import app.dsm.swarm.vo.QueenRegisterReqVO;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.parser.impl.JSONParserImpl;
import app.utils.SimpleUtils;
import app.utils.datastructure.ReflectIndicator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName : app.dsm.swarm.service.QueenBaseService
 * @Description :
 * @Date 2021-08-19 08:44:36
 * @Author ZhangHL
 */
@Path("/queen")
public class QueenBaseService {

    private ListenerAdapter listenerAdapter;

    private LogSystem log = LogSystemFactory.getLogSystem();

    @Path("/register")
    public BaseRspVO register(String args){
        BeeRegisterReqVO getIp = (BeeRegisterReqVO) new JSONParserImpl().parser(args.getBytes(StandardCharsets.UTF_8),BeeRegisterReqVO.class);
        QueenRegisterReqVO queenRegisterReqVO = (QueenRegisterReqVO) new JSONParserImpl().parser(args.getBytes(StandardCharsets.UTF_8),QueenRegisterReqVO.class);
        String[] relativePaths = queenRegisterReqVO.getPaths().split("&&");
        for (String relativePath : relativePaths) {
            if(!SimpleUtils.isEmptyString(relativePath)){
                Indicators indicators = listenerAdapter.getSelectorIO().getIndicators();
                ReflectIndicator reflectIndicator = new ReflectIndicator();
                reflectIndicator.setRelativePath(relativePath);
                reflectIndicator.setApproachWay("1");
                reflectIndicator.setApproachIP(getIp.getApproachIP());
                reflectIndicator.setApproachPort(getIp.getApproachPort());
                indicators.add(reflectIndicator);
                System.out.println(reflectIndicator.getApproachIP()+"  "+reflectIndicator.getApproachPort());
            }
        }
        return new BaseRspVO();
    }

    @Path(value = "/invoke")
    public BaseRspVO invoke(String args){
        return null;
    }

}
