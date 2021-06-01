package app.dsm.compress.impl;

import app.dsm.compress.Compressor;
import app.dsm.compress.MatchMethod;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @ClassName : app.dsm.compress.impl.LZSS
 * @Description :
 * @Date 2021-05-17 10:35:41
 * @Author ZhangHL
 */
public class LZSS implements Compressor {

    private String encoded;

    private String decoded;

    private MatchMethod matchMethod;

    @Override
    public String encode(String text) {
        //初始化检查
        initCheck(text);
        //一些常数
        //滑动窗口最大长度
        int windowMax = 2048;
        //初始化滑动窗口、前项缓冲区、指针长度
        int window = 0, buffer = text.length() - 1, pointer = 0;
        int[] res;
        StringBuilder sb = new StringBuilder();
        while (pointer <= buffer) {
            //找到最长匹配字符串
            res = findLongestMatchString(text, window, buffer, pointer);
            //如果找到了匹配字符串
            if (res[0] != -1) {
                //将匹配项添加到结果集中
                appendMatch(sb, text, res);
                pointer += res[1] - 1;
                //检测滑动窗口有没有超过最大值
                if (calLen(window, pointer) > windowMax) {
                    window += calLen(window, pointer) - windowMax;
                }
            } else {
                //如果没有匹配到字符串
                appendNoMatch(sb, text, pointer);
                pointer++;
            }
        }
        encoded = sb.toString();
        return encoded;
    }

    @Override
    public String decode(String text) {
        char[] c_text = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < c_text.length; i++) {
            if (c_text[i] == (char)65535) {
                int index = c_text[i + 1];
                int offset = c_text[i + 2];
//                if(offset==0||(index+offset)>sb.length()){
//                    continue;
//                }
                //System.out.println(index+" "+offset+" "+sb.length());
                sb.append(sb.subSequence(index, index + offset - 1));
                i+=2;
            } else {
                sb.append(c_text[i]);
            }
        }
        return sb.toString();
    }

    @Override
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

    /**
     * 初始化检查,检查文本长度等相关问题
     *
     * @return boolean
     */
    private boolean initCheck(String text) {
        return text.length() > 1;
    }

    /**
     * 找到最长匹配字符串
     *
     * @param text    文本
     * @param window  滑动窗口，该值应为其起点
     * @param buffer  前项缓冲区，该值为其终点
     * @param pointer 指针，滑动窗口的终点和前项缓冲区的起点
     * @return int 最长匹配字符串在滑动窗口中的位置,int[0]表示其实起始位置，int[1]代表长度
     */
    private int[] findLongestMatchString(String text, int window, int buffer, int pointer) {
        //初始化结果集
        int[] res = new int[]{-1, 1};
        //前项缓冲区指针
        int bufferPointer = pointer + 2;
        //前项缓冲最大容量
        int maxBufferPointer = 128;
        if (bufferPointer > buffer) {
            return res;
        }
        //使用KMP算法进行字符串匹配
        matchMethod = new KMP();
        int maxLen = 0;
        //当前项缓冲区指针区间小于缓冲区区间时才查找
        while (calLen(window, pointer) > calLen(pointer, bufferPointer) && calLen(pointer, bufferPointer) < maxBufferPointer) {
            if (bufferPointer >= buffer) {
                break;
            }
            //match()方法返回-1代表无匹配，否则代表匹配下标
            int match = matchMethod.match(text.substring(window, pointer), text.substring(pointer, bufferPointer));
            if (match != -1 && calLen(pointer, bufferPointer) >= 3) {
                res[0] = match;
                res[1] = calLen(pointer, bufferPointer);
            }
            bufferPointer++;
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
        //二进制：00011000
        //
        //十进制：24
        //
        //十六进制：18
        //
        //缩写：CAN (Cancel)
        //
        //含义：取消
        sb.append((char) 24).append(index).append(offset);
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


}
