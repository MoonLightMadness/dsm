package app.log;



/**
 * @ClassName : utils.high.logger.Logger
 * @Description :日志系统--日志类
 * @Date 2021-03-31 19:25:59
 * @Author ZhangHL
 */
public class Logger {

    private int level = 3;

    public LogEntity info(String msg, Object... args){
        msg=messageHandler(msg,args);
        String className=Thread.currentThread().getStackTrace()[level].getClassName();
        String methodName=Thread.currentThread().getStackTrace()[level].getMethodName();
        String fileName=Thread.currentThread().getStackTrace()[level].getFileName();
        int line=Thread.currentThread().getStackTrace()[level].getLineNumber();
        return LogEntityWrapper.normal(fileName+"("+className+"."+methodName+"."+line+")",msg);
    }

    public LogEntity ok(String msg, Object... args){
        msg=messageHandler(msg,args);
        String className=Thread.currentThread().getStackTrace()[level].getClassName();
        String methodName=Thread.currentThread().getStackTrace()[level].getMethodName();
        String fileName=Thread.currentThread().getStackTrace()[level].getFileName();
        int line=Thread.currentThread().getStackTrace()[level].getLineNumber();
        return LogEntityWrapper.ok(fileName+"("+className+"."+methodName+"."+line+")",msg);
    }

    public LogEntity error(String msg, Object... args){
        msg=messageHandler(msg,args);
        String className=Thread.currentThread().getStackTrace()[level].getClassName();
        String methodName=Thread.currentThread().getStackTrace()[level].getMethodName();
        String fileName=Thread.currentThread().getStackTrace()[level].getFileName();
        int line=Thread.currentThread().getStackTrace()[level].getLineNumber();
        return LogEntityWrapper.error(fileName+"("+className+"."+methodName+"."+line+")",msg);
    }


    private String messageHandler(String msg,Object... args){
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
                    if(args[i] instanceof Exception){
                        sb.append(getStackTrace((Exception) args[i]));
                    }else {
                        sb.append(args[i].toString());
                    }
                    msg=msg.substring(pointer+2);
                }
            }
            sb.append(msg);
        }else {
            sb.append(msg);
        }
        return sb.toString();
    }

    private String getStackTrace(Exception e){
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString()).append("\n");
        for(StackTraceElement element : e.getStackTrace()){
            sb.append("\tat ").append(element.toString()).append("\n");
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

    private String messageHandler2(String msg,String... args) {
        int pointer = 0;
        int counter = 0;
        while (pointer == -1){
            pointer = msg.indexOf('{');
            msg.replaceFirst("\\{",args[counter]);
            msg.replaceFirst("\\|","");
            counter++;
        }
        return msg;
    }
}
