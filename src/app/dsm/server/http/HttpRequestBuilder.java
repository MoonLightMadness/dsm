package app.dsm.server.http;

import java.lang.reflect.Modifier;

/**
 * @ClassName : app.dsm.server.http.HttpRequestBuilder
 * @Description :
 * @Date 2021-08-16 16:33:22
 * @Author ZhangHL
 */
public class HttpRequestBuilder {

    /**
     * 协议
     */
    private String protocol = "HTTP/1.1";

    /**
     * 模式
     */
    private String mode = "POST";

    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 数据
     */
    private String data;


    public HttpRequestBuilder setMode(String mode){
        this.mode = mode;
        return this;
    }

    public HttpRequestBuilder setProtocol(String protocol){
        this.protocol = protocol;
        return this;
    }

    public HttpRequestBuilder setRequestPath(String path){
        this.requestPath=path;
        return this;
    }

    public HttpRequestBuilder setData(String data){
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(mode).append(" ").append(requestPath).append(" ").append(protocol).append("\n");
        sb.append("\n");
        sb.append(data);
        return sb.toString();
    }

}
