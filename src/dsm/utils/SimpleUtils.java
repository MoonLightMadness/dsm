package dsm.utils;


import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 简单工具类
 *
 * @ClassName : utils.base.simpleUtils
 * @Description :
 * @Date 2021-03-31 20:11:49
 * @Author 张怀栏
 */
public class SimpleUtils {
    /**
     * 判断字符串是否为空
     *
     * @return 是则返回true，否则返回false
     * @class SimpleUtils
     * @Description //TODO
     * @Param
     * @Author Zhang huai lan
     * @Date 20:20 2021/3/31
     **/
    public static boolean isEmptyString(String str) {
        return (str == null || "".equals(str));
    }

    /**
     * 固定使用TimeFormatter.SEC_LEVEL
     *
     * @return
     * @class *ClassName*
     * @Description //TODO
     * @Param
     * @Author Zhang huai lan
     * @Date 21:23 2021/3/31
     **/
    public static String getTimeStamp() {
        return new SimpleDateFormat(TimeFormatter.MILLISEC_LEVEL).format(new Date());
    }

    /**
     * 序列化
     *
     * @return byte数组
     * @class
     * @Param
     * @Author Zhang huai lan
     * @Date 8:30 2021/4/14
     * @version V1.0
     **/
    public static byte[] serializableToBytes(Serializable obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            byte[] res = baos.toByteArray();
            baos.close();
            oos.close();
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] serializableToBase64(Serializable obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            byte[] res = baos.toByteArray();
            baos.close();
            oos.close();
            return Base64.getEncoder().encode(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反序列化
     *
     * @return Object对象
     * @class
     * @Param
     * @Author Zhang huai lan
     * @Date 8:30 2021/4/14
     * @version V1.0
     **/
    public static Object bytesToSerializableObject(byte[] bytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object obj = ois.readObject();
            bais.close();
            ois.close();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算时间间隔(与现在)
     *
     * @return 与现在时间相差的时间(单位 : 秒)
     * @class
     * @Param
     * @Author Zhang huai lan
     * @Date 8:57 2021/4/17
     * @version V1.0
     **/
    public static long timeCalculator(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(TimeFormatter.MILLISEC_LEVEL);
            Date d = format.parse(date);
            long gap = System.currentTimeMillis() - d.getTime();
            return gap / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }
    /**
     * 计算时间间隔(与现在)
     * @param option 使用TimeFormatter.****_FATOR进行格式化
     * @return
     * @author Zhang huai lan
     * @date 13:51 2021/5/6
     * @version V1.0
     **/
    public static long timeCalculator(String date,int option) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(TimeFormatter.MILLISEC_LEVEL);
            Date d = format.parse(date);
            long gap = System.currentTimeMillis() - d.getTime();
            return gap / option;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 计算给定两时间的时间间隔(d2 - d1)
     *
     * @param
     * @return 单位:秒
     * @author Zhang huai lan
     * @date 13:33 2021/5/6
     * @version V1.0
     **/
    public static long timeCalculator2(String date1, String date2) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(TimeFormatter.MILLISEC_LEVEL);
            Date d1 = format.parse(date1);
            Date d2 = format.parse(date2);
            long gap = d2.getTime() - d1.getTime();
            return gap / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }
    /**
     * 计算给定两时间的时间间隔(d2 - d1)
     * @param option 使用TimeFormatter.****_FATOR进行格式化
     * @return
     * @author Zhang huai lan
     * @date 13:47 2021/5/6
     * @version V1.0
     **/
    public static long timeCalculator2(String date1, String date2,int option){
        try {
            SimpleDateFormat format = new SimpleDateFormat(TimeFormatter.MILLISEC_LEVEL);
            Date d1 = format.parse(date1);
            Date d2 = format.parse(date2);
            long gap = d2.getTime() - d1.getTime();
            return gap / option;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 将普通字符串转为Base64字符串
     */
    public static String string2Base64Str(String str) {
        return new String(Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 将Base64字符串转化为普通字符串
     */
    public static String base64Str2String(String base64Str) {
        return new String(Base64.getDecoder().decode(base64Str.getBytes(StandardCharsets.UTF_8)));
    }

    public static String ConfigRead(String filename,String header){
        String res=null;
        try {
            BufferedReader br=new BufferedReader(new FileReader(new File(filename)));
            String temp;
            while ((temp= br.readLine())!=null){
                temp=temp.toLowerCase(Locale.ROOT);
                temp=temp.replaceAll(" ","\0");
                if(temp.startsWith(header)){
                    res=temp.substring(header.length());
                }
            }
            br.close();
        }catch (FileNotFoundException ffe){
            ffe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;

    }


    /**
     * 调用Shell <br>
     *
     * c 是执行完dir命令后关闭命令窗口。<br>
     * k 是执行完dir命令后不关闭命令窗口。<br>
     * c start 会打开一个新窗口后执行dir指令，原窗口会关闭。<br>
     * k start 会打开一个新窗口后执行dir指令，原窗口不会关闭。<br>
     * @param cmd    cmd
     * @param option 选项
     * @param block 是否阻塞直到运行完毕
     * @return {@link String} 返回值
     */
    public static String callShell(String cmd,String option,boolean block){
        try {
            Process p;
            p = Runtime.getRuntime().exec("cmd /"+option+" "+cmd);
            if(block){
               p.waitFor();
            }
            BufferedReader reader =new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("gbk")));
            String temp;
            StringBuilder builder = new StringBuilder();
            while ((temp=reader.readLine())!=null){
                builder.append(temp).append("\n");
            }
            return builder.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用外部程序
     *
     * @param cmd cmd
     * @return {@link String}
     */
    @Deprecated
    public static String call(String cmd){
        try {
            Process p;
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader reader =new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("gbk")));
            String temp;
            StringBuilder builder = new StringBuilder();
            while ((temp=reader.readLine())!=null){
                builder.append(temp).append("\n");
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字节数组列表合并成一个字节数组
     *
     * @param list  列表
     * @param count 总长度
     * @return {@link byte[]}
     */
    public static byte[] mergeByteList(List<byte[]> list,int count){
        byte[] res = new byte[count];
        int pointer = 0;
        for (byte[] b : list) {
            if (pointer >= count) {
                break;
            }
            System.arraycopy(b, 0, res, pointer, b.length);
            pointer += b.length;
        }
        return res;
    }

}