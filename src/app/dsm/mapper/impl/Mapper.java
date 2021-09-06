package app.dsm.mapper.impl;

import app.dsm.config.Configer;
import app.dsm.db.DataBase;
import app.dsm.db.impl.SqliteImpl;
import app.dsm.mapper.AbstractMapper;
import app.dsm.mapper.annotation.TableName;
import app.dsm.server.vo.CalculatorReqVO;
import app.dsm.server.vo.UserAuthReqDTO;
import app.utils.special.RTimer;
import lombok.Data;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Mapper extends AbstractMapper {

    DataBase dataBase;

    /**
     * 表的名字
     */
    String tName;

    /**
     * 字段名
     */
    String[] columnNames;

    private Configer configer = new Configer();

    /**
     * 初始化Mapper
     *
     * @return
     * @author zhl
     * @date 2021-08-20 22:59
     * @version V1.0
     */
    public void initialize(Class clazz) {
        initialize(clazz,configer.readConfig("sys.name")+".db");
    }

    /**
     * 初始化Mapper
     * @param dbName 数据库的名字
     * @return
     * @author zhl
     * @date 2021-09-06 10:21
     * @version V1.0
     */
    public void initialize(Class clazz,String dbName) {
        dataBase = new SqliteImpl<>();
        dataBase.initialize(dbName);
        getTableName(clazz);
    }

    private void getTableName(Class clazz) {
        TableName tableName = (TableName) clazz.getDeclaredAnnotation(TableName.class);
        if(tableName != null){
            this.tName = tableName.value();
        }else {
            //如果没有@TableName注释则采用类名作为表名进行解析
            parseOriginalClassName(clazz.getName());
        }
    }

    private void parseOriginalClassName(String name){
        char[] cname = name.toCharArray();
        cname[0] = String.valueOf(cname[0]).toLowerCase(Locale.ROOT).toCharArray()[0];
        this.tName = convertPOJOToDBType(String.valueOf(cname));
    }

    @Override
    public void save(Object object) {
        List<String> list = getNameAndValues(object);
        String columnsList = list.get(0);
        String valuesList = list.get(1);
        dataBase.insert("INSERT INTO " + tName + "(" + columnsList + ")" + " VALUES(" + valuesList + ");");
    }

    private String convertPOJOToDBType(String property) {
        char[] seq = property.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : seq) {
            //如果是该字母是大写字母则变为小写且在前面加入下划线
            if (Character.isUpperCase(c)) {
                sb.append("_").append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 将字段和它的值切割到两个字符串中 <br/>
     * 第一个字符串是字段名<br/>
     * 第二个字符串是它的值<br/>
     * 它们是一一对应的关系<br/>
     *
     * @param object 对象
     * @return @return {@link List<String> }
     * @author zhl
     * @date 2021-08-20 23:41
     * @version V1.0
     */
    private List<String> getNameAndValues(Object object) {
        List<String> result = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder fName = new StringBuilder();
        StringBuilder fValue = new StringBuilder();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String str = convertPOJOToDBType(field.getName());
                String value = (String) field.get(object);
                if (null != value) {
                    fName.append(str).append(",");
                    fValue.append("'").append(value).append("'").append(",");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        String columnsList = fName.substring(0, fName.length() - 1);
        String valuesList = fValue.substring(0, fValue.length() - 1);
        result.add(columnsList);
        result.add(valuesList);
        return result;
    }

    @Override
    public void saveBatch(List<Object> list) {
        for (Object obj : list) {
            this.save(obj);
        }
    }

    @Override
    public Object selectOne(Object object, Object condition) {
        return dataBase.getOneObject("SELECT " + getSelectString(object) + " FROM " + tName + " WHERE " + getWhereString(condition), tName, object.getClass());
    }

    @Override
    public Object[] selectList(Object object, Object condition) {
        return dataBase.getObjects("SELECT " + getSelectString(object) + " FROM " + tName + " WHERE " + getWhereString(condition), tName, object.getClass());
    }

    @Override
    public void update(Object object, Object condition) {
        dataBase.update("UPDATE " + this.tName + " SET " + getSetString(object) + " WHERE " + getWhereString(condition) + ";");
    }

    private String getSelectString(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {
            field.setAccessible(true);
            String str = convertPOJOToDBType(field.getName());
            sb.append(str).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private String getSetString(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String str = convertPOJOToDBType(field.getName());
                String value = (String) field.get(object);
                if (null != value) {
                    sb.append(str).append("= '").append(value).append("',");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sb.substring(0, sb.length() - 1);
    }

    private String getWhereString(Object condition) {
        Field[] fields = condition.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String str = convertPOJOToDBType(field.getName());
                String value = (String) field.get(condition);
                if (null != value) {
                    sb.append(str).append("= '").append(value).append("' AND ");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sb.substring(0, sb.length() - 4);
    }

    @Override
    public void updateBatch(List<Object> list, List<Object> conditions) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            this.update(list.get(i), conditions.get(i));
        }
    }

    @Override
    public void delete(Object condition) {
        //校验condition不为空
        if(null != condition){
            dataBase.delete("DELETE FROM " + tName + " WHERE " + getWhereString(condition));
        }
    }
}

