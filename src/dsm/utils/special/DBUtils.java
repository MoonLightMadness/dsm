package dsm.utils.special;

import dsm.db.DataBase;
import dsm.db.impl.SqlReader;
import dsm.db.impl.SqliteFactory;
import dsm.security.SecurityConstant;

/**
 * @ClassName : dsm.utils.special.DBUtils
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
