package test;

import dsm.compress.Compressor;
import dsm.compress.impl.LZSS;
import dsm.compress.impl.LZSS_MT;
import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
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
    public void testFile_1(){
        Compressor compressor = new LZSS();
        String test = readFile("./LICENSE");
        long time = System.currentTimeMillis();
        System.out.println("原文长度:"+test.length());
        String res = compressor.encode(test);
        System.out.println("压缩时长:"+(System.currentTimeMillis()-time));
        System.out.println("压缩之后长度:"+res.length());
        System.out.println("比率:"+(100-((res.length()*1.0f)/test.length()*100))+"%");
        time = System.currentTimeMillis();
        String decode = compressor.decode(res);
        System.out.println("解压缩时长:"+(System.currentTimeMillis()-time));
        System.out.println(decode.length());
        Assert.assertEquals(test.length(),decode.length());
        compressor.save(res);
    }

    @Test
    public void testFile_2(){
        Compressor compressor = new LZSS();
        String test = readFile("C:\\Users\\Administrator\\Desktop\\entertainment\\java\\dsm\\.idea\\workspace.xml");
        long time = System.currentTimeMillis();
        System.out.println("原文长度:"+test.length());
        String res = compressor.encode(test);
        System.out.println("压缩时长:"+(System.currentTimeMillis()-time));
        System.out.println("压缩之后长度:"+res.length());
        System.out.println("压缩率:"+(100-((res.length()*1.0f)/test.length()*100))+"%");
        time = System.currentTimeMillis();
        String decode = compressor.decode(res);
        System.out.println("解压缩时长:"+(System.currentTimeMillis()-time));
        System.out.println(decode.length());
        Assert.assertEquals(test.length(),decode.length());
        compressor.save(res);
    }
    @Test
    public void testFile_3_MT(){
        //多线程测试
        String test = readFile("./LICENSE");
        //String test = readFile("C:\\Users\\Administrator\\Desktop\\entertainment\\java\\dsm\\.idea\\workspace.xml");
        //String test = readFile("./test.db");
        int len = test.length();
        System.out.println("文件长度:"+len);
        //n线程
        int thread_num = 8;
        int cut = len/thread_num;
        Thread[] threads = new Thread[thread_num];
        StringBuilder[] sbs = new StringBuilder[thread_num];
        LZSS_MT[] mts = new LZSS_MT[thread_num];
        for (int i = 0; i < thread_num; i++) {
            mts[i] = new LZSS_MT();
            int start = i*cut;
            int end = start+cut;
            if(i+1==thread_num ){
                end = test.length();
            }
            sbs[i]=new StringBuilder();
            String slice = test.substring(start,end);
            mts[i].init(slice,sbs[i]);
            threads[i]=new Thread(mts[i]);
            threads[i].setName("T-"+i);
            threads[i].start();
        }
        //等待结束
        long time =System.currentTimeMillis();
        int[] status=new int[thread_num];
        int point=0,endSum=0;
        boolean isEnd=false;
        while (true){
            if(point>=thread_num){
                point=0;
            }
            if(!threads[point].isAlive()){
                status[point]=1;
            }
            for (int i = 0; i < status.length; i++) {
                if(status[i]==1){
                    endSum++;
                }
            }
            if(endSum== status.length){
                break;
            }else {
                endSum=0;
            }
            point++;
        }
        StringBuilder encode=new StringBuilder();
        Compressor compressor = new LZSS();
        for(LZSS_MT mt:mts){
            encode.append(mt.getSb().toString());
        }
        //compressor.save(encode.toString());
        System.out.println("压缩后长度："+encode.length());
        System.out.println("压缩率:"+(100-((encode.length()*1.0f)/test.length()*100))+"%");
        time = System.currentTimeMillis();
        String decode = compressor.decode(encode.toString());
        System.out.println("解压缩时长:"+(System.currentTimeMillis()-time));
        System.out.println(decode.length());

        Assert.assertEquals(test.length(),decode.length());
    }
    @Test
    public void test(){
        LogSystem log = LogSystemFactory.getLogSystem();
        log.immediatelySaveMode(true);
        String[] files=new String[]{
                //"./LICENSE",
                //"./test.db",
                //"C:\\Users\\Administrator\\Desktop\\entertainment\\java\\dsm\\.idea\\workspace.xml",
                //"C:\\Users\\Administrator\\Documents\\Study\\毕业设计\\1.docx,"
                "./1.flac"
        };
        run(files,log);
    }

    private void run(String[] files, LogSystem log){
        for(String file:files){
            String test = readFile(file);
            int len = test.length();
            //n线程
            int thread_num = 4;
            int cut = len/thread_num;
            Thread[] threads = new Thread[thread_num];
            StringBuilder[] sbs = new StringBuilder[thread_num];
            LZSS_MT[] mts = new LZSS_MT[thread_num];
            for (int i = 0; i < thread_num; i++) {
                mts[i] = new LZSS_MT();
                int start = i*cut;
                int end = start+cut;
                if(i+1==thread_num ){
                    end = test.length();
                }
                sbs[i]=new StringBuilder();
                String slice = test.substring(start,end);
                mts[i].init(slice,sbs[i]);
                threads[i]=new Thread(mts[i]);
                threads[i].setName("T-"+i);
                threads[i].start();
            }
            //等待结束
            int[] status=new int[thread_num];
            int point=0,endSum=0;
            boolean isEnd=false;
            long time =System.currentTimeMillis();
            while (true){
                if(point>=thread_num){
                    point=0;
                }
                if(!threads[point].isAlive()){
                    status[point]=1;
                }
                for (int i = 0; i < status.length; i++) {
                    if(status[i]==1){
                        endSum++;
                    }
                }
                if(endSum== status.length){
                    break;
                }else {
                    endSum=0;
                }
                point++;
            }
            long encode_end_time = System.currentTimeMillis() -time;
            StringBuilder encode=new StringBuilder();
            Compressor compressor = new LZSS();
            for(LZSS_MT mt:mts){
                encode.append(mt.getSb().toString());
            }
            time = System.currentTimeMillis();
            String decode = compressor.decode(encode.toString());
            if(test.length()==decode.length()){
                log.info(this.getClass().getName(),"文件名:{},文件大小:{}字节,压缩后大小{},压缩时间:{},压缩率:{},解压缩时间:{}",
                        new File(file).getName(),
                        String.valueOf(test.length()),
                        String.valueOf(encode.toString().length()),
                        String.valueOf(encode_end_time),
                        (100-((encode.length()*1.0f)/test.length()*100))+"%",
                        String.valueOf(System.currentTimeMillis()-time)
                        );
            }else {
                log.error(this.getClass().getName(),"文件名:{},文件大小:{}字节,压缩后大小{},压缩时间:{},压缩率:{},解压缩时间:{}",
                        new File(file).getName(),
                        String.valueOf(test.length()),
                        String.valueOf(encode.toString().length()),
                        String.valueOf(encode_end_time),
                        (100-((encode.length()*1.0f)/test.length()*100))+"%",
                        String.valueOf(System.currentTimeMillis()-time));
            }
        }
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
    @Test
    public void playground(){
        char a =(char) 65535;
        System.out.println(a)  ;
    }
}
