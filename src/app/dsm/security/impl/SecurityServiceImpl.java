package app.dsm.security.impl;

import app.dsm.base.impl.UniversalEntity;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.dsm.security.Decoder;
import app.dsm.security.Encoder;
import app.dsm.security.SecurityService;
import app.dsm.security.excp.EncodeException;
import app.utils.special.DBUtils;


/**
 * @ClassName : utils.high.security.impl.SercurityServiceImpl
 * @Description :
 * @Date 2021-04-17 08:02:16
 * @Author ZhangHL
 */
public class SecurityServiceImpl implements SecurityService {

    private Encoder encoder;

    private Decoder decoder;

    private LogSystem log;


    /**
     * 密钥
     */
    private String accessKey;

    /**
     * 进行hash校验
     *
     * @return
     * @class
     * @Param
     * @Author Zhang huai lan
     * @Date 8:15 2021/4/17
     * @Version V1.0
     **/
    @Override
    public boolean verify(UniversalEntity u) {
        log.info(this.getClass().getName(), "验证中...");
        return encoder.encode(u.str4Hash(), null).equals(u.getHashCode());
    }


    @Override
    public void init(Encoder encoder, Decoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
        log = LogSystemFactory.getLogSystem();
    }

    @Override
    public String encode(String text) throws EncodeException {
        log.info(this.getClass().getName(), "加密中...");
        return encoder.encode(text, DBUtils.getSK(accessKey));
    }

    @Override
    public String decode(String text) {
        log.info(this.getClass().getName(), "解密中...");
        return decoder.decode(text, DBUtils.getSK(accessKey));
    }



    @Override
    public Encoder getEncoder() {
        return encoder;
    }

    public void setEncoder(Encoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public Decoder getDecoder() {
        return decoder;
    }

    public void setDecoder(Decoder decoder) {
        this.decoder = decoder;
    }

    public void setAccessKey(String key) {
        this.accessKey = key;
    }


}
