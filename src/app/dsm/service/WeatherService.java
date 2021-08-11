package app.dsm.service;

import app.utils.listener.IRegister;

public interface WeatherService extends IRegister {

    /**
     * 改变天气数据
     * @param weather 天气
     * @return
     * @author zhl
     * @date 2021-08-11 14:29
     * @version V1.0
     */
    void changeWeather(String weather);


}
