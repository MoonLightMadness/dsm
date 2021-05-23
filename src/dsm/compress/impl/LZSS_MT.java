package dsm.compress.impl;

import dsm.compress.MatchMethod;

/**
 * @ClassName : dsm.compress.impl.LZSS_MT
 * @Description :
 * @Date 2021-05-18 08:50:11
 * @Author ZhangHL
 */
public class LZSS_MT implements Runnable{

    StringBuilder sb ;

    String text;

    MatchMethod matchMethod;

    public void init(String text,StringBuilder sb){
        this.text=text;
        this.sb=sb;
        matchMethod=new KMP();
    }

    @Override
    public void run() {
        //初始化检查
        initCheck(text);
        //一些常数
        //滑动窗口最大长度
        int windowMax = 4000;
        //初始化滑动窗口、前项缓冲区、指针长度
        int window = 0, buffer = text.length() - 1, pointer = 0;
        int[] res;
        int count=0;
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
            count++;
        }
//        System.out.println(count);
//        System.out.println(find_count);
//        System.out.println(find_count/count);
//        encoded = sb.toString();
//        return encoded;
    }

    public StringBuilder getSb(){
        return sb;
    }

    private int find_count=0;

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
        int maxBufferPointer = 20;
        if (bufferPointer > buffer) {
            return res;
        }
        //使用KMP算法进行字符串匹配
        matchMethod = new KMP();
        //当前项缓冲区指针区间小于缓冲区区间时才查找
        while (calLen(window, pointer) > calLen(pointer, bufferPointer) && calLen(pointer, bufferPointer) < maxBufferPointer) {
            if (bufferPointer >= buffer) {
                break;
            }
            //match()方法返回-1代表无匹配，否则代表匹配下标
            int match = matchMethod.match(text.substring(window, pointer), text.substring(pointer, bufferPointer));
            if (match != -1 && calLen(pointer, bufferPointer) >= 5) {
                res[0] = match;
                res[1] = calLen(pointer, bufferPointer);
            }
            bufferPointer++;
            find_count++;
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
//        sb.append("(").append(res[0]).append(",").append(res[1] -1).append(")");
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
}
