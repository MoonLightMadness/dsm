package dsm.db.impl;

import dsm.config.ConfigReader;
import dsm.config.impl.DatabaseConfigReader;
import dsm.db.DataBase;
import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : dsm.db.impl.SqliteImpl
 * @Description :
 * @Date 2021-05-04 14:16:02
 * @Author ZhangHL
 */
public class SqliteImpl<T> implements DataBase<T> {
    private ConfigReader configReader;

    private Connection connection;

    private Statement statement;

    private ResultSet resultSet;

    private LogSystem log;

    @Override
    public void init(String... args){
        log= LogSystemFactory.getLogSystem();
        //log.info(this.getClass().getName(),"初始化数据库连接");
        configReader=new DatabaseConfigReader();
        String dbName=configReader.read()[0];
        //log.info(this.getClass().getName(),"连接到数据库:{}",dbName);
        try {
            Class.forName("org.sqlite.JDBC");
            connection= DriverManager.getConnection("jdbc:sqlite:"+dbName);
            statement=connection.createStatement();
            //设置超时
            statement.setQueryTimeout(30);
            //log.info(this.getClass().getName(),"数据库连接初始化完成");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            log.error(this.getClass().getName(),e.getMessage());
        }
    }
    @Override
    public T get(String command) {
        try {
            synchronized (this){
                resultSet= statement.executeQuery(command);
                if(resultSet.next()){
                    return (T) resultSet.getObject(1);
                }
            }
        } catch (SQLException throwables) {
            log.error(this.getClass().getName(),"error:{}---sql:{}",throwables.getMessage(),throwables.getSQLState());
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public T[] gets(String command) {
        List<T> list=new ArrayList<>();
        int count=1;
        try {
            synchronized (this){
                resultSet= statement.executeQuery(command);
                while (resultSet.next()){
                    list.add((T) resultSet.getObject(count));
                    count++;
                }
            }
        } catch (SQLException throwables) {
            log.error(this.getClass().getName(),"error:{}---sql:{}",throwables.getMessage(),throwables.getSQLState());
            throwables.printStackTrace();
        }
        return (T[]) list.toArray();
    }

    @Override
    public void insert(String command) {
        try {
            synchronized (this){
                statement.execute(command);
            }
        } catch (SQLException throwables) {
            log.error(this.getClass().getName(),"error:{}---sql:{}",throwables.getMessage(),throwables.getSQLState());
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(String command) {
        try {
            synchronized (this){
                statement.execute(command);
            }
        } catch (SQLException throwables) {
            log.error(this.getClass().getName(),"error:{}---sql:{}",throwables.getMessage(),throwables.getSQLState());
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(String command) {
        try {
            synchronized (this){
                statement.execute(command);
            }
        } catch (SQLException throwables) {
            log.error(this.getClass().getName(),"error:{}---sql:{}",throwables.getMessage(),throwables.getSQLState());
            throwables.printStackTrace();
        }
    }


}
