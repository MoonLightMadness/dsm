package test;

import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.SimpleUtils;
import com.sun.xml.internal.ws.protocol.soap.ServerMUTube;
import lombok.Data;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @ClassName : test.play
 * @Description :
 * @Date 2021-08-03 09:41:50
 * @Author ZhangHL
 */
public class play {
    @Test
    public void test1(){
        LocalDateTime localTime = LocalDateTime.now();
        System.out.println(localTime.toString());
    }

    @Test
    public void test2() {
        LogSystem log = LogSystemFactory.getLogSystem();
        log.info(null,"{}test{}test{}","1","2","3");
    }
    @Test
    public void test3() {
        School school = new School();
        school.setName("sch");
        Student stu = new Student();
        stu.setName("zzz");
        stu.setAge("19");
        school.setStu(stu);
        School dup = (School) SimpleUtils.duplicate(school);
        System.out.println(school == dup);
        System.out.println(dup.getName());
        System.out.println(dup.getStu().getName());
        System.out.println(dup.getStu().getAge());
    }


}
@Data
class School{
    private String name;

    private Student stu;



}
@Data
class Student{
    private String name;

    private String age;


}