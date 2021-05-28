package dsm.bili.event;

import dsm.bili.Controler;
import dsm.bili.STATECODE;
import dsm.bili.Tool;
import dsm.statemachine.impl.Event;

import java.sql.SQLException;

/**
 * @ClassName : dsm.bili.event.ShowNewEvent
 * @Description :
 * @Date 2021-05-28 10:59:30
 * @Author ZhangHL
 */
public class ShowNewEvent extends Event {
    @Override
    public int activateAction(Object obj) {
        Controler controler = (Controler) obj;
        try {
            if(controler.checkDate() && STATECODE.DONE.getCode().equals(controler.getStatus())){
                System.out.println("Today's work has been done");
                //Analyse
                System.out.println("Today's new:\n");
                System.out.println(Tool.newToday());
                return 1;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
