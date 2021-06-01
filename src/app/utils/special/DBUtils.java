package app.utils.special;

import app.dsm.db.DataBase;
import app.dsm.db.impl.SqlReader;
import app.dsm.db.impl.SqliteFactory;

/**
 * @ClassName : app.utils.special.DBUtils
 * @Description :
 * @Date 2021-05-06 17:23:44
 * @Author ZhangHL
 */
public class DBUtils {
    public static String getSK(String accessKey){
        String sk = null;
        DataBase dataBase= SqliteFactory.getInstance();
        dataBase.init();
        SqlReader reader=new SqlReader();
        reader.init();
        sk = (String) dataBase.get(reader.get(SpecialContants.GET_SK,accessKey));
        return sk;
    }

    public static String getServiceNameByAK(String accessKey){
        String name = null;
        DataBase dataBase= SqliteFactory.getInstance();
        dataBase.init();
        SqlReader reader=new SqlReader();
        reader.init();
        name = (String) dataBase.get(reader.get(SpecialContants.GET_NAME_BY_AK,accessKey));
        return name;
    }
}
