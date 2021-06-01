package app.dsm.base.impl;

import app.dsm.security.impl.Sha1Encoder;
import app.utils.SimpleUtils;

/**
 * @ClassName : app.dsm.base.impl.UniversalEntityWrapper
 * @Description :
 * @Date 2021-05-01 15:55:27
 * @Author ZhangHL
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

    /**
     * 得到服务注册实体
     *
     * @param name 服务名字
     * @param code ak
     * @return {@link UniversalEntity}
     */
    public static UniversalEntity getServiceRegisterEntity(String name,String code){
        UniversalEntity universalEntity=new UniversalEntity();
        universalEntity.setGuid(null);
        universalEntity.setAuthLevel("service");
        universalEntity.setSrc(name);
        universalEntity.setTo(null);
        universalEntity.setLevel(null);
        universalEntity.setMessage(null);
        universalEntity.setMessageType(null);
        universalEntity.setCompressCode(code);
        universalEntity.setTimestamp(SimpleUtils.getTimeStamp());
        Sha1Encoder encoder=new Sha1Encoder();
        universalEntity.setHashCode(SimpleUtils.string2Base64Str(encoder.encode(universalEntity.toString(),null)));
        return universalEntity;
    }

}
