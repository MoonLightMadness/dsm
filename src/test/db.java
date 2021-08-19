package test;

import app.dsm.db.DataBase;
import app.dsm.db.SqlBuilder;
import app.dsm.db.impl.SqlBuilderImpl;
import app.dsm.db.impl.SqlReader;
import app.dsm.db.impl.SqliteImpl;
import org.junit.Test;

/**
 * @ClassName : test.db
 * @Description :
 * @Date 2021-05-04 14:40:36
 * @Author ZhangHL
 */
public class db {
    @Test
    public void dbTest1() {
        DataBase dataBase = new SqliteImpl();
        dataBase.initialize();
        String insertTest = "insert into test1 values('1','aaa')";
        String searchTest = "select name_ from test1 where id='1'";
        String updateTest = "update test1 set name_='bbb' where id='1'";
        String deleteTest = "delete from test1 where id='1'";
        dataBase.insert(insertTest);
        System.out.println(dataBase.get(searchTest));
        dataBase.update(updateTest);
        System.out.println(dataBase.get(searchTest));
        dataBase.delete(deleteTest);

    }

    @Test
    public void dbTest2() {
        DataBase dataBase = new SqliteImpl();
        dataBase.initialize();
        SqlReader reader = new SqlReader();
        reader.init();
        //System.out.println(reader.get("insert","test","a","111"));
        dataBase.insert(reader.get("insert", "a", "111"));
        System.out.println(dataBase.get(reader.get("get", "a")));
    }

    @Test
    public void sqlBuilderTest1() {
        SqlBuilder builder = new SqlBuilderImpl();
        long time = System.currentTimeMillis();
        builder.setTable("test_1").select("id").where("name","zhl").and().where("id",1);
        System.out.println(builder);
        builder.reset().setTable("test__2").update("name","aaa").where("id",1);
        System.out.println(builder);
        builder.reset().setTable("test_3").delete("testColumn","aaa").where("name","zhl");
        System.out.println(builder);
        builder.reset().setTable("test_4").insert("id",123).insert("name","zhl");
        System.out.println(builder);
        time = System.currentTimeMillis() -time;
    }

    @Test
    public void test4(){
        DataBase dataBase = new SqliteImpl();
        dataBase.initialize();
        String s = (String) dataBase.get("select * from auth_user_config where id =1");
        System.out.println(s);

    }
}
