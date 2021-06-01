package app.dsm.verify.impl;

import app.dsm.base.BaseEntity;
import app.dsm.security.Encoder;
import app.utils.SimpleUtils;
import app.dsm.verify.EntityVerifier;

/**
 * @ClassName : app.dsm.verify.impl.EntityVerifierImpl
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
