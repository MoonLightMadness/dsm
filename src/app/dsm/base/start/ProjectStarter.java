package app.dsm.base.start;

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


    public void start(){
        checkProjectFile();
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
