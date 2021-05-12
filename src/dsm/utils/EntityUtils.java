package dsm.utils;

import dsm.base.BaseEntity;
import dsm.base.impl.UniversalEntity;

import java.lang.reflect.Field;
import java.util.Base64;

/**
 * @ClassName : dsm.utils.EntityUtils
 * @Description :
 * @Date 2021-05-06 20:07:17
 * @Author ZhangHL
 */
public class EntityUtils {

    public static BaseEntity propertiesCopy(BaseEntity entity){
        BaseEntity base=null;
        try {
            base=(entity.getClass().newInstance());
            for(Field f : entity.getClass().getDeclaredFields()){
                f.setAccessible(true);
                f.set(base,f.get(entity));
            }
            return base;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return base;
    }
}
