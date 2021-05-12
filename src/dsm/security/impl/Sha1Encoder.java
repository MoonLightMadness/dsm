package dsm.security.impl;

import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
import dsm.security.Encoder;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName : utils.high.security.impl.Sha1Encoder
 * @Description :
 * @Date 2021-04-03 17:09:09
 * @Author 张怀栏
 */
public class Sha1Encoder implements Encoder {
    private LogSystem log;

    public void init(){
        log= LogSystemFactory.getLogSystem();
    }
    /**
     * 不需要提供key
     * @class
     * @Param
     * @return
     * @Author Zhang huai lan
     * @Date 17:18 2021/4/3
     * @Version V1.0
     **/
    @Override
    public String encode(String text,String key) {
        try {
            MessageDigest md=MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes(StandardCharsets.UTF_8));
            return new String(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
