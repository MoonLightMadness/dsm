package app.dsm.bili;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor {
    private String content;
    private Pattern pName = Pattern.compile("blank\" class=\"title\">(.*?)</a>");
    private Pattern pUrl = Pattern.compile("<div class=\"info\"><a href=\"//www.(.*?)\" target=\"_blank\" class=\"title\"");
    private Pattern pAu = Pattern.compile(" author\"></i>(.*?)</span>");
    private Pattern pPoint = Pattern.compile("<div class=\"pts\"><div>(.*?)</div>综合得分");
    //private Pattern pTags=Pattern.compile("<li class=\"tag\"><a href=(.*?) target=\"_blank\">(.*?)</a>");
    private Pattern pTags = Pattern.compile("\"tag_name\":\"(.*?)\"");
    public Extractor(String c){
        content=c;
    }
    public String getTitle(){
        StringBuilder name=new StringBuilder();
        Matcher m=pName.matcher(content);
        while (m.find()){
            name.append(m.group(1)).append("\n");
        }
        return name.toString();
    }
    public String getUrl(){
        StringBuilder url=new StringBuilder();
        Matcher m=pUrl.matcher(content);
        while (m.find()){
            url.append("https://www.").append(m.group(1)).append("\n");
        }
        return url.toString();
    }
    public String getAuthor(){
        StringBuilder author=new StringBuilder();
        Matcher m=pAu.matcher(content);
        while (m.find()){
            author.append(m.group(1)).append("\n");
        }
        return author.toString().replaceAll("/n","").replaceAll(" ","");
    }
    public String getPoint(){
        StringBuilder point=new StringBuilder();
        Matcher m=pPoint.matcher(content);
        while (m.find()){
            point.append(m.group(1)).append("\n");
        }
        return point.toString();
    }
    public String getTags(String content){
        StringBuilder tags=new StringBuilder();
        try {
            Matcher m=pTags.matcher(content);
            while (m.find()){
                tags.append(m.group(1)).append("&&");
            }
        }
        catch (Exception e){

            return null;
        }
        return tags.toString();
    }
}
