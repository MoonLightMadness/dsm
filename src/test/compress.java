package test;

import app.dsm.compress.Compressor;
import app.dsm.compress.impl.LZSS;
import app.dsm.compress.impl.LZSS_MT;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName : test.compress
 * @Description :
 * @Date 2021-05-17 13:49:15
 * @Author ZhangHL
 */
public class compress {
    @Test
    public void testEncode() {
        LZSS_MT mt = new LZSS_MT();
        String test = "asdzxcmasdzxcjkasdzxiatyughjtyughjnisckoasfoafeasdzxingkjbjvhcdzhgtasdzxdfxtzasfasffchjaa";
        StringBuilder sb = new StringBuilder();
        mt.init(test, sb);
        mt.run();
        String res = sb.toString();
        String decode = mt.decode(res);
        System.out.println(test + " " + test.length());
        System.out.println(res + " " + res.length());
        System.out.println(decode + " " + decode.length());
        Assert.assertEquals(test, decode);
    }

    @Test
    public void testFile_1() {
        Compressor compressor = new LZSS();
        String test = readFile(new File("./LICENSE"));
        long time = System.currentTimeMillis();
        System.out.println("原文长度:" + test.length());
        String res = compressor.encode(test);
        System.out.println("压缩时长:" + (System.currentTimeMillis() - time));
        System.out.println("压缩之后长度:" + res.length());
        System.out.println("比率:" + (100 - ((res.length() * 1.0f) / test.length() * 100)) + "%");
        time = System.currentTimeMillis();
        String decode = compressor.decode(res);
        System.out.println("解压缩时长:" + (System.currentTimeMillis() - time));
        System.out.println(decode.length());
        Assert.assertEquals(test.length(), decode.length());
        compressor.save(res);
    }

    @Test
    public void testFile_2() {
        Compressor compressor = new LZSS();
        String test = readFile(new File("C:\\Users\\Administrator\\Desktop\\entertainment\\java\\app.dsm\\.idea\\workspace.xml"));
        long time = System.currentTimeMillis();
        System.out.println("原文长度:" + test.length());
        String res = compressor.encode(test);
        System.out.println("压缩时长:" + (System.currentTimeMillis() - time));
        System.out.println("压缩之后长度:" + res.length());
        System.out.println("压缩率:" + (100 - ((res.length() * 1.0f) / test.length() * 100)) + "%");
        time = System.currentTimeMillis();
        String decode = compressor.decode(res);
        System.out.println("解压缩时长:" + (System.currentTimeMillis() - time));
        System.out.println(decode.length());
        Assert.assertEquals(test.length(), decode.length());
        compressor.save(res);
    }

    @Test
    public void testFile_3_MT() {
        //多线程测试
        String test = readFile(new File("./LICENSE"));
        //String test = readFile("C:\\Users\\Administrator\\Desktop\\entertainment\\java\\app.dsm\\.idea\\workspace.xml");
        //String test = readFile("./test.db");
        int len = test.length();
        System.out.println("文件长度:" + len);
        //n线程
        int thread_num = 1;
        int cut = len / thread_num;
        Thread[] threads = new Thread[thread_num];
        StringBuilder[] sbs = new StringBuilder[thread_num];
        LZSS_MT[] mts = new LZSS_MT[thread_num];
        for (int i = 0; i < thread_num; i++) {
            mts[i] = new LZSS_MT();
            int start = i * cut;
            int end = start + cut;
            if (i + 1 == thread_num) {
                end = test.length();
            }
            sbs[i] = new StringBuilder();
            String slice = test.substring(start, end);
            mts[i].init(slice, sbs[i]);
            threads[i] = new Thread(mts[i]);
            threads[i].setName("T-" + i);
            threads[i].start();
        }
        //等待结束
        long time = System.currentTimeMillis();
        int[] status = new int[thread_num];
        int point = 0, endSum = 0;
        boolean isEnd = false;
        while (true) {
            if (point >= thread_num) {
                point = 0;
            }
            if (!threads[point].isAlive()) {
                status[point] = 1;
            }
            for (int i = 0; i < status.length; i++) {
                if (status[i] == 1) {
                    endSum++;
                }
            }
            if (endSum == status.length) {
                break;
            } else {
                endSum = 0;
            }
            point++;
        }
        StringBuilder encode = new StringBuilder();
        Compressor compressor = new LZSS();
        for (LZSS_MT mt : mts) {
            encode.append(mt.getSb().toString());
        }
        //System.out.println(encode);
        //compressor.save(encode.toString());
        System.out.println("压缩后长度：" + encode.length());
        System.out.println("压缩率:" + (100 - ((encode.length() * 1.0f) / test.length() * 100)) + "%");
        time = System.currentTimeMillis();
        String decode = compressor.decode(encode.toString());
        System.out.println("解压缩时长:" + (System.currentTimeMillis() - time));
        System.out.println(decode.length());

        Assert.assertEquals(test.length(), decode.length());
    }

