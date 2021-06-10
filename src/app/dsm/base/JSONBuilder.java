package app.dsm.base;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class JSONBuilder {

    private JSONObject obj;

    public JSONBuilder(){
        obj = new JSONObject();
    }

    public JSONBuilder appendProperty(String key, String value){
        obj.put(key,value);
        return this;
    }

    public JSONBuilder appendProperty(Map m){
        obj.putAll(m);
        return this;
    }

    public JSONObject toObject(){
        return obj;
    }

}
