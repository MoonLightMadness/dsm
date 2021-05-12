package dsm.verify;

import dsm.base.BaseEntity;
import dsm.security.Encoder;
import dsm.security.EntitySecurityService;

public interface EntityVerifier {

    void init(Encoder encoder);

    boolean verify(BaseEntity entity);

}
