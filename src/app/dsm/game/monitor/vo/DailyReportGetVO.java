package app.dsm.game.monitor.vo;

import lombok.Data;

/**
 * @ClassName : app.dsm.game.monitor.vo.DailyReportGetVO
 * @Description :
 * @Date 2021-09-07 15:20:37
 * @Author ZhangHL
 */
@Data
public class DailyReportGetVO {

    private String startTime;

    private String endTime;

    private String lastTime;

    private String gameDate;

    private String processName;


}
