package app.dsm.db.impl;

import java.io.*;
import java.util.Locale;

/**
 * sql语句读取器
 * @ClassName : app.dsm.db.impl.SqlReader
 * @Description :
 * @Date 2021-05-04 22:55:17
 * @Author ZhangHL
 */
public class SqlReader {

    public void init(){

    }

    public String get(String... args){
        String res=null;
        String header=args[0];
        try {
            BufferedReader reader=new BufferedReader(new FileReader(new File("sql.txt")));
            String temp;
            while ((temp=reader.readLine())!=null){
                temp=temp.toLowerCase(Locale.ROOT);
                String t2=temp.replaceAll(" ","");
                if(t2.startsWith(header)){
                    res=messageHandler(temp.substring(temp.indexOf('=')+1),args);
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private String messageHandler(String msg,String... args){
        int argsLen=args.length;
        StringBuilder sb=new StringBuilder();
        if(argsLen>0){
            int pointer=0;
            /*============================================================
             *  算法：
             * 每次循环脱离一对{}
             * 若参数大于占位符数时保留{}
             *============================================================*/
            for (int i = 1; i < argsLen; i++) {
                pointer=msg.indexOf('{');
                if(pointer!=-1){
                    sb.append(msg, 0, pointer);
                    sb.append('\'').append(args[i]).append('\'');
                    msg=msg.substring(pointer+2);
                }
            }
            sb.append(msg);
        }else {
            sb.append(msg);
        }
        return sb.toString();
    }
}
