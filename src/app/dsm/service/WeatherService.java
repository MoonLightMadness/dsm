package app.dsm.service;

import app.utils.listener.IRegister;

public interface WeatherService extends IRegister {

    void changeWeather(String weather);


}
