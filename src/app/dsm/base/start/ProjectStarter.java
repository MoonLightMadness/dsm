package app.dsm.base.start;

import app.dsm.bili.Daily;
import app.dsm.core.Core;
import app.dsm.service.impl.Service;
import app.dsm.service.impl.ServiceMessageHandler;
import app.log.LogSystem;
import app.log.LogSystemFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @ClassName : app.dsm.base.start.ProjectStarter
 * @Description :
 * @Date 2021-05-11 14:19:52
 * @Author ZhangHL
 */
public class ProjectStarter {

    private static ProjectStarter ps ;

    private static LogSystem log ;

    public void start(){
        checkProjectFile();
    }

    //程序入口点
    public static void main(String[] args) {
        log = LogSystemFactory.getLogSystem();
        ps = new ProjectStarter();
        //模块开启选择
        ps.switchMode(args);
    }

    public void switchMode(String[] args){
       String name = "?";
        try {
            name = args[0];
            switch (name){
                case "core":
                    startCore(args[1]);
                    break;
                case "service":
                    startService(args[1],args[2]);
                    break;
                case "bili":
                    startBili();
                    break;
                default:
                    System.out.println("李在赣神魔");
                    break;
            }
        }catch (Exception e) {
            log.error(null,"[{}]模块启失败-->{}",name,e.getMessage());
        }
    }

    private void startCore(String name){
        Core core = new Core();
        core.init(name);
        Thread thread = new Thread(core);
        thread.start();

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startService(String name,String coreName){
        Service receiver = new Service();
        receiver.init(name,coreName,new ServiceMessageHandler());
        new Thread(receiver).start();
        try {
            while (true) {
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startBili(){
        Daily daily = new Daily();
        daily.init();
        System.out.println(daily.updateData());
        System.out.println(daily.updateTags());
        System.out.println(daily.showNew());
    }
    /**
     * 检查项目文件夹是否存在，不存在则建立程序运行必要文件
     */
    private void checkProjectFile(){
        String home = System.getProperties().getProperty("user.home");
        String separator = System.getProperties().getProperty("file.separator");
        File f = new File(home+separator+ "app/dsm");
        String path = f.getPath();
        Universal.CONFIG_PATH=path;
        Universal.SEPARATOR=separator;
        if(!f.exists()){
            f.mkdir();
            File flowconfig = new File(path+separator+"flowconfig.yaml");
            File log = new File(path+separator+"log.txt");
            File config = new File(path+separator+"config.txt");
            File  flow = new File(path+separator+"flow.txt");
            try {
                flow.createNewFile();
                flowconfig.createNewFile();
                log.createNewFile();
                config.createNewFile();
                createDataBase(path,separator);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createDataBase(String path,String separator){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection= DriverManager.getConnection("jdbc:sqlite:"+path+separator+"app.dsm.db");
            Statement statement =connection.createStatement();
            statement.executeUpdate( "create table sys_table(name varchar(20), version varchaar(20));" );
            //关闭数据库连接
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
