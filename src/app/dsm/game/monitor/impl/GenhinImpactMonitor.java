package app.dsm.game.monitor.impl;

import app.dsm.config.Configer;
import app.dsm.game.monitor.Monitor;
import app.dsm.game.monitor.vo.TimeVo;
import app.dsm.mail.Mail;
import app.dsm.mapper.annotation.TableName;
import app.dsm.mapper.impl.Mapper;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.guid.impl.SnowFlake;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@TableName("genhin_impact")
public class GenhinImpactMonitor implements Monitor {


    private boolean isRunning = false;

    private LocalDateTime start;

    private LogSystem log = LogSystemFactory.getLogSystem();

    private Configer configer;

    private Mapper mapper;

    private TimeVo timeVo;

    private SnowFlake snowFlake;

    public void init() {
        mapper = new Mapper();
        mapper.initialize(this.getClass());
        snowFlake = new SnowFlake();
        configer = new Configer();
    }

    @Override
    public void startMonitor() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process p = runtime.exec("tasklist");
            BufferedReader bw = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
            String s;
            while ((s = bw.readLine()) != null) {
                if (s.toLowerCase(Locale.ROOT).startsWith("yuanshen")) {
                    setStatus(true);
                    return;
                }
            }
            setStatus(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setStatus(boolean isRunning) {
        boolean mailSwtch = false;
        if (configer.readConfig("mail.switch").toLowerCase(Locale.ROOT).equals("on")) {
            mailSwtch = true;
        } else {
            mailSwtch = false;
        }
        if (!this.isRunning && isRunning) {
            sendRunning(mailSwtch);
        } else if (this.isRunning && !isRunning) {
            sendClose(mailSwtch);
        }
        this.isRunning = isRunning;
    }


    private void sendRunning(boolean mailSwtch) {
        start = LocalDateTime.now();
        timeVo = new TimeVo();
        try {
            timeVo.setId(snowFlake.generateGuid("1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        timeVo.setStartTime(start.toString());
        log.info("启动游戏--{}", start);
        if(mailSwtch){
            int success = -1;
            while (success == -1) {
                try {
                    success = Mail.sendMail("home.pc", "phone", "YuanShen Start"
                            , "YuanShen.exe Start in" + start);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendClose(boolean mailSwtch) {
        LocalDateTime end = LocalDateTime.now();
        timeVo.setEndTime(end.toString());
        timeVo.setLastTime(String.valueOf(start.until(end, ChronoUnit.MINUTES)));
        mapper.save(timeVo);
        log.info("结束游戏--{}", end);
        if(mailSwtch){
            int success = -1;
            while (success == -1) {
                try {

                    success = Mail.sendMail("home.pc", "phone", "YuanShen Closed"
                            , "YuanShen.exe Closed in" + end + "</br>You played " + start.until(end, ChronoUnit.MINUTES) + " mins");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            GenhinImpactMonitor genhinImpactMonitor = new GenhinImpactMonitor();
            genhinImpactMonitor.init();
            while (true) {
                genhinImpactMonitor.startMonitor();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
