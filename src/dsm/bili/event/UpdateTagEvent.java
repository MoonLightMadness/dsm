package dsm.bili.event;

import dsm.bili.Controler;
import dsm.bili.STATECODE;
import dsm.statemachine.impl.Event;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * @ClassName : dsm.bili.event.UpdateTagEvent
 * @Description :
 * @Date 2021-05-28 10:57:17
 * @Author ZhangHL
 */
public class UpdateTagEvent extends Event {
    @Override
    public int activateAction(Object obj) {
        Controler controler = (Controler) obj;
        try {
            if(STATECODE.UNDONE.getCode().equals(controler.getStatus())){
                System.out.println("Start to get Tags");
                Controler.getTags();
                System.out.println("Success");
                return 1;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
