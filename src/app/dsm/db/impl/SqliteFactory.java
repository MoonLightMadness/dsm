package app.dsm.db.impl;

/**
 * @ClassName : app.dsm.db.impl.SqliteFactory
 * @Description :
 * @Date 2021-05-05 12:26:41
 * @Author ZhangHL
 */
public class SqliteFactory {
    private static SqliteImpl sqlite;

    public static SqliteImpl getInstance(){
        if(sqlite==null){
            sqlite=new SqliteImpl();
            sqlite.initialize();
        }
        return sqlite;
    }
}
