package dsm.bili;

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
        System.out.println("Start to get Routine");
        Controler.getRoutine();
        System.out.println("Success");
//        if(!controler.checkDate() && controler.getStatus().equals(STATECODE.DONE.getCode())){
//            System.out.println("Start to get Routine");
//            Controler.getRoutine();
//            System.out.println("Success");
//        }
//        if(STATECODE.UNDONE.getCode().equals(controler.getStatus())){
//            System.out.println("Start to get Tags");
//            Controler.getTags();
//            System.out.println("Success");
//        }
//        if(controler.checkDate() && STATECODE.DONE.getCode().equals(controler.getStatus())){
//            System.out.println("Today's work has been done");
//        }


    }


}
