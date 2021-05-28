package dsm.bili;

import dsm.utils.SimpleUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainTest {
    //For Test
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, InterruptedException, ParseException {

        Controler controler=new Controler();
        if(!controler.checkDate() && controler.getStatus().equals(STATECODE.DONE.getCode())){
            System.out.println("Start to get Routine");
            Controler.getRoutine();
            System.out.println("Success");
        }
        if(STATECODE.UNDONE.getCode().equals(controler.getStatus())){
            System.out.println("Start to get Tags");
            Controler.getTags();
            System.out.println("Success");
        }
        if(controler.checkDate() && STATECODE.DONE.getCode().equals(controler.getStatus())){
            System.out.println("Today's work has been done");
            //Analyse
            System.out.println("Today's new:\n");
            System.out.println(Tool.newToday());
        }


    }
    @Test
    public void test1(){
        String time = "2021-05-23 00:00:00.000";
        String date = "2021-05-23 20:59:43.404";
        long interval = SimpleUtils.timeCalculator2(date,time);
        System.out.println(interval);

        System.out.println(Tool.newToday());
    }

}
