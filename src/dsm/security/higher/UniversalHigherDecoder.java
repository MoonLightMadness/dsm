package dsm.security.higher;

import dsm.base.BaseEntity;
import dsm.security.Decoder;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName : dsm.security.higher.UniversalHigherDecoder
 * @Description :
 * @Date 2021-05-05 17:51:25
 * @Author ZhangHL
 */
public class UniversalHigherDecoder {
    public static BaseEntity entityDecode(Decoder decoder, BaseEntity entity,String key){
        for (Field f : entity.getClass().getDeclaredFields()){
            try {
                f.setAccessible(true);
                if("compressCode".equals(f.getName())){
                    continue;
                }
                String text = ((String) f.get(entity));
                text=decoder.decode(text,key);
                f.set(entity, text);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }
}
