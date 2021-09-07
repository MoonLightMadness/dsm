package app.dsm.game.monitor.impl;

import app.dsm.config.Configer;
import app.dsm.config.utils.ConfigerUtil;
import app.dsm.game.monitor.Report;
import app.dsm.game.monitor.vo.DailyReportGetVO;
import app.dsm.mail.Mail;
import app.dsm.mapper.annotation.TableName;
import app.dsm.mapper.impl.Mapper;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : app.dsm.game.monitor.impl.DailyReport
 * @Description :
 * @Date 2021-09-07 15:12:22
 * @Author ZhangHL
 */
@TableName("universal_monitor")
public class DailyReport implements Report, Runnable {

    private Configer configer = new Configer();

    private LogSystem log = LogSystemFactory.getLogSystem();

    private Mapper mapper;

    private List<String> waitSend;

    @Override
    public void init() {
        mapper = new Mapper();
        mapper.initialize(this.getClass(), configer.readConfig("monitor.database"));
        waitSend = new ArrayList<>();
    }

    @Override
    public String getReport(String date,String processName) {
        StringBuilder sb = new StringBuilder();
        DailyReportGetVO dailyReportGetVO = new DailyReportGetVO();
        dailyReportGetVO.setProcessName(processName);
        dailyReportGetVO.setGameDate(date);
        Object[] res = mapper.selectList(new DailyReportGetVO(), dailyReportGetVO);
        if (res != null) {
            long totalTime = 0L;
            int count = 0;
            sb.append(processName).append(":<br>");
            for (Object obj : res) {
                DailyReportGetVO temp = (DailyReportGetVO) obj;
                sb.append(count++).append(" ");
                sb.append(" ").append(temp.getStartTime()).append("~").append(temp.getEndTime()).append("<br>");
                if (temp.getLastTime() != null) {
                    totalTime += Long.parseLong(temp.getLastTime());
                }
            }
            sb.append("总计:").append(totalTime / 60).append(" 分钟").append("<br>").append("<br>");
        }
        return sb.toString();
    }


    private boolean checkSwitch(String processName) {
        if (ConfigerUtil.getSwitchOn("monitor.daily.report.switch")) {
            if (ConfigerUtil.getSwitchOn(processName + ".daily.report.switch")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean checkTime(String processName) {
        LocalTime localTime = LocalTime.now();
        String configTime = configer.readConfig(processName + ".daily.report.time");
        if (configTime == null) {
            configTime = configer.readConfig("monitor.daily.report.time");
        }
        if (configTime == null) {
            return false;
        }
        LocalTime config = LocalTime.parse(configTime);
        if (localTime.until(config, ChronoUnit.SECONDS) <= 1 &&
                localTime.until(config, ChronoUnit.SECONDS) >= 0 &&
                !waitSend.contains(processName)) {
            return true;
        }
        return false;
    }

    private void sendDailyMail(String processName) {
        String content = getReport(LocalDate.now().toString(),processName);
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

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        log.info("每日报告模块启动");
        while (true) {
            try {
                List<String> targets = configer.readConfigList("monitor.target");
                for (String target : targets) {
                    if (checkSwitch(target) && checkTime(target)) {
                        waitSend.add(target);
                    }
                }
                for(String target : waitSend){
                    sendDailyMail(target);
                }
                waitSend = new ArrayList<>();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error("每日报告模块出现错误，原因：{}",e);
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test() {
        LocalTime now = LocalTime.parse("04:00:00");
        LocalTime temp = LocalTime.parse("08:00:00");
        System.out.println(now.until(temp,ChronoUnit.SECONDS));
    }
}
