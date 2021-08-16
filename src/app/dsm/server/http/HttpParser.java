package app.dsm.server.http;

import app.dsm.server.domain.HttpEntity;
import org.junit.Test;

/**
 * @ClassName : app.dsm.server.http.HttpParser
 * @Description :
 * @Date 2021-08-16 08:11:24
 * @Author ZhangHL
 */
public class HttpParser {


    public static HttpEntity parse(String data){
        //Data sample:
//        POST /server/gettime HTTP/1.1
//        Content-Type: application/json
//        User-Agent: PostmanRuntime/7.28.3
//        Accept: */*
//        Postman-Token: 8c915b42-544e-4359-a6a9-b12596dd2509
//        Host: 127.0.0.1:10005
//        Accept-Encoding: gzip, deflate, br
//        Connection: keep-alive
//        Content-Length: 10
//
//        {
//
//        }
        HttpEntity entity = new HttpEntity();
        String mode = data.substring(0,data.indexOf(" "));
        String path = data.substring(data.indexOf(" ")+1,data.indexOf(" ",mode.length()+1));
        String body = null;
        //存在无数据的情况
        try {
            data.substring(data.indexOf("{"),data.lastIndexOf("}")+1);
        }catch (Exception e) {
            //e.printStackTrace();
        }
        entity.setMode(mode);
        entity.setRequestPath(path);
        entity.setBody(body);
        return entity;
    }

    @Test
    public void test(){
        String str = "POST /server/gettime HTTP/1.1\n" +
                "Content-Type: application/json\n" +
                "User-Agent: PostmanRuntime/7.28.3\n" +
                "Accept: */*\n" +
                "Postman-Token: 8c915b42-544e-4359-a6a9-b12596dd2509\n" +
                "Host: 127.0.0.1:10005\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 10\n" +
                "\n" +
                "{\n" +
                "    \n" +
                "}";
        HttpEntity entity = HttpParser.parse(str);
        System.out.println(entity.getMode());
        System.out.println(entity.getRequestPath());
        System.out.println(entity.getBody());
    }

}
