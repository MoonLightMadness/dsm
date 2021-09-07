package app.dsm.game.monitor;

public interface Report {

    void init();

    String getReport(String date,String processName);

}
