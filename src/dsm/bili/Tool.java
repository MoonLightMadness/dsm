package dsm.bili;


import dsm.utils.SimpleUtils;
import dsm.utils.TimeFormatter;
import org.junit.Test;

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

    public static String getCountByName(String theme,String date){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        int size=0;
        try {
            DBforBili dbb = new DBforBili();
            List<Extracted> list = dbb.read(SimpleUtils.getTimeStamp2(TimeFormatter.DAY_LEVEL));
            for (Extracted extracted : list) {
                if(extracted.tags.contains(theme)){
                    sb.append(extracted.title).append("      author:").append(extracted.author).append("    score:").append(extracted.point).append("\n");
                    count++;
                }
                size++;
            }
            sb.append(count).append("\n");
            sb.append("ratio:").append(((float) count/size)*100).append("%\n");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    @Test
    public void getTest(){
        String res = Tool.getCountByName("机动战姬",SimpleUtils.getTimeStamp2(TimeFormatter.DAY_LEVEL));
        String[] titles={"Title","Author","Score"};
        String[] name={"drfhftgdjntucgyjmky",
        "drfhyre",
        "egtrrrrrrrrreeeee"};
        String[] author ={"werr3","jack","cuindy"};
        String[] score={"1130440","920259","912121"};
        System.out.println(SimpleUtils.stringFormatter(titles,name,author,score));
    }
}
