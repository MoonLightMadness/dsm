package app.dsm.bili;

import app.dsm.config.Configer;
import org.junit.Test;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class Spider {
    BiliSite target;
    public Spider(BiliSite bs){
        target=bs;
    }
    public String downLoad() throws IOException, ProtocolException {
        HttpURLConnection huc;
        StringBuilder data=new StringBuilder() ;
        while(true){
            huc=null;
            huc = (HttpURLConnection) target.getUrl().openConnection();
            huc.setRequestMethod("GET");
            huc.setDoInput(true);
            huc.setDoOutput(true);
            huc.setConnectTimeout(8000);
            huc.setReadTimeout(10000);
            huc.setInstanceFollowRedirects(false);
            huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");
            huc.setRequestProperty("cookie",new Configer().readConfig("bili.cookie"));
            try {
                int code = huc.getResponseCode();
                if (code == STATECODE.NORMAL.getId()) {
                    if ("gzip".equals(huc.getContentEncoding())) {
                        GZIPInputStream gzis = new GZIPInputStream(huc.getInputStream());
                        byte[] gdata = new byte[1024];
                        int len = 0;
                        StringBuffer sb = new StringBuffer();
                        while ((len = (gzis.read(gdata))) != 0) {
                            if (len == -1) {
                                break;
                            }
                            sb.append(new String(gdata, 0, len, StandardCharsets.UTF_8));
                        }
                        return String.valueOf(sb);
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(huc.getInputStream(), StandardCharsets.UTF_8));
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        data.append(temp).append("/n");
                    }
                    break;
                } else {
                    if (code==STATECODE.BANNED.getId()) {
                        throw new IOException("You are banned by this website");
                    }
                    if (code == STATECODE.REDIRECT.getId()) {
                        System.out.println("Code:"+STATECODE.REDIRECT.getId()+"\tRedirect...");
                        target.setUrl(new URL("https:" + huc.getHeaderField("Location")));
                    }

                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                break;
            }

        }
        return data.toString();
    }


}
