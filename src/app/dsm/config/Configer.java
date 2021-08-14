package app.dsm.config;

import app.log.LogSystem;
import app.log.LogSystemFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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


    /**
     * 远程url
     */
    private String[] remote_url;

    private LogSystem log = LogSystemFactory.getLogSystem();

    public Configer(){
        this.init("./metaconfig.txt");
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
            try {
                synchronized (Configer.class){
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                    String temp;
                    while ((temp = br.readLine())!=null){
                        if(temp.trim().startsWith(propertyName)){
                            return temp.substring(temp.indexOf("=")+1).trim();
                        }
                    }
                    br.close();
                }
            } catch (FileNotFoundException e) {
                log.error(null,e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                log.error(null,e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
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

    public void writeConfig(int mode,String path,String dataType,String brief){
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
                if(mode == 1){
                    bw.write("local_path = ");
                }else {
                    bw.write("remote_path = ");
                }
                bw.write(path);
                bw.flush();
                bw.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
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
