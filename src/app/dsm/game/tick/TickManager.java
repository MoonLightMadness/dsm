package app.dsm.game.tick;

import java.util.ArrayList;
import java.util.List;

public class TickManager {

    private static List<Tick> tickList = new ArrayList<>();

    public static void add(Tick t){
        tickList.add(t);
    }

    public static void update(){
        for (Tick t : tickList) {
            t.update();
        }
    }

}
