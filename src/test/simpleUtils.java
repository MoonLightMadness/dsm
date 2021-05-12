package test;

import dsm.base.impl.UniversalEntity;
import dsm.base.impl.UniversalEntityWrapper;
import dsm.utils.EntityUtils;
import dsm.utils.SimpleUtils;
import dsm.utils.TimeFormatter;
import org.junit.Assert;
import org.junit.Test;

/**
 * @ClassName : test.simpleUtils
 * @Description :
 * @Date 2021-05-06 10:53:36
 * @Author 张怀栏
 */
public class simpleUtils {
    @Test
    public void test1() {
        String test = "This is a Test";
        String encode = SimpleUtils.string2Base64Str(test);
        System.out.println(encode);
        String decode = SimpleUtils.base64Str2String(encode);
        System.out.println(decode);
        System.out.println(test.equals(decode));
        Assert.assertEquals(test,decode);

    }
    @Test
    public void test2(){
        String date="2021-05-05 18:49:11.433";
        System.out.println(SimpleUtils.timeCalculator(date));
    }
    @Test
    public void test3(){
        String d1="2021-05-05 18:48:44.385";
        String d2="2021-05-05 18:48:18.717";
        System.out.println(SimpleUtils.timeCalculator2(d1,d2));
    }
    @Test
    public void test4(){
        String date="2021-05-05 18:49:11.433";
        System.out.println(SimpleUtils.timeCalculator2(date,"2021-05-06 11:20:22.305", TimeFormatter.HOUR_FATOR));
        System.out.println(SimpleUtils.timeCalculator(date,TimeFormatter.HOUR_FATOR));
    }
    @Test
    public void test5(){
        UniversalEntity entity= UniversalEntityWrapper.getOne("1",
                "23",
                "test",
                "to:self",
                "23",
                "This is a test",
                "Test",
                "123");
        UniversalEntity test1= (UniversalEntity) EntityUtils.propertiesCopy(entity);
        UniversalEntity test2=entity;
        Assert.assertFalse(entity==test1);
        Assert.assertTrue(entity==test2);
    }
    @Test
    public void test6(){
        String res =  SimpleUtils.callShell("ipconfig","c",true);
        System.out.println(res);
        res = SimpleUtils.call("notepad C:\\Users\\Administrator\\Desktop\\work\\DSM\\config.txt");
        System.out.println(res);
    }
}
