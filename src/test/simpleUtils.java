package test;

import app.dsm.base.impl.UniversalEntity;
import app.dsm.base.impl.UniversalEntityWrapper;
import app.utils.EntityUtils;
import app.utils.SimpleUtils;
import app.utils.TimeFormatter;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @ClassName : test.simpleUtils
 * @Description :
 * @Date 2021-05-06 10:53:36
 * @Author ZhangHL
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
        res = SimpleUtils.callShell("notepad C:\\Users\\Administrator\\Desktop\\work\\DSM\\config.txt","c",false);
        System.out.println("res");
    }
    @Test
    public void test7(){
        String res =  SimpleUtils.callShell("PWDGenerator 20000 type-A 1","c",true);
        System.out.println(res);
    }

    @Test
    public void test8(){
        byte[] bytes =new byte[1024];
        bytes[0] = 'a';
        bytes[1] = '\0';
        System.out.println(bytes.length);
    }

    @Test
    public void test9() {
        System.out.println(SimpleUtils.readFile("./metaconfig.txt"));
    }

    @Test
    public void test10() {
        LocalDate start = LocalDate.parse("2021-08-09") ;
        LocalDate end = LocalDate.parse("2021-09-10");
        long interval = start.until(end, ChronoUnit.DAYS);
        System.out.println(interval);
    }
}
