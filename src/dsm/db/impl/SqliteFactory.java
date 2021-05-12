package dsm.db.impl;

/**
 * @ClassName : dsm.db.impl.SqliteFactory
 * @Description :
 * @Date 2021-05-05 12:26:41
 * @Author 张怀栏
 */
public class SqliteFactory {
    private static SqliteImpl sqlite;

    public static SqliteImpl getInstance(String... args){
        if(sqlite==null){
            sqlite=new SqliteImpl();
            sqlite.init(args);
        }
        return sqlite;
    }
}
