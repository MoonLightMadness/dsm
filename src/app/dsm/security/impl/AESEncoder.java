package app.dsm.security.impl;

import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.dsm.security.Encoder;
import app.dsm.security.excp.EncodeException;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @ClassName : utils.high.security.impl.AESEncoder
 * @Description :
 * @Date 2021-04-03 17:16:09
 * @Author ZhangHL
 */
public class AESEncoder implements Encoder {
    private LogSystem log;

    public void init(){
        log= LogSystemFactory.getLogSystem();
    }


    @Override
    public String encode(String text,String sKey) {
        try {
            if (sKey == null) {
                throw new EncodeException("key不能为空");
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                throw new EncodeException("Key长度不是16位");
            }
            byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | EncodeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
