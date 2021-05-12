package dsm.verify.impl;

import dsm.base.BaseEntity;
import dsm.base.impl.UniversalEntity;
import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
import dsm.security.Decoder;
import dsm.security.Encoder;
import dsm.security.SecurityService;
import dsm.security.excp.DecodeException;
import dsm.security.impl.SecurityServiceImpl;
import dsm.utils.SimpleUtils;
import dsm.verify.Verifier;

import java.nio.charset.StandardCharsets;

/**
 * 消息验证器
 * @ClassName : dsm.verify.impl.MessageVerifier
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
