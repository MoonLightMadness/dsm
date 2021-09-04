package app.log;


import app.dsm.base.JSONTool;
import app.dsm.config.Configer;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 初始化请调用init()方法
 *
 * @ClassName : utils.high.logsystem.LogSystem
 * @Description :日志系统--系统类
 * @Date 2021-03-31 20:43:24
 * @Author ZhangHL
 */
public class LogSystem {

    private List<LogEntity> list;

    private Logger log;

    private int logCount = 0;

    private boolean consoleOutput = true;

    private String sysName;

    private String logPath;

    private Configer configer;

    public void init() {
        log = new Logger();
        list = new ArrayList<>();
        configer = new Configer();
        sysName = configer.readConfig("sys.name");
        logPath = configer.readConfig("log.save.path");
        if (!isToday()) {
            dailyChange();
        }
    }

    public void init(boolean consoleOutput) {
        init();
        this.consoleOutput = consoleOutput;
    }

    private void dailyChange() {
        File file = new File(logPath + "/" + sysName + ".log");
        File old = new File(logPath + "/" + configer.readConfig("log.date") + "-" + file.getName());
        try {
            file.renameTo(old);
            file = new File(logPath + "/" + sysName + ".log");
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        configer.updateConfig("log.date", LocalDate.now().toString(), configer.readConfig("system.config.path"));
    }

    /**
     * 将一个日志实体加入到日志系统中
     *
     * @return
     * @class *ClassName*
     * @Description //TODO
     * @Param
     * @Author Zhang huai lan
     * @Date 20:55 2021/3/31
     **/
    private void add(LogEntity log) {
        try {
            synchronized (LogSystem.class) {
                ListIterator<LogEntity> iterator = list.listIterator();
                //控制台输出
                if (consoleOutput) {
                    System.out.println(log.toString());
                }
                iterator.add(log);
                logCount++;
                if (logCount >= LogConstantArg.AUTO_SAVE_MAX_COUNT) {
                    this.save();
                    logCount = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将日志系统中的日志持久化
     *
     * @return
     * @class *ClassName*
     * @Description //TODO
     * @Param
     * @Author Zhang huai lan
     * @Date 20:56 2021/3/31
     **/
    public void save() {
        synchronized (LogSystem.class) {
            File f = new File(logPath + "/" + sysName + ".log");
            try {
                if (!f.exists()) {
                    f.createNewFile();
                }
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(f, true), StandardCharsets.UTF_8);
                Iterator<LogEntity> iterator = list.iterator();
                while (iterator.hasNext()) {
                    LogEntity entity = iterator.next();
                    if (entity != null) {
                        writer.write(entity + "\n");
                    }
                    iterator.remove();
                }
                writer.close();
                //list.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isToday() {
        LocalDate localDate = LocalDate.now();
        String date = new Configer().readConfig("log.date");
        if(date != null){
            LocalDate config = LocalDate.parse(date);
            return localDate.until(config, ChronoUnit.DAYS) == 0;
        }else {
            configer.writeConfig(configer.readConfig("system.config.path"),"log.date",LocalDate.now().toString(),null,null );
        }
        return true;
    }


    public void immediatelySaveMode(boolean opt) {
        if (opt) {
            LogConstantArg.AUTO_SAVE_MAX_COUNT = 1;
        } else {
            LogConstantArg.AUTO_SAVE_MAX_COUNT = 1000;
        }
    }

    public void ok(String msg, Object... args) {
        this.add(log.ok(msg, args));
    }

    public void error(String msg, Object... args) {
        this.add(log.error(msg, args));
    }

    public void info(String msg, Object... args) {
        this.add(log.info(msg, args));
    }
}
