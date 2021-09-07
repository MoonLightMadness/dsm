package app.dsm.config;

import app.log.LogSystem;
import app.log.LogSystemFactory;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Configer {


    /**
     * 模式
     * 1-本地模式 2-远程模式
     */
    private int mode = 1;


    /**
     * 本地目录地址
     */
    private String[] local_directory;


    private Pattern callPattern = Pattern.compile("\\$\\{(.*?)}");

    /**
     * 远程url
     */
    private String[] remote_url;

    private LogSystem log = LogSystemFactory.getLogSystem();

    public Configer(){
        this.init("./metaconfig.txt");
    }

    /**
     * @param metaPath 元配置文件路径
     * @return @return {@link  }
     * @author zhl
     * @date 2021-09-06 10:13
     * @version V1.0
     */
    public Configer(String metaPath){
        this.init(metaPath);
    }

    /**
     * 初始化
     * 在指定路径下创建配置目录
     *
     * @param path 路径
     */
    public void init(String path) {
        //检查元配置文件
        File f = new File(path);
        try {
            if (f.exists()) {
                readLocalPath(f);
            } else {
                log.info("元配置文件不存在，新建元配置文件");
                f.createNewFile();
            }
        } catch (IOException e) {
            log.error(null,e.getMessage());
            e.printStackTrace();
        }
    }

    public String readConfig(String propertyName) {
        for (String path : local_directory){
            File f = new File(path);
            if (!f.exists()) {
                log.error("在指定的路径上找不到该文件--path:{}",path);
                return null;
            }
            BufferedReader br = null;
            try {
                synchronized (Configer.class){
                   br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                    String temp;
                    while ((temp = br.readLine())!=null){
                        if(temp.trim().startsWith(propertyName)){
                            return callReplacer(temp.substring(temp.indexOf("=")+1).trim());
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                log.error(null,e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                log.error(null,e.getMessage());
                e.printStackTrace();
            }finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.error("未找到属性{}的值",propertyName);
        return null;
    }

    private String callReplacer(String str){
        String res = null;
        if(str != null){
            Matcher matcher = callPattern.matcher(str);
            while (matcher.find()){
                String temp = readConfig(matcher.group(1));
                if(temp != null){
                    res = str.replace("${"+matcher.group(1)+"}",temp);
                }
            }
            if(res != null){
                Matcher check = callPattern.matcher(res);
                if(check.find()){
                    res = callReplacer(res);
                }
            }else {
                res = str;
            }
        }
        return res;
    }

    public String readConfig(String propertyName,String... args) {
        for (String path : local_directory){
            File f = new File(path);
            if (!f.exists()) {
                log.error("在指定的路径上找不到该文件--path:{}",path);
                return null;
            }
            BufferedReader br = null;
            try {
                synchronized (Configer.class){
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                    String temp;
                    while ((temp = br.readLine())!=null){
                        if(temp.trim().startsWith(propertyName)){
                            return messageHandler(temp.substring(temp.indexOf("=")+1).trim(),args);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                log.error(null,e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                log.error(null,e.getMessage());
                e.printStackTrace();
            }finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.error("未找到属性{}的值",propertyName);
        return null;
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
            for (int i = 0; i < argsLen; i++) {
                pointer=msg.indexOf('{');
                if(pointer!=-1){
                    sb.append(msg, 0, pointer);
                    sb.append(args[i].toString());
                    msg=msg.substring(pointer+2);
                }
            }
            sb.append(msg);
        }else {
            sb.append(msg);
        }
        return sb.toString();
    }

    public List<String> readConfigList(String propertyName) {
        List<String> result = new ArrayList<>();
        for (String path : local_directory){
            File f = new File(path);
            if (!f.exists()) {
                log.error("在指定的路径上找不到该文件--path:{}",path);
                return null;
            }
            try {
                synchronized (Configer.class){
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                    String temp;
                    while ((temp = br.readLine())!=null){
                        if(temp.trim().startsWith(propertyName)){
                            result.add(temp.substring(temp.indexOf("=")+1).trim());
                        }
                    }
                    br.close();
                }
            } catch (IOException e) {
                log.error(null,e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * @param path     路径
     * @param key
     * @param value
     * @param dataType 数据类型 可空
     * @param brief    注释 可空
     * @return
     * @author zhl
     * @date 2021-09-04 16:16
     * @version V1.0
     */
    public void writeConfig(String path,String key,String value,String dataType,String brief){
        File f = new File(path);
        if(!f.exists()){
            log.error("在指定的路径上找不到该文件");
            return;
        }
        try {
            synchronized (Configer.class){
                BufferedWriter bw = new BufferedWriter(new FileWriter(f,true));
                bw.newLine();
                bw.write("# "+brief);
                bw.newLine();
                bw.write("# DataType:"+dataType);
                bw.newLine();
                bw.write(key+" = "+value);
                bw.newLine();
                bw.flush();
                bw.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

    }

    public void updateConfig(String key,String newValue,String path){
        File f = new File(path);
        if (!f.exists()) {
            log.error("在指定的路径上找不到该文件--path:{}",path);
        }
        try {
            StringBuilder sb = new StringBuilder();
            synchronized (Configer.class){
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                String temp;
                while ((temp = br.readLine())!=null){
                    if(!temp.trim().startsWith(key)){
                        sb.append(temp).append("\n");
                    }else {
                        sb.append(key).append(" = ").append(newValue).append("\n");
                    }
                }
                br.close();
                write(f, sb.toString());
            }
        } catch (FileNotFoundException e) {
            log.error(null,e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error(null,e.getMessage());
            e.printStackTrace();
        }
    }

    private void write(File f,String data){
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f,false)));
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshLocal(File f){
        readLocalPath(f);
    }

    public void refreshRemote(File f){
        readRemote(f);
    }

    private void readLocalPath(File f) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String temp = null;
            synchronized (Configer.class){
                while ((temp = br.readLine()) != null) {
                    //跳过注释
                    if(temp.startsWith("#")){
                        continue;
                    }
                    if (temp.trim().startsWith("local_path")) {
                        sb.append(temp.split("=")[1].trim()).append("\n");
                    }
                }
                br.close();
            }
            local_directory = sb.toString().split("\n");
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void readRemote(File f){
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String temp = null;
            synchronized (Configer.class){
                while ((temp = br.readLine()) != null) {
                    //跳过注释
                    if(temp.startsWith("#")){
                        continue;
                    }
                    if (temp.trim().startsWith("remote_path")) {
                        sb.append(temp.split("=")[1]).append("\n");
                    }
                }
                br.close();
            }
            remote_url = sb.toString().split("\n");
        } catch (FileNotFoundException e) {
            log.error(null,e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error(null,e.getMessage());
            e.printStackTrace();
        }
    }

    private String pathConstructor(String path) {
        return local_directory + path;
    }

}
