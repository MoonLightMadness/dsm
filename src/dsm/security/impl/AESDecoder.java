package dsm.security.impl;

import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
import dsm.security.Decoder;
import dsm.security.excp.DecodeException;
import dsm.security.excp.EncodeException;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @ClassName : utils.high.security.impl.AESDecoder
 * @Description :
 * @Date 2021-04-03 17:22:14
 * @Author ZhangHL
 */
public class AESDecoder implements Decoder {
    private LogSystem log;

    public void init(){
        log= LogSystemFactory.getLogSystem();
        log.immediatelySaveMode(true);
    }
    @Override
    public String decode(String text, String sKey) {

        try {
            if (sKey == null) {
                throw new DecodeException("key不能为空");
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                throw new DecodeException("Key长度不是16位");
            }
            byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.getDecoder().decode(text);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, StandardCharsets.UTF_8);
                //return originalString.getBytes(StandardCharsets.UTF_8);
                return originalString;
            } catch (Exception e) {
                log.error(this.getClass().getName(),e.toString());
            }
        } catch (Exception ex) {
            log.error(this.getClass().getName(),ex.toString());
        }
        return null;
    }

}
