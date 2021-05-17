package test;

import dsm.compress.Compressor;
import dsm.compress.impl.LZSS;
import dsm.utils.SimpleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

/**
 * @ClassName : test.compress
 * @Description :
 * @Date 2021-05-17 13:49:15
 * @Author ZhangHL
 */
public class compress {
    @Test
    public void testEncode(){
        Compressor compressor = new LZSS();
        String test = "asdzxcmasdzxcjkasdzxiatyughjtyughjnisckoasfoafeasdzxingkjbjvhcdzhgtasdzxdfxtz";
        String res = compressor.encode(test);
        String decode = compressor.decode(res);
        System.out.println(test+" "+test.length());
        System.out.println(res+" "+res.length());
        System.out.println(decode+" "+decode.length());
        Assert.assertEquals(test,decode);
    }
@Test
    public void testFileEncode(){
        Compressor compressor = new LZSS();
        String test = readFile("./.idea/workspace.xml");
        long time = System.currentTimeMillis();
        System.out.println("原文长度:"+test.length());
        String res = compressor.encode(test);
        System.out.println("压缩时长:"+(System.currentTimeMillis()-time));
        System.out.println("压缩之后长度:"+res.length());
        System.out.println("比率:"+((res.length()*1.0f)/test.length()*100)+"%");
        time = System.currentTimeMillis();
        String decode = compressor.decode(res);
        System.out.println("解压缩时长:"+(System.currentTimeMillis()-time));
        System.out.println(decode.length());
        Assert.assertEquals(test,decode);
    }

    private String readFile(String path){
        try {
            File f= new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(f));
            StringBuilder sb =new StringBuilder();
            String temp;
            while ((temp=reader.readLine())!=null){
                sb.append(temp);
            }
            reader.close();
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
