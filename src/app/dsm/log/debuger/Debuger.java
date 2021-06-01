package app.dsm.log.debuger;

/**
 * @ClassName : app.dsm.log.debuger.Debuger
 * @Description :
 * @Date 2021-05-09 14:48:44
 * @Author ZhangHL
 */
public class Debuger {

    private volatile static StringBuilder debug;

    public static void addMessage(String msg, String... args) {
        if (debug == null) {
            debug = new StringBuilder();
        }
        debug.append(messageHandler(msg, args)).append("\n");
    }

    public static String getDebug() {
        return debug.toString();
    }

    private static String messageHandler(String msg, String... args) {
        int argsLen = args.length;
        StringBuilder sb = new StringBuilder();
        if (argsLen > 0) {
            int pointer = 0;
            /*============================================================
             *  算法：
             * 每次循环脱离一对{}
             * 若参数大于占位符数时保留{}
             *============================================================*/
            for (int i = 0; i < argsLen; i++) {
                pointer = msg.indexOf('{');
                if (pointer != -1) {
                    sb.append(msg, 0, pointer);
                    sb.append(args[i]);
                    msg = msg.substring(pointer + 2);
                }
            }
            sb.append(msg);
        } else {
            sb.append(msg);
        }
        return sb.toString();
    }
}
