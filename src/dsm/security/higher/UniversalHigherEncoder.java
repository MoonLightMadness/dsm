package dsm.security.higher;

import dsm.base.BaseEntity;
import dsm.security.Encoder;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName : dsm.security.higher.UniversalHigherEncoder
 * @Description :
 * @Date 2021-05-05 17:24:41
 * @Author ZhangHL
 */
public class UniversalHigherEncoder {
    public static BaseEntity entityEncode(Encoder encoder, BaseEntity entity,String key){
        for (Field f : entity.getClass().getDeclaredFields()){
            try {
                f.setAccessible(true);
                if(f.getName().equals("compressCode")){
                    continue;
                }
                f.set(entity, encoder.encode(((String) f.get(entity)), key));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }
}
