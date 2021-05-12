package dsm.log;



/**
 * @ClassName : utils.high.logger.Logger
 * @Description :日志系统--日志类
 * @Date 2021-03-31 19:25:59
 * @Author 张怀栏
 */
public class Logger {

    public LogEntity<String> info(String src, String msg, String... args){
        msg=messageHandler(msg,args);
        return LogEntityWrapper.normal(src,msg);
    }

    public LogEntity<String> ok(String src, String msg, String... args){
        msg=messageHandler(msg,args);
        return LogEntityWrapper.ok(src,msg);
    }

    public LogEntity<String> error(String src, String msg, String... args){
        msg=messageHandler(msg,args);
        return LogEntityWrapper.error(src,msg);
    }


    private String messageHandler(String msg,String... args){
        int argsLen=args.length;
        StringBuilder sb=new StringBuilder();
        if(argsLen>0){
            int pointer=0;
            /*============================================================
             *  算法：
             * 每次循环脱离一对{}
             * 若参数大于占位符数时保留{}
             *============================================================*/
            for (int i = 0; i < argsLen; i++) {
                pointer=msg.indexOf('{');
                if(pointer!=-1){
                    sb.append(msg, 0, pointer);
                    sb.append(args[i]);
                    msg=msg.substring(pointer+2);
                }
            }
            sb.append(msg);
        }else {
            sb.append(msg);
        }
        return sb.toString();
    }
}
