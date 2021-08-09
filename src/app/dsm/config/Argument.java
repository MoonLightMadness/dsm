package app.dsm.config;

public class Argument {

    /**
     * 获得属性值
     * 例如：输入"-testProperty property -other ohter" testProperty  输出property
     * @param args 参数
     * @param key  键值
     * @return {@link String}
     */
    public static String getValue(String args,String key){
        String value = null;
        int index = args.indexOf("-"+key);
        index += key.length()+2;
        int last = args.indexOf(" -",index);
        value = args.substring(index,last);
        return value;
    }

}
