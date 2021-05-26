package dsm.bili;


import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @author zhl
 */
public class Tool {
    public static Date ChangeDate(Date now, int day) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = now.toString();
        // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
        java.util.Date date = sdf.parse(str, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(Calendar.DATE, day);
        java.util.Date date1 = calendar.getTime();
        String out = sdf.format(date1);
        Date d=java.sql.Date.valueOf(out);
        return d;
    }
    public static List<Extracted> getInfo(int n) throws SQLException, ClassNotFoundException, ParseException {
        DBforBili dfb=new DBforBili();
        List<Extracted> li=dfb.read(Tool.ChangeDate(new Date(new java.util.Date().getTime()),n));
        dfb.close();
        return li;
    }
}
