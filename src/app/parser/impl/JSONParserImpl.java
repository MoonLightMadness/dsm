package app.parser.impl;

import app.dsm.base.JSONTool;
import app.dsm.exception.ServiceException;
import app.dsm.exception.UniversalErrorCodeEnum;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.parser.JSONParse;

import lombok.SneakyThrows;


import java.lang.reflect.Field;


public class JSONParserImpl implements JSONParse {

    private LogSystem log = LogSystemFactory.getLogSystem();

    @SneakyThrows
    @Override
    public Object parser(byte[] json,Class clazz) {
        try {
            log.info("JSON字符串转换开始，入参{}",new String(json));
            Object obj = JSONTool.getObject(json,clazz);
            return obj;
        }catch (Exception e) {
            log.error("JSON字符串转换失败，原因:{}",e);
            throw new ServiceException(UniversalErrorCodeEnum.UEC_010001.getCode(),UniversalErrorCodeEnum.UEC_010001.getMsg());
        }
    }

    @SneakyThrows
    @Override
    public Object propertiesParser(byte[] data,Class clazz){
        try {
            log.info("JSON字符串属性解析开始,入参{}",new String(data));
            Object obj = clazz.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for(Field field : fields){
                field.setAccessible(true);
                String temp;
                if((temp = JSONTool.getProperty(field.getName(),data))!=null){
                    field.set(obj,temp);
                }
            }
            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("JSON字符串属性解析失败，原因:{}",e);
            throw new ServiceException(UniversalErrorCodeEnum.UEC_010001.getCode(), UniversalErrorCodeEnum.UEC_010001.getMsg());
        }

    }




}
