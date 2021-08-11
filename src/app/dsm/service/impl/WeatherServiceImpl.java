package app.dsm.service.impl;

import app.dsm.service.WeatherService;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.listener.IListener;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @ClassName : app.dsm.service.impl.WeatherServiceImpl
 * @Description :
 * @Date 2021-08-11 14:18:44
 * @Author ZhangHL
 */
@Data
public class WeatherServiceImpl implements WeatherService {

    /**
     * 天气
     */
    private String weather = "Sunny";

    private List<IListener> listeners;

    private LogSystem log  = LogSystemFactory.getLogSystem();;

    /**
     * 初始化
     * @return
     * @author zhl
     * @date 2021-08-11 14:26
     * @version V1.0
     */
    public void initialize(){

        listeners = new ArrayList<>();
    }

    /**
     * 改变天气数据
     * @param weather 天气
     * @return
     * @author zhl
     * @date 2021-08-11 14:29
     * @version V1.0
     */
    @Override
    public void changeWeather(String weather){
        this.weather = weather;
        this.invoke();
    }

    /**
     * 订阅服务
     *
     * @param iListener 侦听器
     * @return
     * @author zhl
     * @date 2021-08-11 08:28
     * @version V1.0
     */
    @Override
    public void register(IListener iListener) {
        ListIterator listIterator = listeners.listIterator();
        listIterator.add(iListener);
    }

    /**
     * 取消订阅
     *
     * @param iListener 侦听器
     * @return
     * @author zhl
     * @date 2021-08-11 08:29
     * @version V1.0
     */
    @Override
    public void cancle(IListener iListener) {
        ListIterator iterator = listeners.listIterator();
        while (iterator.hasNext()) {
            if(iterator.next() == iListener){
                iterator.remove();
            }
        }
    }

    /**
     * 通知订阅者
     *
     * @return
     * @author zhl
     * @date 2021-08-11 14:22
     * @version V1.0
     */
    @Override
    public void invoke() {
        ListIterator iterator = listeners.listIterator();
        while (iterator.hasNext()){
            IListener listener = (IListener) iterator.next();
            listener.invoke(weather);
        }
    }
}
