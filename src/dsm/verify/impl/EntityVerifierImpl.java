package dsm.verify.impl;

import dsm.base.BaseEntity;
import dsm.base.impl.UniversalEntity;
import dsm.security.Encoder;
import dsm.security.EntitySecurityService;
import dsm.utils.SimpleUtils;
import dsm.verify.EntityVerifier;

import java.util.Base64;

/**
 * @ClassName : dsm.verify.impl.EntityVerifierImpl
 * @Description :
 * @Date 2021-05-06 19:40:54
 * @Author ZhangHL
 */
public class EntityVerifierImpl implements EntityVerifier {

    private Encoder encoder;

    @Override
    public void init(Encoder encoder) {
        this.encoder=encoder;
    }

    @Override
    public boolean verify(BaseEntity entity) {
        String hashCode=entity.getHashCode();
        entity.setHashCode(null);
        String reHash=SimpleUtils.string2Base64Str(encoder.encode(entity.toString(),null));
        boolean res = (hashCode.equals(reHash));
        entity.setHashCode(hashCode);
        return res;
    }
}
