package app.dsm.verify;

import app.dsm.base.BaseEntity;
import app.dsm.security.Encoder;

public interface EntityVerifier {

    void init(Encoder encoder);

    boolean verify(BaseEntity entity);

}
