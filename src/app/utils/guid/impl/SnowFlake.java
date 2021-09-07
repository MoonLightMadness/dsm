package app.utils.guid.impl;

import app.utils.guid.GuidGenerator;


/**
 * @ClassName : utils.high.guid.impl.SnowFlake
 * @Description :
 * @Date 2021-04-04 08:46:11
 * @Author ZhangHL
 */
public class SnowFlake implements GuidGenerator {
    /**============================================================
     * 一个64位数
     *
     *  第一个部分，是 1 个 bit：0，这个是无意义的。(在二进制中第一位如果为1则其为负数，不符合要求，故固定为0)
     *
     * 第二个部分是 41 个 bit：表示的是时间戳。
     *
     * 第三个部分是 5 个 bit：表示的是机房 id，10001。
     *
     * 第四个部分是 5 个 bit：表示的是机器 id，1 1001。
     *
     * 第五个部分是 12 个 bit：表示的序号，就是某个机房某台机器上这一毫秒内同时生成的 id 的序号，0000 00000000。
     *
     *============================================================*/
    private long guid=0L;
    private long startMillis=1630847188;
    private long currentMillis=0L;
    private volatile long sameRequest=0L;
    private long lastCurrentMillis=0L;

    public SnowFlake() {
        startMillis=System.currentTimeMillis();
    }

    @Override
    public String generateGuid(String... args) throws Exception {
        guid=System.currentTimeMillis()<<22 | Long.parseLong(args[0])<<12 | sameRequest;
        getMillis();
        return String.valueOf(guid);
    }


    private long getMillis() throws InterruptedException {
        long temp=System.currentTimeMillis();
        synchronized (this){
            currentMillis=temp-startMillis;
            if(currentMillis==lastCurrentMillis){
                sameRequest++;
                if(sameRequest>=4096){
                    sameRequest=0;
                    Thread.sleep(1);
                }
            }
            lastCurrentMillis=currentMillis;
        }
        return currentMillis;
    }
}
