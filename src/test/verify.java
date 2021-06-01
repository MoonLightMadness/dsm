package test;

import app.dsm.base.impl.UniversalEntity;
import app.dsm.base.impl.UniversalEntityWrapper;
import app.dsm.security.EntitySecurityService;
import app.dsm.security.excp.DecodeException;
import app.dsm.security.excp.EncodeException;
import app.dsm.security.impl.*;
import app.dsm.verify.EntityVerifier;
import app.dsm.verify.impl.EntityVerifierImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * @ClassName : test.verify
 * @Description :
 * @Date 2021-04-29 19:48:00
 * @Author ZhangHL
 */
public class verify {

    @Test
    public void versifyTest(){
        UniversalEntity entity= UniversalEntityWrapper.getOne("1",
                "23",
                "test",
                "to:self",
                "23",
                "This is a test",
                "Test",
                "123");
        EntityVerifier verifier =new EntityVerifierImpl();
        verifier.init(new Sha1Encoder());

        EntitySecurityService securityService=new EntitySecurityServiceImpl();
        securityService.init(new AESEncoder(),new AESDecoder());
       try {
           entity= (UniversalEntity) securityService.encode(entity,entity.getCompressCode());
           entity=(UniversalEntity) securityService.decode(entity,entity.getCompressCode());
           Assert.assertEquals(true,verifier.verify(entity));
       } catch (EncodeException | DecodeException e) {
           e.printStackTrace();
       }

    }
}
