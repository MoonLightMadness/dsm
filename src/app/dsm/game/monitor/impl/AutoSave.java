package app.dsm.game.monitor.impl;

import app.dsm.config.Configer;
import app.dsm.game.monitor.constant.TimeConstant;
import app.dsm.game.monitor.vo.TimeVo;
import app.dsm.mapper.impl.Mapper;
import app.log.LogSystem;
import app.log.LogSystemFactory;

import java.util.HashMap;
import java.util.ListIterator;

/**
 * @ClassName : app.dsm.game.monitor.impl.AutoSave
 * @Description :
 * @Date 2021-09-07 10:33:37
 * @Author ZhangHL
 */
public class AutoSave implements Runnable {

    private HashMap<String, String> processIn;

    private Configer configer = new Configer();

    private LogSystem log = LogSystemFactory.getLogSystem();

    private Mapper mapper;

    public AutoSave(Mapper mapper,HashMap<String, String> processIn){
        this.mapper = mapper;
        this.processIn = processIn;
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
        while (true){
            try {
                long sleep = parseConfig();
                log.info("下次保存时间--{}ms",sleep);
                Thread.sleep(sleep);
                log.info("自动保存开始");
                synchronized (HashMap.class){
                    ListIterator<String> iterator = (ListIterator<String>) processIn.keySet().iterator();
                    while (iterator.hasNext()){
                        String id = processIn.get(iterator.next());
                        TimeVo condition = new TimeVo();
                        condition.setId(id);
                        TimeVo timeVo = (TimeVo) mapper.selectOne(new TimeVo(),condition);
                        TimeVo update = new TimeVo();
                        if(timeVo.getLastTime()!=null){
                            update.setLastTime(String.valueOf(Long.parseLong(timeVo.getLastTime())+sleep));
                        }else {
                            update.setLastTime(String.valueOf(sleep));
                        }
                        mapper.update(update,condition);
                    }
                }
                log.info("自动保存结束");
            }catch (Exception e){
                log.error("自动保存模块出现错误，原因:{}",e);
            }
        }
    }

    public long parseConfig() {
        String autosave = configer.readConfig("monitor.autosave");
        char[] cas = autosave.toCharArray();
        StringBuilder sb = new StringBuilder();
        long total = 0L;
        // y M d h m s
        for (char ca : cas) {
            if (ca == 'y') {
                total += Integer.parseInt(sb.toString().trim()) * TimeConstant.YearOfMillSeconds;
                sb = new StringBuilder();
            } else if (ca == 'M') {
                total += Integer.parseInt(sb.toString().trim()) * TimeConstant.MonthOfMillSeconds;
                sb = new StringBuilder();
            } else if (ca == 'd') {
                total += Integer.parseInt(sb.toString().trim()) * TimeConstant.DayOfMillSeconds;
                sb = new StringBuilder();
            } else if (ca == 'h') {
                total += Integer.parseInt(sb.toString().trim()) * TimeConstant.HourOfMillSeconds;
                sb = new StringBuilder();
            } else if (ca == 'm') {
                total += Integer.parseInt(sb.toString().trim()) * TimeConstant.MinuteOfMillSeconds;
                sb = new StringBuilder();
            } else if (ca == 's') {
                total += Integer.parseInt(sb.toString().trim()) * TimeConstant.SecondOfMillSeconds;
                sb = new StringBuilder();
            }else {
                sb.append(ca);
            }
        }
        return total;
    }
}
