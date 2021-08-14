package app.dsm.server.filter;

import app.dsm.config.Configer;
import app.log.LogSystem;
import app.log.LogSystemFactory;

import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * @ClassName : app.dsm.server.filter.Filter
 * @Description :
 * @Date 2021-08-14 13:59:26
 * @Author ZhangHL
 */
public class Filter {


    private LogSystem log = LogSystemFactory.getLogSystem();
    /**
     * 监测请求连接的ip是否能过通过过滤器
     * @param channel
     * @return can pass -> true,if not -> false
     * @author zhl
     * @date 2021-08-14 13:59
     * @version V1.0
     */
    public boolean canPass(SocketChannel channel){
        try {
            List<String> black = new Configer().readConfigList("bk.ip");
            String cip = channel.getRemoteAddress().toString();
            String ip = cip.substring(1,cip.indexOf(':'));
            for (String bip : black){
                if(bip.equals(ip)){
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            log.error("获取Socket的ip失败，原因:{}",e);
        }
        return false;
    }

}