    @Test
    public void test() {
        LogSystem log = LogSystemFactory.getLogSystem();
        log.immediatelySaveMode(true);
        File directory = new File("C:\\Users\\qq237\\Desktop\\assets");
        File[] files = directory.listFiles();
        run(files, log);
    }
    @Test
    public void paper_test() {
        LogSystem log = LogSystemFactory.getLogSystem();
        log.immediatelySaveMode(true);
        File directory = new File("C:\\Users\\qq237\\Desktop\\assets");
        File[] files = directory.listFiles();
        for(File f : files){
            String test = readFile(f);
            int len = test.length();
            LZSS_MT mt =new LZSS_MT();
            StringBuilder sb = new StringBuilder();
            mt.init(test,sb);
            long time = System.currentTimeMillis();
            mt.doEncode();
            time = System.currentTimeMillis() - time;
            long encode_end_time = time;
            time = System.currentTimeMillis();
            String encode = sb.toString();
            //String decode = mt.decode(encode.toString());
            //compressor.save(decode.toString());
            log.info(this.getClass().getName(), "文件名:{},文件大小:{}字节,压缩后大小{},压缩时间:{},压缩比:{}",
                    f.getName(),
                    String.valueOf(test.length()),
                    String.valueOf(encode.toString().length()),
                    String.valueOf(encode_end_time),
                    (test.length()/(encode.length()*1.0f ))+ ""
            );
            mt.getHuff().clear();
        }

    }


    private void run(File[] files, LogSystem log) {
        for (File file : files) {
            String test = readFile(file);
            int len = test.length();
            //n线程
            int thread_num = 1;
            int cut = len / thread_num;
            Thread[] threads = new Thread[thread_num];
            StringBuilder[] sbs = new StringBuilder[thread_num];
            LZSS_MT[] mts = new LZSS_MT[thread_num];
            for (int i = 0; i < thread_num; i++) {
                mts[i] = new LZSS_MT();
                int start = i * cut;
                int end = start + cut;
                if (i + 1 == thread_num) {
                    end = test.length();
                }
                sbs[i] = new StringBuilder();
                String slice = test.substring(start, end);
                mts[i].init(slice, sbs[i]);
                threads[i] = new Thread(mts[i]);
                threads[i].setName("T-" + i);
                threads[i].start();
            }
            //等待结束
            int[] status = new int[thread_num];
            int point = 0, endSum = 0;
            boolean isEnd = false;
            long time = System.currentTimeMillis();
            while (true) {
                if (point >= thread_num) {
                    point = 0;
                }
                if (!threads[point].isAlive()) {
                    status[point] = 1;
                }
                for (int i = 0; i < status.length; i++) {
                    if (status[i] == 1) {
                        endSum++;
                    }
                }
                if (endSum == status.length) {
                    break;
                } else {
                    endSum = 0;
                }
                point++;
            }
            long encode_end_time = System.currentTimeMillis() - time;
            StringBuilder encode = new StringBuilder();
            LZSS_MT compressor = new LZSS_MT();
            for (LZSS_MT mt : mts) {
                encode.append(mt.getSb().toString());
            }
            time = System.currentTimeMillis();
            String decode = compressor.decode(encode.toString());
            //compressor.save(decode.toString());
            if (test.length() == decode.length()) {
                log.info(this.getClass().getName(), "文件名:{},文件大小:{}字节,压缩后大小{},压缩时间:{},压缩比:{},解压缩时间:{},解压缩大小:{}",
                        file.getName(),
                        String.valueOf(test.length()),
                        String.valueOf(encode.toString().length()),
                        String.valueOf(encode_end_time),
                        (test.length()/(encode.length()*1.0f ))+ "",
                        String.valueOf(System.currentTimeMillis() - time),
                        String.valueOf(decode.length())
                );
            } else {
                log.error(this.getClass().getName(), "文件名:{},文件大小:{}字节,压缩后大小{},压缩时间:{},压缩比:{},解压缩时间:{}",
                        file.getName(),
                        String.valueOf(test.length()),
                        String.valueOf(encode.toString().length()),
                        String.valueOf(encode_end_time),
                        (test.length()/(encode.length()*1.0f ))+ "",
                        String.valueOf(System.currentTimeMillis() - time));
            }
        }
    }

    private String readFile(File f) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"));
            CharBuffer cb = CharBuffer.allocate((int)f.length());
            int size = br.read(cb);
            br.close();
            char[] res = new char[size];
            System.arraycopy(cb.array(), 0, res, 0, size);
            return String.valueOf(res);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Test
    public void playground() {
        char a = (char) 65535;
        System.out.println(a);
    }
}
