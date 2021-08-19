package app.dsm.server.http;

import java.time.LocalDateTime;

/**
 * @ClassName : app.dsm.server.http.HttpResponseBuilder
 * @Description :
 * @Date 2021-08-17 08:45:04
 * @Author ZhangHL
 */
public class HttpResponseBuilder {
//    POST / HTTP/1.1
//    Content-Type: application/json
//    User-Agent: PostmanRuntime/7.28.1
//    Accept: */*
//Postman-Token: 79577fc3-5568-4d1b-9afd-e4d7cb7a46e2
//Host: 127.0.0.1:9004
//Accept-Encoding: gzip, deflate, br
//Connection: keep-alive
//Content-Length: 36
//
//{
//
//    "path":"/server/gettime"
//}

    /**
     * 协议类型
     * 默认HTTP/1.1
     */
    private String protocol = "HTTP/1.1";

    /**
     * 状态码
     */
    private String code = "200";

    /**
     * 状态信息
     */
    private String msg;

    /**
     * 内容格式
     */
    private String contentType = "Application/json";

    /**
     * 日期
     */
    private String date;

    /**
     * 连接方式
     */
    private String connection = "Close";

    /**
     * 数据
     */
    private String data;

    /**
     * 服务器名
     */
    private String server;

    /**
     * 服务器ip
     */
    private String host;



    public HttpResponseBuilder setCode(String code){
        this.code = code;
        return this;
    }

    public HttpResponseBuilder setContentType(String contentType){
        this.contentType = contentType;
        return this;
    }

    public HttpResponseBuilder setConnection(String connection){
        this.connection = connection;
        return this;
    }

    public HttpResponseBuilder setData(String data){
        this.data = data.substring(1,data.length()-1);
        this.data = this.data.replace("\\","");
        return this;
    }

    public HttpResponseBuilder setServer(String server){
        this.server = server;
        return this;
    }

    public HttpResponseBuilder setHost(String host){
        this.host = host;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(protocol).append(" ").append(code).append("\n");
        sb.append("Content-Type: ").append(contentType).append("\n");
        sb.append("Connection: ").append(connection).append("\n");
        sb.append("Date: ").append(LocalDateTime.now()).append("\n");
        sb.append("Server: ").append(server).append("\n");
        sb.append("Host: ").append(host).append("\n");
        sb.append("\n").append("{\n");
        sb.append(data).append("\n}");
        return sb.toString();
    }

}
