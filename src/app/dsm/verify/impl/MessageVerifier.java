package app.dsm.verify.impl;

import app.dsm.base.impl.UniversalEntity;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.dsm.security.Decoder;
import app.dsm.security.Encoder;
import app.dsm.security.SecurityService;
import app.dsm.security.excp.DecodeException;
import app.dsm.security.impl.SecurityServiceImpl;
import app.utils.SimpleUtils;
import app.dsm.verify.Verifier;

import java.nio.charset.StandardCharsets;

/**
 * 消息验证器
 * @ClassName : app.dsm.verify.impl.MessageVerifier
 * @Description :
 * @Date 2021-04-29 19:26:47
 * @Author ZhangHL
 */
public class MessageVerifier implements Verifier {
    /**
     * 安全服务
     */
    private SecurityService ss;

    private LogSystem log;
    /**
     * 先解密，再验证
     */
    public void init(Decoder decoder, Encoder encoder){
        ss=new SecurityServiceImpl();
        ss.init(encoder,decoder);
        log= LogSystemFactory.getLogSystem();
    }


    @Override
    public boolean verify(String text,String key) {
        try {
            //解密
            String decodeText = ss.decode(text);
            //验证
            UniversalEntity u = (UniversalEntity) SimpleUtils.bytesToSerializableObject(decodeText.getBytes(StandardCharsets.UTF_8));
            return ss.verify(u);
        } catch (DecodeException e) {
            log.error(this.getClass().getName(),e.getMessage());
        }
        return false;
    }

    public SecurityService getSs() {
        return ss;
    }

    public void setSs(SecurityService ss) {
        this.ss = ss;
    }
}
