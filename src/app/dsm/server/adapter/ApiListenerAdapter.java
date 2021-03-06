package app.dsm.server.adapter;

import app.dsm.base.JSONTool;
import app.dsm.config.Configer;
import app.dsm.db.impl.SqliteImpl;
import app.dsm.server.authority.AuthSystem;
import app.dsm.server.domain.BasePath;
import app.dsm.server.domain.MessagePacket;
import app.dsm.server.domain.UserAuthData;
import app.dsm.server.http.HttpResponseBuilder;
import app.dsm.server.trigger.PathTrigger;
import app.dsm.server.vo.NoPowerBaseRspVO;
import app.dsm.server.vo.ResponseRspVO;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.SimpleUtils;
import app.utils.datastructure.ReflectIndicator;
import app.utils.listener.ThreadListener;
import app.utils.net.Sender;
import lombok.Data;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ListIterator;

@Data
public class ApiListenerAdapter implements ThreadListener {


    private PathTrigger pathTrigger;

    private Object result;

    private ListenerAdapter listenerAdapter;

    private LogSystem log = LogSystemFactory.getLogSystem();

    private Configer configer;

    public void initialize() {
        configer = new Configer();
        pathTrigger = listenerAdapter.getSelectorIO().getPathTrigger();
    }

    @Override
    public void invoke(Object obj, String... args) {
        MessagePacket messagePacket = listenerAdapter.getMessagePacket();
        byte[] data = messagePacket.getData();
        BasePath basePath = (BasePath) SimpleUtils.parseTo(data, BasePath.class);
        //如果未给response字段，默认不触发方法
        if(!canTrigger(basePath)){
            noTriggerResponse();
            return;
        }
        UserAuthData userAuthData = (UserAuthData) SimpleUtils.parseTo(data, UserAuthData.class);
        //检查是否具有权限字段
        checkAuthority(userAuthData);
        ListIterator<ReflectIndicator> iterator = listenerAdapter.getSelectorIO().getIndicators().getIterator();
        while (iterator.hasNext()){
            ReflectIndicator indicator = iterator.next();
            if(indicator.getRelativePath().equals(basePath.getPath())){
                if(AuthSystem.judge(indicator.getAuthority(),userAuthData.getAuthLevel())){
                    result = pathTrigger.trigger(basePath.getPath(), new String(data), listenerAdapter,indicator);
                    if (result != null) {
                        response(result);
                        return;
                    }
                }else {
                    log.info("权限不足");
                    response(new NoPowerBaseRspVO());
                    return;
                }
                return;
            }
        }

    }

    private boolean canTrigger(BasePath basePath){
        if(null == basePath.getResponse() || "0".equals(basePath.getResponse())){
            log.info("不响应且不触发方法数据,{}",basePath);
            return false;
        }
        return true;
    }

    private void checkAuthority(UserAuthData userAuthData){
        if(null != userAuthData.getUserId()||null !=userAuthData.getUserPassword()){
            SqliteImpl sqliteImpl = new SqliteImpl();
            sqliteImpl.initialize(configer.readConfig("server.db.name"));
            String command = configer.readConfig("get.auth.level", userAuthData.getUserId(), userAuthData.getUserPassword());
            userAuthData.setAuthLevel((String) sqliteImpl.get(command));
        }else {
            userAuthData.setAuthLevel("NORMAL");
        }
    }

    private void noTriggerResponse(){
        ResponseRspVO responseRspVO = new ResponseRspVO();
        responseRspVO.setResponse("000000");
        responseRspVO.setMsg("不响应且不触发方法");
        response(responseRspVO);
    }

    /**
     * 返回消息
     *
     * @param obj obj
     * @return
     * @author zhl
     * @date 2021-08-14 02:03
     * @version V1.0
     */
    private void response(Object obj) {
        SocketChannel socketChannel = listenerAdapter.getChannel();
        String ret = getResponse(JSONTool.toJson(obj));
        log.info("返回数据:{}到{}", ret, socketChannel);
        Sender.send(socketChannel, ret.getBytes(StandardCharsets.UTF_8));
    }


    private String getResponse(byte[] data) {
        Configer configer = new Configer();
        HttpResponseBuilder httpBuilder = new HttpResponseBuilder();
        httpBuilder.setCode("200").setServer("DSMServer/1.0")
                .setHost(configer.readConfig("ip") + " " + configer.readConfig("port"));
        httpBuilder.setData(new String(data));
        return httpBuilder.toString();
    }

    @Override
    public void setArgs(Object obj, String... args) {

    }
}
