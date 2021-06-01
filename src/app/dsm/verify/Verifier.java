package app.dsm.verify;

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
