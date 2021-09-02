package app.dsm.server.handler;

import java.util.Locale;

/**
 * @ClassName : app.dsm.server.handler.HandlerFactory
 * @Description :
 * @Date 2021-09-02 08:58:56
 * @Author ZhangHL
 */
public class HandlerFactory {


    public static Handler getHandler(String name){
        switch (name){
            case "APIG":
                return new APIGHandler();
            case "CONSOLE":
                return  new ConsoleHandler();
            default :
                return new APIGHandler();
        }
    }

}
