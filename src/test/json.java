package test;

import app.dsm.base.JSONTool;
import app.dsm.base.impl.UniversalEntity;
import app.dsm.base.impl.UniversalEntityWrapper;
import app.utils.SimpleUtils;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName : test.json
 * @Description :
 * @Date 2021-06-02 14:13:02
 * @Author ZhangHL
 */
public class json {
    @Test
    public void test1(){
        UniversalEntity entity1 = UniversalEntityWrapper.getOne(String.valueOf(System.currentTimeMillis()),
                "1",
                "test",
                "core",
                "1",
                "get_ip test",
                "null",
                "00001");
        byte[] b = SimpleUtils.serializableToBytes(entity1);
        String str= JSONObject.toJSONString(entity1,true);
        System.out.println(str);
        UniversalEntity entity = (UniversalEntity) JSONTool.getObject(str.getBytes(StandardCharsets.UTF_8),UniversalEntity.class);
        System.out.println(entity.toString());
    }
}
