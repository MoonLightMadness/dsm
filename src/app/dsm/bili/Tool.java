package app.dsm.bili;


import app.utils.SimpleUtils;
import app.utils.TimeFormatter;

import java.sql.SQLException;
import java.util.Iterator;
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

    public static String newToday(){
        String today = SimpleUtils.getTimeStamp2(TimeFormatter.DAY_LEVEL);
        String yesterday = SimpleUtils.getTimeStamp(TimeFormatter.DAY_LEVEL,TimeFormatter.DAY_FATOR,-1);
        StringBuilder sb = new StringBuilder();
        try {
            DBforBili dbb = new DBforBili();
            List<Extracted> yesterList = dbb.read(yesterday);
            List<Extracted> current = dbb.read(today);
            //检查数据正确性
            if(checkData(current,yesterList)){
                int index = 0;
                Iterator<Extracted> curi = current.iterator();
                while (curi.hasNext()){
                    Extracted e = curi.next();
                    Iterator<Extracted> yesi = yesterList.iterator();
                    while (yesi.hasNext()){
                        Extracted y = yesi.next();
                        if(e.url.equals(y.url)){
                            curi.remove();
                        }
                    }
                }
                for(Extracted cur:current){
                    sb.append(index).append(" ").append(cur.title).append("   ").append(cur.author).append("   ").append(cur.point).append("\n");
                    index++;
                }
                return sb.toString();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static boolean checkData(List c,List y){
        if(c.size()==0){
            System.out.println("今日数据未同步");
            return false;
        }
        if(y.size()==0){
            System.out.println("昨日无数据");
            return false;
        }
        return true;
    }
}
