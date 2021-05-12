package dsm.security;

import dsm.base.impl.UniversalEntity;
import dsm.security.excp.DecodeException;
import dsm.security.excp.EncodeException;


/**
 * 安全服务
 * @ClassName : utils.high.security.SecurityService
 * @Description :
 * @Date 2021-04-03 17:05:40
 * @Author 张怀栏
 */
public interface SecurityService {
    /**
     * 校验hash
     * @Param
     * @Author Zhang huai lan
     * @Version V1.0
     **/
    boolean verify(UniversalEntity u);
    
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
    String encode(String text) throws EncodeException;

    /**
     * 解密
     * @param bytes
     * @return
     */
    String decode(String bytes) throws DecodeException;

    Encoder getEncoder();

    Decoder getDecoder();
}
