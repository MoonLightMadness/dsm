package app.dsm.novel;

import app.dsm.config.Configer;
import app.utils.SimpleUtils;
import app.utils.net.WebDowloader;
import org.junit.Test;

import java.io.*;
import java.net.ProtocolException;

public class Downloader {

    private Configer configer = new Configer();

    private String novel_basic = null;

    public Downloader(){
        configer.refreshLocal(new File("./metaconfig.txt"));
        novel_basic = configer.readConfig("novel_download_site_basic");
    }

    public String downloadNovel(String id) {
        File f = new File("./"+id+".txt");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = configer.readConfig("novel_download_site")+ id+"/";
        String data = null;
        try {
            data = WebDowloader.downLoad(url);
            //System.out.println(data);
            data = extract(data,id);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    @Test
    public void test1(){
        Downloader downloader =new Downloader();
        System.out.println(downloader.downloadNovel("680"));
    }
    private String extract(String data,String id) {
        String chapterPattern = configer.readConfig("download_chapterPattern");
        String info = configer.readConfig("download_info");
        String chapters = SimpleUtils.match(data, chapterPattern, 1);
        StringBuilder sb = new StringBuilder();
        String href =null;
        String title = null;
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(id+".txt"),true)));
            for (String single : chapters.split("\n")) {
                href = SimpleUtils.match(single,info,1);
                title = SimpleUtils.match(single,info,2);
                //sb.append(title.replaceAll("/n",""));
                //sb.append(extractNovel(novel_basic+href).replaceAll("/n",""));
                writer.write(title);
                writer.write(extractNovel(novel_basic+href));
                Thread.sleep(1);
            }
            writer.flush();
            writer.close();
        } catch (InterruptedException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 进入地址下载章节
     *
     * @param url url
     * @return {@link String}
     */
    private String extractNovel(String url){
        String contentPattern = configer.readConfig("download_contentPattern");
        String data =null;
        try {
            data = WebDowloader.downLoad(url);
            data = SimpleUtils.match(data,contentPattern, 1);
            data = data.replaceAll("<br />","\n");
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
