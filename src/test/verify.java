package test;

import dsm.base.impl.UniversalEntity;
import dsm.base.impl.UniversalEntityWrapper;
import dsm.security.EntitySecurityService;
import dsm.security.SecurityService;
import dsm.security.excp.DecodeException;
import dsm.security.excp.EncodeException;
import dsm.security.impl.*;
import dsm.utils.SimpleUtils;
import dsm.verify.EntityVerifier;
import dsm.verify.Verifier;
import dsm.verify.impl.EntityVerifierImpl;
import dsm.verify.impl.MessageVerifier;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

/**
 * @ClassName : test.verify
 * @Description :
 * @Date 2021-04-29 19:48:00
 * @Author 张怀栏
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
