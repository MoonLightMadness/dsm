package app.dsm.security.impl;

import app.dsm.base.BaseEntity;
import app.dsm.base.impl.UniversalEntity;
import app.dsm.log.LogSystem;
import app.dsm.log.LogSystemFactory;
import app.dsm.security.Decoder;
import app.dsm.security.Encoder;
import app.dsm.security.EntitySecurityService;
import app.dsm.security.excp.DecodeException;
import app.dsm.security.excp.EncodeException;
import app.dsm.utils.special.DBUtils;
import app.dsm.verify.EntityVerifier;
import app.dsm.verify.impl.EntityVerifierImpl;

import java.lang.reflect.Field;

/**
 * Entity安全服务
 *
 * @ClassName : app.dsm.security.impl.EntitySecurityServiceImpl
 * @Description :
 * @Date 2021-05-06 16:53:05
 * @Author ZhangHL
 */
public class EntitySecurityServiceImpl implements EntitySecurityService {
    private Encoder encoder;

    private Decoder decoder;

    private LogSystem log;

    @Override
    public boolean verify(UniversalEntity u,Encoder encoder) {
        EntityVerifier verifier =new EntityVerifierImpl();
        verifier.init(encoder);
        return verifier.verify(u);
    }

    @Override
    public void init(Encoder encoder, Decoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
        log = LogSystemFactory.getLogSystem();
    }
    /**
     *
     * @param key - AK
     * @return
     * @author Zhang huai lan
     * @date 21:23 2021/5/6
     * @version V1.0
     **/
    @Override
    public BaseEntity encode(BaseEntity entity,String key) throws EncodeException {
        key= DBUtils.getSK(key);
        for (Field f : entity.getClass().getDeclaredFields()){
            try {
                f.setAccessible(true);
                if(f.getName().equals("compressCode")){
                    continue;
                }
                if(f.get(entity)==null) {
                    continue;
                }
                f.set(entity, encoder.encode(((String) f.get(entity)), key));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }
    /**
     *
     * @param key - AK
     * @return
     * @author Zhang huai lan
     * @date 21:23 2021/5/6
     * @version V1.0
     **/
    @Override
    public BaseEntity decode(BaseEntity entity,String key) throws DecodeException {
        key= DBUtils.getSK(key);
        for (Field f : entity.getClass().getDeclaredFields()){
            try {
                f.setAccessible(true);
                if("compressCode".equals(f.getName())){
                    continue;
                }
                String text = ((String) f.get(entity));
                if(text==null){
                    continue;
                }
                text=decoder.decode(text,key);
                f.set(entity, text);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }


    @Override
    public Encoder getEncoder() {
        return encoder;
    }

    @Override
    public Decoder getDecoder() {
        return decoder;
    }
}
