package app.dsm.game.monitor.inner;

import app.dsm.config.Configer;
import app.dsm.game.monitor.Report;
import app.dsm.game.monitor.constant.InnerConstant;
import app.dsm.game.monitor.constant.TimeConstant;
import app.dsm.game.monitor.impl.DailyReport;
import app.dsm.game.monitor.vo.DailyReportVO;
import app.dsm.game.monitor.vo.TimeVo;
import app.dsm.mail.Mail;
import app.dsm.mapper.impl.Mapper;
import app.utils.TimeFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName : app.dsm.game.monitor.inner.InnerFunction
 * @Description :
 * @Date 2021-09-08 09:02:28
 * @Author ZhangHL
 */
public class InnerFunction {

    private static Configer configer = new Configer();

    public static boolean checkInnerFunction(String cmd){
        for(String str : InnerConstant.innerFunctions){
            if(str.equals(cmd)){
                return true;
            }
        }
        return false;
    }

    public static String getTime(){
        LocalTime time = LocalTime.now();
        return time.format(DateTimeFormatter.ofPattern(TimeFormatter.NORMALTIME));
    }

    public static String getDate(){
        LocalDate localDate = LocalDate.now();
        return localDate.toString();
    }

    public static String getDateTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.toString().replace("T"," ");
    }

    public static String getProcessName(String processName){
        return processName;
    }

    public static String getLastTime(String id, Mapper mapper){
        TimeVo timeVo = new TimeVo();
        timeVo.setId(id);
        DailyReportVO dailyReportVO = (DailyReportVO) mapper.selectOne(new DailyReportVO(),timeVo);
        if(dailyReportVO.getLastTime() != null){
            return dailyReportVO.getLastTime();
        }else {
            return "-1";
        }
    }

    public static void sendReport(String processName){
        sendReport(processName);
    }

    private void sendDailyMail(String processName) {
        Report report = new DailyReport();
        report.init();
        String content = report.getReport(LocalDate.now().toString(),processName);
        String customMailReceiver = configer.readConfig(processName + ".mail.daily.receiver");
        String customSenderName = configer.readConfig(processName + ".mail.daily.sender");
        String customReceiverName = configer.readConfig(processName + ".mail.daily.receivername");
        if (customMailReceiver == null) {
            customMailReceiver = configer.readConfig("monitor.mail.default.daily.receiver");
        }
        if (customSenderName == null) {
            customSenderName = configer.readConfig("monitor.mail.default.daily.sendername");
        }
        if (customReceiverName == null) {
            customReceiverName = configer.readConfig("monitor.mail.default.daily.receivername");
        }
        Mail.receiveMailAccount = customMailReceiver;
        int success = -1;
        while (success == -1) {
            success = Mail.sendMail(customSenderName, customReceiverName, "每日报告", content);
        }
    }
}
