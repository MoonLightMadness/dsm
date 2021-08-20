package app.dsm.db.impl;

import app.dsm.config.Argument;
import app.dsm.config.ConfigReader;
import app.dsm.config.Configer;
import app.dsm.config.impl.DatabaseConfigReader;
import app.dsm.db.DataBase;
import app.dsm.db.exception.DBException;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @ClassName : app.dsm.db.impl.SqliteImpl
 * @Description :
 * @Date 2021-05-04 14:16:02
 * @Author ZhangHL
 */
public class SqliteImpl<T> implements DataBase<T> {

    private Connection connection;

    private Statement statement;

    private ResultSet resultSet;

    private LogSystem log;

    private Configer configer;

    @Override
    public void initialize(){
        log= LogSystemFactory.getLogSystem();
        configer = new Configer();
        //log.info(this.getClass().getName(),"初始化数据库连接");
        String dbName=configer.readConfig("database");
        //log.info("连接到数据库:{}",dbName);
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection= DriverManager.getConnection("jdbc:sqlite:"+dbName);
            this.statement=connection.createStatement();
            //设置超时
            this.statement.setQueryTimeout(30);
            log.info("数据库连接初始化完成");
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
    public Object[] getObjects(String command,String tableName,Class clazz){
        try {
            String[] names = getColumnNames(tableName);
            List<Object> list = new ArrayList<>();
            synchronized (this){
                resultSet= statement.executeQuery(command);
                while (resultSet.next()){
                    Object object = clazz.getDeclaredConstructor().newInstance();
                    Field[] fields = object.getClass().getDeclaredFields();
                    for (String name : names){
                        for (Field field : fields){
                            field.setAccessible(true);
                            if(name.replace("_","").equals(field.getName().toLowerCase(Locale.ROOT))){
                                String str = resultSet.getString(name);
                                field.set(object,str);
                            }
                        }
                    }
                    list.add(object);
                }
            }
            return list.toArray();
        } catch (SQLException | NoSuchMethodException throwables) {
            log.error("error:{}---sql:{}",throwables);
            throwables.printStackTrace();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @SneakyThrows
    public Object getOneObject(String command,String tableName,Class clazz){
        try {
            Object object = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = object.getClass().getDeclaredFields();
            String[] names = getColumnNames(tableName);
            synchronized (this){
                resultSet= statement.executeQuery(command);
                while (resultSet.next()){
                    System.out.println(resultSet.getFetchSize());
                    for (String name : names){
                        for (Field field : fields){
                            if(name.replace("_","").equals(field.getName().toLowerCase(Locale.ROOT))){
                                String str = resultSet.getString(name);
                                field.set(object,str);
                            }
                        }
                    }
                    return object;
                }
            }
        } catch (SQLException | NoSuchMethodException throwables) {
            log.error("error:{}---sql:{}",throwables);
            throwables.printStackTrace();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String[] getColumnNames(String tableName){
        String comm = "pragma table_info(\""+tableName+"\")";
        StringBuilder sb = new StringBuilder();
        try {
            resultSet = statement.executeQuery(comm);
            while (resultSet.next()){
                sb.append(resultSet.getString("name")).append("\n");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sb.toString().split("\n");
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