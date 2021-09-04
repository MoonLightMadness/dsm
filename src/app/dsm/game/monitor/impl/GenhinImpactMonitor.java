package app.dsm.game.monitor.impl;

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

    private Mapper mapper;

    private TimeVo timeVo;

    private SnowFlake snowFlake;

    public void init(){
        mapper = new Mapper();
        mapper.initialize(this.getClass());
        snowFlake = new SnowFlake();
    }

    @Override
    public void startMonitor() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process p = runtime.exec("tasklist");
            BufferedReader bw = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
            String s;
            while ((s=bw.readLine())!=null) {
                if(s.toLowerCase(Locale.ROOT).startsWith("yuanshen")){
                    setStatus(true);
                    return;
                }
            }
            setStatus(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setStatus(boolean isRunning){
        if(!this.isRunning && isRunning){
            sendRunning();
        }else if(this.isRunning && !isRunning){
            sendClose();
        }
        this.isRunning = isRunning;
    }


    private void sendRunning(){
        start = LocalDateTime.now();
        timeVo = new TimeVo();
        try {
            timeVo.setId(snowFlake.generateGuid("1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        timeVo.setStartTime(start.toString());
        int success = -1;
        while (success == -1){
            try {
               success = Mail.sendMail("home.pc","phone","YuanShen Start"
                        ,"YuanShen.exe Start in"+ start);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendClose(){
        LocalDateTime end = LocalDateTime.now();
        timeVo.setEndTime(end.toString());
        timeVo.setLastTime(String.valueOf(start.until(end, ChronoUnit.MINUTES)));
        int success = -1;
        while (success == -1){
            try {
                mapper.save(timeVo);
                success = Mail.sendMail("home.pc","phone","YuanShen Closed"
                        ,"YuanShen.exe Closed in"+ end+"</br>You played "+start.until(end, ChronoUnit.MINUTES)+" mins");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        try {
            GenhinImpactMonitor genhinImpactMonitor = new GenhinImpactMonitor();
            genhinImpactMonitor.init();
            while (true){
                genhinImpactMonitor.startMonitor();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
