package dsm.compress.impl;

import dsm.compress.MatchMethod;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @ClassName : dsm.compress.impl.LZSS_MT
 * @Description :
 * @Date 2021-05-18 08:50:11
 * @Author ZhangHL
 */
public class LZSS_MT implements Runnable {

    StringBuilder sb;

    String text;

    MatchMethod matchMethod;

    public void init(String text, StringBuilder sb) {
        this.text = text;
        this.sb = sb;
        matchMethod = new KMP();
    }

    @Override
    public void run() {
        //初始化检查
        initCheck(text);
        //一些常数
        //滑动窗口最大长度
        int windowMax = 2333;
        //初始化滑动窗口、前项缓冲区、指针长度
        int window = 0, buffer = text.length() - 1, pointer = 0;
        int[] res;
        int count = 0;
        while (pointer <= buffer) {
            //找到最长匹配字符串
            res = findLongestMatchString(text, window, windowMax, buffer, pointer);
            //如果找到了匹配字符串
            if (res[0] != -1) {
                res[0]+=window;
                //System.out.println(res[0]+" "+res[1]);
                //将匹配项添加到结果集中
                appendMatch(sb, text, res);
                pointer += res[1] ;
            } else {
                //如果没有匹配到字符串
                appendNoMatch(sb, text, pointer);
                pointer++;
            }
            //检测滑动窗口有没有超过最大值
            if (calLen(window, pointer) > windowMax) {
                window += calLen(window, pointer) - windowMax - 1;
            }
            count++;
        }
    }

    public String decode(String text) {
        char[] c_text = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < c_text.length; i++) {
            if (c_text[i] == (char) 65535) {
                int index = c_text[i + 1];
                int offset = c_text[i + 2];
                sb.append(sb.subSequence(index, index + offset ));
                i += 2;
            } else {
                sb.append(c_text[i]);
            }
        }
        return sb.toString();
    }

    public StringBuilder getSb() {
        return sb;
    }

    private int find_count = 0;

    /**
     * 找到最长匹配字符串
     *
     * @param text    文本
     * @param window  滑动窗口，该值应为其起点
     * @param buffer  前项缓冲区，该值为其终点
     * @param pointer 指针，滑动窗口的终点和前项缓冲区的起点
     * @return int 最长匹配字符串在滑动窗口中的位置,int[0]表示其实起始位置，int[1]代表长度
     */
    private int[] findLongestMatchString(String text, int window, int max_window, int buffer, int pointer) {
        //初始化结果集
        int[] res = new int[]{-1, 1};

        //前项缓冲最大容量
        int maxBufferPointer = 512;

        ZHL zhl = new ZHL();
        int front = pointer + maxBufferPointer;
        if (front > text.length()) {
            front = (front - text.length()) + pointer;
        }
        if (front < text.length()) {
            res = zhl.match(text.substring(window, pointer), text.substring(pointer, front));
            if (res[1] < 3) {
                res[0] = -1;
            }
        }
        return res;
    }

    /**
     * 添加匹配项
     *
     * @param sb   结果集
     * @param text 文本
     * @param res  匹配结果
     */
    private void appendMatch(StringBuilder sb, String text, int[] res) {
        char index = (char) res[0];
        char offset = (char) res[1];
        sb.append((char) 65535).append(index).append(offset);
    }

    private void appendNoMatch(StringBuilder sb, String text, int pointer) {
        sb.append(text.charAt(pointer));
    }

    /**
     * 计算区间长度
     *
     * @param fore 起始点
     * @param back 结束点
     * @return int 长度
     */
    private int calLen(int fore, int back) {
        return back - fore + 1;
    }


    /**
     * 初始化检查,检查文本长度等相关问题
     *
     * @return boolean
     */
    private boolean initCheck(String text) {
        return text.length() > 1;
    }

    public void save(String encode) {
        File f = new File(System.currentTimeMillis() +".lzss");
        try {
            f.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write(encode);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
