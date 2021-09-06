package app.dsm.game.monitor.impl;

import app.dsm.config.Configer;
import app.dsm.config.utils.ConfigerUtil;
import app.dsm.game.monitor.Monitor;
import app.dsm.game.monitor.vo.TimeVo;
import app.dsm.mail.Mail;
import app.dsm.mapper.annotation.TableName;
import app.dsm.mapper.impl.Mapper;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.guid.impl.SnowFlake;
import jdk.internal.dynalink.linker.LinkerServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 通用监视器类   <br>
 * 在配置文件中配置需要监视的进程名以及相关配置    <br>
 * 处于监视状态下的进程启动时将进行记录并且发送邮件      <br>
 * 同理，关闭时会将该条记录持久化并且发送邮件     <br>
 * 两种状态皆可通过配置文件开关邮件的发送功能     <br>
 * 具有每日报告和每周报告功能     <br>
 * 具有分析时间功能  <br>
 *
 * @ClassName : app.dsm.game.monitor.impl.UniversalMonitor
 * @Description :
 * @Date 2021-09-06 14:14:46
 * @Author ZhangHL
 */
@TableName("universal_monitor")
public class UniversalMonitor implements Monitor {


    private LogSystem log = LogSystemFactory.getLogSystem();

    private Configer configer = new Configer();

    private Mapper mapper;

    private SnowFlake snowFlake;

    /**
     * 存储被监视的进程的状态
     */
    private HashMap<String, Boolean> runtimes;

    /**
     * 存储进程开启但未关闭时的被监视进程的关键值
     * 关键值:id
     */
    private HashMap<String, String> processIn;

    @Override
    public void startMonitor() {
        renewRuntimes();
    }


    public void init() {
        //初始化存储结构
        runtimes = new HashMap<>();
        initialRuntimes();
        //初始化Mapper
        String dbName = configer.readConfig("monitor.database");
        mapper = new Mapper();
        mapper.initialize(this.getClass(), dbName);
        //初始化雪花Id生成器
        snowFlake = new SnowFlake();
        //初始化进程中存储结构
        processIn = new HashMap<>();
    }

    private void initialRuntimes() {
        List<String> target = getTargetList();
        for (String str : target) {
            runtimes.put(str, false);
        }
    }

    private String[] getTasklist() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process p = runtime.exec("tasklist");
            BufferedReader bw = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
            String s;
            StringBuilder sb = new StringBuilder();
            while ((s = bw.readLine()) != null) {
                sb.append(s).append("\n");
            }
            return sb.toString().split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新存储结构.
     *
     * @return
     * @author zhl
     * @date 2021-09-06 14:53
     * @version V1.0
     */
    private void renewRuntimes() {
        String[] tasklist = getTasklist();
        Set<String> keys = runtimes.keySet();
        for (String key : keys) {
            boolean onList = false;
            Boolean b = runtimes.get(key);
            assert tasklist != null;
            for (String task : tasklist) {
                if (task.toLowerCase(Locale.ROOT).startsWith(key.toLowerCase(Locale.ROOT))) {
                    if (!b) {
                        //进程刚刚启动了
                        runtimes.put(key, Boolean.TRUE);
                        progressStart(key);
                    }
                    onList = true;
                    break;
                }
            }
            //进程关闭了
            if (b && !onList) {
                progressClosed(key);
                runtimes.put(key, false);
            }
        }
    }

    private void progressStart(String progressName) {
        log.info("进程{}启动", progressName);
        //持久化
        persistanceOfStart(progressName);
        //邮件功能
        sendStartMail(progressName);
    }

