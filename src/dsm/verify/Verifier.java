package dsm.verify;

import dsm.base.BaseEntity;
import dsm.base.impl.UniversalEntity;

/**
 * 验证器
 * @Param
 * @Author Zhang huai lan
 * @Version V1.0
 **/
public interface Verifier {
    /**
     * 验证
     * @Param bytes-密文 key-秘钥
     * @Author Zhang huai lan
     * @Version V1.0
     **/
    boolean verify(String text,String key);

}
