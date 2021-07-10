package app.dsm.config;

public class Argument {

    public static String getValue(String args,String key){
        String value = null;
        int index = args.indexOf("-"+key);
        index += key.length()+2;
        int last = args.indexOf(" -",index);
        value = args.substring(index,last);
        return value;
    }

}
