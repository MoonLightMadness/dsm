package app.log;



/**
 * @ClassName : utils.high.logger.Logger
 * @Description :日志系统--日志类
 * @Date 2021-03-31 19:25:59
 * @Author ZhangHL
 */
public class Logger {

    public LogEntity<String> info(String msg, Object... args){
        msg=messageHandler2(msg,args);
        String className=Thread.currentThread().getStackTrace()[3].getClassName();
        String methodName=Thread.currentThread().getStackTrace()[3].getMethodName();
        String fileName=Thread.currentThread().getStackTrace()[3].getFileName();
        int line=Thread.currentThread().getStackTrace()[3].getLineNumber();
        return LogEntityWrapper.normal(fileName+"("+className+"."+methodName+"."+line+")",msg);
    }

    public LogEntity<String> ok(String msg, Object... args){
        msg=messageHandler2(msg,args);
        String className=Thread.currentThread().getStackTrace()[3].getClassName();
        String methodName=Thread.currentThread().getStackTrace()[3].getMethodName();
        String fileName=Thread.currentThread().getStackTrace()[3].getFileName();
        int line=Thread.currentThread().getStackTrace()[3].getLineNumber();
        return LogEntityWrapper.ok(fileName+"("+className+"."+methodName+"."+line+")",msg);
    }

    public LogEntity<String> error(String msg, Object... args){
        msg=messageHandler2(msg,args);
        String className=Thread.currentThread().getStackTrace()[3].getClassName();
        String methodName=Thread.currentThread().getStackTrace()[3].getMethodName();
        String fileName=Thread.currentThread().getStackTrace()[3].getFileName();
        int line=Thread.currentThread().getStackTrace()[3].getLineNumber();
        return LogEntityWrapper.error(fileName+"("+className+"."+methodName+"."+line+")",msg);
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


    private String messageHandler2(String msg,Object... args){
        int pointer = 0,counter=0;
        while (pointer != -1 && counter < args.length){
            pointer = msg.indexOf("{");
            msg = msg.replaceFirst("\\{",args[counter++].toString());
            msg = msg.replaceFirst("\\}","");
        }
        return msg;
    }
}
