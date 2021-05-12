package test;

import dsm.base.impl.UniversalEntity;
import dsm.base.impl.UniversalEntityWrapper;
import dsm.security.higher.UniversalHigherDecoder;
import dsm.security.higher.UniversalHigherEncoder;
import dsm.security.impl.AESDecoder;
import dsm.security.impl.AESEncoder;
import org.junit.Test;

/**
 * @ClassName : test.higherEncoder
 * @Description :
 * @Date 2021-05-05 17:35:35
 * @Author 张怀栏
 */
public class higherEncoder {
    @Test
    public void test1(){
        UniversalEntity entity= UniversalEntityWrapper.getOne("1",
                "23",
                "test",
                "to:self",
                "23",
                "This is a test",
                "Test",
                "1234567891234567");
        entity= (UniversalEntity) UniversalHigherEncoder.entityEncode(new AESEncoder(),entity,entity.getCompressCode());
        System.out.println(entity);
        AESDecoder decoder=new AESDecoder();
        decoder.init();
        entity= (UniversalEntity) UniversalHigherDecoder.entityDecode(decoder,entity,entity.getCompressCode());
        System.out.println(entity);
    }
}
