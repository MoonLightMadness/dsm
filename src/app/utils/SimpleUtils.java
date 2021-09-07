package app.utils;


import app.dsm.exception.ServiceException;
import app.dsm.exception.UniversalErrorCodeEnum;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.parser.impl.JSONParserImpl;
import app.utils.datastructure.XByteBuffer;
import app.utils.domain.Variable;
import lombok.SneakyThrows;


import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单工具类
 *
 * @ClassName : utils.base.simpleUtils
 * @Description :
 * @Date 2021-03-31 20:11:49
 * @Author ZhangHL
 */
public class SimpleUtils {

    private static LogSystem log = LogSystemFactory.getLogSystem();


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
     * 使用TimeFormatter获取时间
     *
     * @param format 格式
     * @return {@link String}
     */
    public static String getTimeStamp2(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * 获取偏移量的时间戳
     *
     * @param format 格式
     * @param level  水平
     * @param offset 位移 -n代表前推 n表示后推
     * @return {@link String}
     */
    public static String getTimeStamp(String format, int level, int offset) {
        String today = getTimeStamp2(format);
        try {
            long d = new SimpleDateFormat(format).parse(today).getTime();
            d += ((long) level * offset);
            Date date = new Date(d);
            return new SimpleDateFormat(format).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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
            oos.writeObject(null);
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
            Object obj;
            while ((obj = ois.readObject()) != null) {
                bais.close();
                ois.close();
                return obj;
            }
            //Object obj = ois.readObject();
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
     *
     * @param option 使用TimeFormatter.****_FATOR进行格式化
     * @return
     * @author Zhang huai lan
     * @date 13:51 2021/5/6
     * @version V1.0
     **/
    public static long timeCalculator(String date, int option) {
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
     *
     * @param option 使用TimeFormatter.****_FATOR进行格式化
     * @return
     * @author Zhang huai lan
     * @date 13:47 2021/5/6
     * @version V1.0
     **/
    public static long timeCalculator2(String date1, String date2, int option) {
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


    /**
     * 调用Shell <br>
     * <p>
     * c 是执行完dir命令后关闭命令窗口。<br>
     * k 是执行完dir命令后不关闭命令窗口。<br>
     * c start 会打开一个新窗口后执行dir指令，原窗口会关闭。<br>
     * k start 会打开一个新窗口后执行dir指令，原窗口不会关闭。<br>
     *
     * @param cmd    cmd
     * @param option 选项
     * @param block  是否阻塞直到运行完毕
     * @return {@link String} 返回值
     */
    public static String callShell(String cmd, String option, boolean block) {
        try {
            Process p;
            p = Runtime.getRuntime().exec("cmd /" + option + " " + cmd);
            if (block) {
                p.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("gbk")));
                String temp;
                StringBuilder builder = new StringBuilder();
                while ((temp = reader.readLine()) != null) {
                    builder.append(temp).append("\n");
                }
                return builder.toString();
            }

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
    public static String call(String cmd) {
        try {
            Process p;
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("gbk")));
            String temp;
            StringBuilder builder = new StringBuilder();
            while ((temp = reader.readLine()) != null) {
                builder.append(temp).append("\n");
            }
            p.destroy();
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
    public static byte[] mergeByteList(List<byte[]> list, int count) {
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

    public static byte[] receiveDataInNIO(SocketChannel socketChannel) {
        int standard = 1024;
        XByteBuffer xb = new XByteBuffer();
        ByteBuffer buffer = ByteBuffer.allocate(standard);
        int size;
        while (true) {
            buffer.clear();
            try {
                size = socketChannel.read(buffer);
                if (size > 0) {
                    if (size == standard) {
                        buffer.flip();
                        xb.append(buffer.array());
                    } else {
                        byte[] rb = new byte[size];
                        buffer.flip();
                        System.arraycopy(buffer.array(), 0, rb, 0, size);
                        xb.append(rb);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    socketChannel.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                break;
            }
            if (size <= 0) {
                break;
            }
        }
        return xb.getBytes();
    }

    public static String stringFormatter(String[] titles, String[]... args) {
        StringBuilder sb = new StringBuilder();
        int count = args.length;
        int[] max = new int[count];
        int temp = 0;
        for (int i = 0; i < count; i++) {
            String[] ss = args[i];
            for (String s : ss) {
                if (s != null) {
                    temp = s.length();
                }
                if (max[i] < temp) {
                    max[i] = temp;
                }
            }
            temp = 0;
        }
        for (int i = 0; i < titles.length; i++) {
            sb.append(titles[i]).append(getSpaces(max[i] - titles[i].length() + 5));
        }
        sb.append("\n");
        for (int i = 0; i < args[0].length; i++) {
            for (int j = 0; j < titles.length; j++) {
                sb.append(args[j][i]).append(getSpaces(max[j] - args[j][i].length() + 5));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static String getSpaces(int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * 使用正则匹配(使用\n作为分隔符)
     *
     * @param text    文本
     * @param pattern 模式
     * @return {@link String}
     */
    public static String match(String text, String pattern, int index) {
        StringBuilder name = new StringBuilder();
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        while (m.find()) {
            name.append(m.group(index)).append("\n");
        }
        return name.toString();
    }

    public static byte[] readFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            log.error(null, "can not find {}", path);
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] content = new byte[fis.available()];
            fis.read(content);
            fis.close();
            return content;
        } catch (FileNotFoundException e) {
            log.error(null, e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFile(String path, byte[] content) {
        File file = new File(path);
        if (!file.exists()) {
            log.error(null, "can not find {}", path);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(content);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            log.error(null, e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算从0000-01-01到给定时间段的天数
     *
     * @param year  一年
     * @param month 月
     * @param day   一天
     * @return @return long
     * @author zhl
     * @date 2021/08/09
     * @version V1.0
     */
    public static long calEpochDay(long year, long month, long day) {
        long y = year;
        long m = month;
        long total = 0;
        total += 365 * y;
        if (y >= 0) {
            total += (y + 3) / 4 - (y + 99) / 100 + (y + 399) / 400;
        } else {
            total -= y / -4 - y / -100 + y / -400;
        }
        total += ((367 * m - 362) / 12);
        total += day - 1;
        if (m > 2) {
            total--;
            if (!isLeapYear(year)) {
                total--;
            }
        }
        return total;
    }

    private static boolean isLeapYear(long prolepticYear) {
        return ((prolepticYear & 3) == 0) && ((prolepticYear % 100) != 0 || (prolepticYear % 400) == 0);
    }

    /**
     * 复制一个新的对象
     *
     * @param obj obj
     * @return @return {@link Object }
     * @author zhl
     * @date 2021-08-11 10:12
     * @version V1.0
     */
    @SneakyThrows
    public static Object duplicate(Object obj) {
        try {
            log.info("属性复制开始，入参:{}", obj);
            Class clazz = obj.getClass();
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object newer = constructor.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(obj).getClass().isPrimitive() || (field.get(obj).getClass() == String.class)
                        || (field.get(obj).getClass() == Integer.class)) {
                    field.set(newer, field.get(obj));
                } else {
                    field.set(newer, duplicate(field.get(obj)));
                }
            }
            return newer;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("属性复制失败，原因:{}", e);
            throw new ServiceException(UniversalErrorCodeEnum.UEC_010002.getCode(), UniversalErrorCodeEnum.UEC_010002.getMsg());

        }

    }

    public static String getFilePathSeparator() {
        return System.getProperty("file.separator");
    }




    public Variable getVariable(Object obj) {
        Variable variable = new Variable();
        try {
            Class clazz = obj.getClass();
            variable.setName(clazz.getSimpleName());
            variable.setFullName(clazz.getTypeName());
            variable.setValue(obj);
        } catch (Exception ex) {
            log.error("获取变量属性失败，原因：{}", ex);
        }
        return variable;
    }

    public boolean hasClassAnnotation(Class clazz, Class annotationType) {
        Annotation annotation = clazz.getDeclaredAnnotation(annotationType);
        if (null != annotation && annotation.annotationType() == annotationType) {
            return true;
        }
        return false;
    }

    public boolean hasMethodAnnotaion(Method method, Class annotationType) {
        Annotation annotation = method.getDeclaredAnnotation(annotationType);
        if (null != annotation && annotation.annotationType() == annotationType) {
            return true;
        }
        return false;
    }

    /**
     * 获取类级别注解中value的值
     *
     * @param clazz          clazz
     * @param annotationType 注释类型
     * @return @return {@link String }
     * @author zhl
     * @date 2021-08-23 14:00
     * @version V1.0
     */
    public String getClassAnnotationValue(Class clazz, Class annotationType) {
        if (hasClassAnnotation(clazz, annotationType)) {
            Annotation annotation = clazz.getDeclaredAnnotation(annotationType);
            try {
                Method method = annotation.getClass().getDeclaredMethod("value");
                return (String) method.invoke(annotation);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("获取注解值失败，原因：{}", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取方法级别的注解的value的值
     *
     * @param method         方法
     * @param annotationType 注释类型
     * @return @return {@link String }
     * @author zhl
     * @date 2021-08-23 14:33
     * @version V1.0
     */
    public String getMethodAnnotationValue(Method method, Class annotationType) {
        if (hasMethodAnnotaion(method, annotationType)) {
            Annotation annotation = method.getDeclaredAnnotation(annotationType);
            try {
                Method me = annotation.getClass().getDeclaredMethod("value");
                return (String) me.invoke(annotation);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error("获取注解值失败，原因：{}", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 快速排序
     *
     * @param array 数组
     * @param start 开始
     * @param end   结束
     * @return @return {@link int[] }
     * @author zhl
     * @date 2021-08-23 15:13
     * @version V1.0
     */
    public static int[] qsort(int array[], int start, int end) {
        int pivot = array[start];
        int i = start;
        int j = end;
        while (i < j) {
            while ((i < j) && (array[j] > pivot)) {
                j--;
            }
            while ((i < j) && (array[i] < pivot)) {
                i++;
            }
            if ((array[i] == array[j]) && (i < j)) {
                i++;
            } else {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        if (i - 1 > start) {
            array = qsort(array, start, i - 1);
        }
        if (j + 1 < end) {
            array = qsort(array, j + 1, end);
        }
        return array;
    }

    /**
     * 对对象数值进行排序
     * 排序标志：Field
     * @param array 数组
     * @param field 排序标志，该字段类型必须为数值类型
     * @param start 开始
     * @param end   结束
     * @return @return {@link Object[] }
     * @author zhl
     * @date 2021-08-23 15:51
     * @version V1.0
     */
    public static Object[] qsortObjects(Object[] array, Field field, int start, int end) {
        long pivot = parseToLong(array[start],field);
        int i = start;
        int j = end;
        while (i < j) {
            while ((i < j) && (parseToLong(array[j],field) > pivot)) {
                j--;
            }
            while ((i < j) && (parseToLong(array[i],field)  < pivot)) {
                i++;
            }
            if ((parseToLong(array[i],field)  == parseToLong(array[j],field) ) && (i < j)) {
                i++;
            } else {
                Object temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        if (i - 1 > start) {
            array = qsortObjects(array, field, start, i - 1);
        }
        if (j + 1 < end) {
            array = qsortObjects(array, field, j + 1, end);
        }
        return array;
    }

    /**
     * 对对象数值进行排序
     * 排序标志：Field
     * @param objects 对象
     * @param field   具有基本数值类型的字段
     * @return @return {@link List<Object> }
     * @author zhl
     * @date 2021-08-23 16:08
     * @version V1.0
     */
    public static List<Object> qsortObjects(List<Object> objects,Field field){
        Object[] os = objects.toArray();
        os = qsortObjects(os,field,0,os.length-1);
        return Arrays.asList(os);
    }

    private static long parseToLong(Object object,Field field){
        return Long.valueOf(String.valueOf(getObjectFieldValue(object, field)));
    }

    public static Object getObjectFieldValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            log.error("获取字段值失败，原因:{}", e);
            e.printStackTrace();
        }
        return null;
    }

    public static String[] addressCutter(String address){
        // example: /127.0.0.1:10000
        address = address.substring(1);
        String[] result = new String[2];
        result[0] = address.split(":")[0];
        result[1] = address.split(":")[1];
        return result;
    }

    public static Object parseTo(byte[] json,Class clazz){
        return new JSONParserImpl().parser(json,clazz);
    }

    /**
     * 终结Java服务 <br/>
     * windows系统使用
     * @param serviceName 服务名称
     * @return @return {@link String }
     * @author zhl
     * @date 2021-09-06 13:46
     * @version V1.0
     */
    public static String terminateJavaService(String serviceName){
        String str = "jps | find /I \""+serviceName+"\"";
        String res = SimpleUtils.callShell(str,"c",true);
        res = res.split(" ")[0].trim();
        res = SimpleUtils.callShell("taskkill /F /PID "+res,"c",true);
        return res;
    }

}
