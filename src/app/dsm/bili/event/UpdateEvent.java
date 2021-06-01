package app.dsm.bili.event;

import app.dsm.bili.Controler;
import app.dsm.bili.STATECODE;
import app.dsm.statemachine.impl.Event;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @ClassName : app.dsm.bili.event.UpdateEvent
 * @Description :
 * @Date 2021-05-28 10:55:12
 * @Author ZhangHL
 */
public class UpdateEvent extends Event {
    @Override
    public int activateAction(Object obj) {
        Controler controler = (Controler) obj;
        try {
            if(!controler.checkDate() && controler.getStatus().equals(STATECODE.DONE.getCode())){
                System.out.println("Start to get Routine");
                Controler.getRoutine();
                System.out.println("Success");
                return 1;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
