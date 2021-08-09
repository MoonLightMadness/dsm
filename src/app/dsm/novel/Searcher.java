package app.dsm.novel;

import app.dsm.config.Configer;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.SimpleUtils;
import app.utils.net.WebDowloader;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.ProtocolException;

public class Searcher {

    private Configer configer = new Configer();

    private  String searchPath = null;

    private LogSystem log = LogSystemFactory.getLogSystem();

    public Searcher(){
        configer.refreshLocal(new File("./metaconfig.txt"));
        searchPath = configer.readConfig("novel_search_site");
    }

    public String getSearchResult(String name){
        String data = download(name);
        String result = extract(data);
        return result;
    }

    @Test
    public void test1(){
        Searcher searcher = new Searcher();
        System.out.println(searcher.getSearchResult("明"));
    }

    private String download(String name){
        try {
            String data = WebDowloader.downLoad(searchPath+name);
            //System.out.println(data);
            return data;
        } catch (ProtocolException e) {
            log.error(null,e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error(null,e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private String extract(String data){
        String infoPattern = configer.readConfig("search_infoPattern");
        String namePattern1 = configer.readConfig("search_namePattern1");
        String namePattern2 = configer.readConfig("search_namePattern2");
        String catPattern = configer.readConfig("search_catPattern");
        String authorPattern = configer.readConfig("search_authorPattern");
        String uptimePattern = configer.readConfig("search_uptimePattern");
        //System.out.println(infoPattern);
        String info = SimpleUtils.match(data,infoPattern,1);
        StringBuilder sb = new StringBuilder();
        String temp = null;
        for(String single : info.split("\n")){
            //System.out.println(single);
            temp = SimpleUtils.match(single,namePattern1,1);
            sb.append(SimpleUtils.match(temp,namePattern2,1));
            sb.append(SimpleUtils.match(single,namePattern2,2));
            sb.append(SimpleUtils.match(single, catPattern, 1));
            sb.append(SimpleUtils.match(single, authorPattern, 1));
            sb.append(SimpleUtils.match(single, uptimePattern, 1));
            sb.append("\n");
        }
        return sb.toString();
    }

}
