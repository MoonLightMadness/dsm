package app.dsm.security;

import app.dsm.base.BaseEntity;
import app.dsm.base.impl.UniversalEntity;
import app.dsm.security.excp.DecodeException;
import app.dsm.security.excp.EncodeException;

public interface EntitySecurityService {
    /**
     * 校验hash
     * @Param
     * @Author Zhang huai lan
     * @Version V1.0
     **/
    boolean verify(UniversalEntity u,Encoder encoder);

    /**
     * 初始化
     * @Param
     * @Author Zhang huai lan
     * @Version V1.0
     **/
    void init(Encoder encoder,Decoder decoder);
    /**
     * 加密
     * @Param
     * @Author Zhang huai lan
     * @Version V1.0
     *
     * @return*/
    BaseEntity encode(BaseEntity entity,String key) throws EncodeException;

    /**
     * 解密
     * @param
     * @return
     */
    BaseEntity decode(BaseEntity entity,String key) throws DecodeException;

    Encoder getEncoder();

    Decoder getDecoder();
}
