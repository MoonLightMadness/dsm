package dsm.base.impl;

import dsm.security.impl.Sha1Encoder;
import dsm.utils.SimpleUtils;
import dsm.utils.guid.impl.SnowFlake;

/**
 * @ClassName : dsm.base.impl.UniversalEntityWrapper
 * @Description :
 * @Date 2021-05-01 15:55:27
 * @Author 张怀栏
 */
public class UniversalEntityWrapper {

    public static UniversalEntity getOne(String guid,
                                         String authLevel,
                                         String src,
                                         String to,
                                         String level,
                                         String message,
                                         String messageType,
                                         String compressCode
                                         ){
        UniversalEntity universalEntity=new UniversalEntity();
        universalEntity.setGuid(guid);
        universalEntity.setAuthLevel(authLevel);
        universalEntity.setSrc(src);
        universalEntity.setTo(to);
        universalEntity.setLevel(level);
        universalEntity.setMessage(message);
        universalEntity.setMessageType(messageType);
        universalEntity.setCompressCode(compressCode);
        universalEntity.setTimestamp(SimpleUtils.getTimeStamp());
        Sha1Encoder encoder=new Sha1Encoder();
        universalEntity.setHashCode(SimpleUtils.string2Base64Str(encoder.encode(universalEntity.toString(),null)));
        return universalEntity;
    }

    public static UniversalEntity getVisitorEntity(){
        UniversalEntity universalEntity=new UniversalEntity();
        universalEntity.setGuid(null);
        universalEntity.setAuthLevel("Visitor");
        universalEntity.setSrc("Unknown");
        universalEntity.setTo("Gate");
        universalEntity.setLevel("01");
        universalEntity.setMessage(null);
        universalEntity.setMessageType(null);
        universalEntity.setCompressCode("123");
        universalEntity.setTimestamp(SimpleUtils.getTimeStamp());
        Sha1Encoder encoder=new Sha1Encoder();
        universalEntity.setHashCode(SimpleUtils.string2Base64Str(encoder.encode(universalEntity.toString(),null)));
        return universalEntity;
    }

    public static UniversalEntity getOKEntity(String ak){
        UniversalEntity universalEntity=new UniversalEntity();
        universalEntity.setGuid(null);
        universalEntity.setAuthLevel("OK");
        universalEntity.setSrc("Gate");
        universalEntity.setTo(null);
        universalEntity.setLevel(null);
        universalEntity.setMessage(null);
        universalEntity.setMessageType(null);
        universalEntity.setCompressCode(ak);
        universalEntity.setTimestamp(SimpleUtils.getTimeStamp());
        Sha1Encoder encoder=new Sha1Encoder();
        universalEntity.setHashCode(SimpleUtils.string2Base64Str(encoder.encode(universalEntity.toString(),null)));
        return universalEntity;
    }

    public static UniversalEntity getErrorEntity(){
        UniversalEntity universalEntity=new UniversalEntity();
        universalEntity.setGuid(null);
        universalEntity.setAuthLevel("Error");
        universalEntity.setSrc("Gate");
        universalEntity.setTo(null);
        universalEntity.setLevel(null);
        universalEntity.setMessage(null);
        universalEntity.setMessageType(null);
        universalEntity.setCompressCode("0");
        universalEntity.setTimestamp(SimpleUtils.getTimeStamp());
        Sha1Encoder encoder=new Sha1Encoder();
        universalEntity.setHashCode(SimpleUtils.string2Base64Str(encoder.encode(universalEntity.toString(),null)));
        return universalEntity;
    }

}
