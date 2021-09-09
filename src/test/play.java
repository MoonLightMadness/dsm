package test;

import app.dsm.config.Configer;
import app.dsm.config.utils.ConfigerUtil;
import app.dsm.game.monitor.Monitor;
import app.dsm.game.monitor.impl.GenhinImpactMonitor;
import app.dsm.game.monitor.inner.InnerFunction;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.SimpleUtils;
import app.utils.special.RTimer;
import com.sun.xml.internal.ws.protocol.soap.ServerMUTube;
import lombok.Data;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * @ClassName : test.play
 * @Description :
 * @Date 2021-08-03 09:41:50
 * @Author ZhangHL
 */
public class play {

    private LogSystem log = LogSystemFactory.getLogSystem();

    @Test
    public void test1() {
        LocalDateTime localTime = LocalDateTime.now();
        System.out.println(localTime.toString());
    }

    @Test
    public void test2() {
        LogSystem log = LogSystemFactory.getLogSystem();
        log.info(null, "{}test{}test{}", "1", "2", "3");
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

    @Test
    public void test4() {
        char[] cname = "GenhinImpactMonitor".toCharArray();
        cname[0] = String.valueOf(cname[0]).toLowerCase(Locale.ROOT).toCharArray()[0];
        System.out.println(convertPOJOToDBType(String.valueOf(cname)));
        System.out.println(ConfigerUtil.isToday("log.date"));
    }

    private String convertPOJOToDBType(String property) {
        char[] seq = property.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : seq) {
            //如果是该字母是大写字母则变为小写且在前面加入下划线
            if (Character.isUpperCase(c)) {
                sb.append("_").append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @Test
    public void test5() {
        String str = "jps | find /I \"RemoteMavenServer36\"";
        String res = SimpleUtils.callShell(str, "c", true);
        res = res.split(" ")[0].trim();
        res = SimpleUtils.callShell("taskkill /F /PID " + res, "c", true);
        System.out.println(res);
    }

    @Test
    public void test6() {
        Configer configer = new Configer();
        RTimer rTimer = new RTimer();
        rTimer.start();
        System.out.println(configer.readConfig("notepad++.exe.start.mail.subject"));
        System.out.println(rTimer.end());
        rTimer.start();
        System.out.println(configer.readConfig("notepad++.exe.start.mail.subject"));
        System.out.println(rTimer.end());
    }

    @Test
    public void test7() {
        System.out.println("1");
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            log.error("{}", e);
        }
        System.out.println("2");
    }

    @Test
    public void test8() {
        for (int i = 1453; i < 1559; i++) {
            int y = i;
            int four = (y + 3) / 4;
            int hund = (y + 99) / 100;
            int fd = (y + 399) / 400;
            int test = four - hund + fd;
            System.out.println(four+" "+hund+" "+fd+" "+test + " " + i);
        }
    }

    @Test
    public void test9(){
        int x=0,y=0;
        int xy=0;
        int xp=0;
        for (int i =1;i<=12;i++){
            x+=i;
            y+=((367 * i - 362) / 12.0);
            xy += i*y;
            xp += (i*i);
        }
        double n = 12.0;
        double avg_x = x/n , avg_y = y/n;
        int sum_xy = xy , sum_xp = xp;
        double k = (sum_xy - n*avg_x*avg_y)/(sum_xp - n*avg_x);
        System.out.println(k);
        System.out.println(avg_y - k*avg_x);
    }

}

@Data
class School {
    private String name;

    private Student stu;


}

@Data
class Student {
    private String name;

    private String age;


}