    private void sendStartMail(String progressName) {
        boolean mainSwitch = ConfigerUtil.getSwitchOn("monitor.mail.main.switch");
        if (mainSwitch) {
            //检查是否有用户自定义的配置
            Boolean customConfig = ConfigerUtil.getSwitchOn(progressName + ".start.mail.switch");
            //如果有则发送邮件
            if (customConfig) {
                //检查用户是否自定义了该进程启动时的相关配置，如果没有则使用默认值
                String customMailReceiver = configer.readConfig(progressName + ".start.mail.receiver");
                String customSenderName = configer.readConfig(progressName + ".start.mail.sender");
                String customReceiverName = configer.readConfig(progressName + ".start.mail.receiver");
                String customSubject = configer.readConfig(progressName + ".start.mail.subject");
                String customContent = configer.readConfig(progressName + ".start.mail.content");
                if (customMailReceiver == null) {
                    customMailReceiver = configer.readConfig("monitor.start.mail.default.receiver");
                }
                if (customSenderName == null) {
                    customSenderName = configer.readConfig("monitor.start.mail.default.sendername");
                }
                if (customReceiverName == null) {
                    customReceiverName = configer.readConfig("monitor.start.mail.default.receivername");
                }
                if (customSubject == null) {
                    customSubject = configer.readConfig("monitor.start.mail.default.subject");
                }
                if (customContent == null) {
                    customContent = configer.readConfig("monitor.start.mail.default.content");
                }
                Mail.receiveMailAccount = customMailReceiver;
                int success = -1;
                while (success == -1) {
                    success = Mail.sendMail(customSenderName, customReceiverName, customSubject, customContent);
                }
            }
        }
    }

    private void persistanceOfStart(String progressName) {
        TimeVo timeVo = new TimeVo();
        try {
            timeVo.setId(snowFlake.generateGuid("2"));
            processIn.put(progressName, timeVo.getId());
        } catch (Exception e) {
            log.error("id生成失败，原因:{}", e);
            e.printStackTrace();
        }
        timeVo.setProcessName(progressName);
        timeVo.setStartTime(LocalDateTime.now().toString());
        timeVo.setGameDate(LocalDate.now().toString());
        mapper.save(timeVo);
    }

    private void progressClosed(String progressName) {
        log.info("进程{}关闭", progressName);
        //持久化
        persistanceOfClose(progressName);
        //邮件功能
        sendClosedMail(progressName);
        //从中间存储结构中删除
        processIn.remove(progressName);
    }

    private void sendClosedMail(String progressName){
        //邮件总开关
        boolean mainSwitch = ConfigerUtil.getSwitchOn("monitor.mail.main.switch");
        if(mainSwitch){
            //检查是否有用户自定义的配置
            Boolean customConfig = ConfigerUtil.getSwitchOn(progressName + ".close.mail.switch");
            //如果有则发送邮件
            if (customConfig) {
                //检查用户是否自定义了该进程启动时的相关配置，如果没有则使用默认值
                String customMailReceiver = configer.readConfig(progressName + ".close.mail.receiver");
                String customSenderName = configer.readConfig(progressName + ".close.mail.sender");
                String customReceiverName = configer.readConfig(progressName + ".close.mail.receiver");
                String customSubject = configer.readConfig(progressName + ".close.mail.subject");
                String customContent = configer.readConfig(progressName + ".close.mail.content");
                if (customMailReceiver == null) {
                    customMailReceiver = configer.readConfig("monitor.close.mail.default.receiver");
                }
                if (customSenderName == null) {
                    customSenderName = configer.readConfig("monitor.close.mail.default.sendername");
                }
                if (customReceiverName == null) {
                    customReceiverName = configer.readConfig("monitor.close.mail.default.receivername");
                }
                if (customSubject == null) {
                    customSubject = configer.readConfig("monitor.close.mail.default.subject");
                }
                if (customContent == null) {
                    customContent = configer.readConfig("monitor.close.mail.default.content");
                }
                Mail.receiveMailAccount = customMailReceiver;
                int success = -1;
                while (success == -1) {
                    success = Mail.sendMail(customSenderName, customReceiverName, customSubject, customContent);
                }
            }
        }
    }

    private void persistanceOfClose(String progressName) {
        String id = processIn.get(progressName);
        TimeVo beacon = new TimeVo();
        beacon.setId(id);
        //获取开始时间
        TimeVo start = (TimeVo) mapper.selectOne(new TimeVo(), beacon);
        TimeVo timeVo = new TimeVo();
        timeVo.setEndTime(LocalDateTime.now().toString());
        //计算持续时间
        long lastTime = LocalDateTime.parse(start.getStartTime()).until(LocalDateTime.now(), ChronoUnit.SECONDS);
        timeVo.setLastTime(String.valueOf(lastTime));
        //持久化
        mapper.update(timeVo, beacon);
    }

    private List<String> getTargetList() {
        return configer.readConfigList("monitor.target");
    }

    public static void main(String[] args) {
        UniversalMonitor universalMonitor = new UniversalMonitor();
        universalMonitor.init();
        try {
            while (true){
                universalMonitor.startMonitor();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
