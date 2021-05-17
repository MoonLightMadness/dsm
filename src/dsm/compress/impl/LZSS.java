package dsm.compress.impl;

import dsm.compress.Compressor;
import dsm.compress.MatchMethod;
import org.omg.PortableServer.POA;

/**
 * @ClassName : dsm.compress.impl.LZSS
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
        int windowMax = 1024 * 1024;
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
                pointer += res[1] -1;
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
            if (c_text[i] == '(' ) {
                int len=i;
                for (int p=i+1;;p++){
                    if(c_text[p]==')'){
                        len = p - i + 1;
                        break;
                    }
                }
                String[] nums = text.substring(i+1,len+i-1).split(",");
                sb.append(sb.subSequence(Integer.parseInt(nums[0]),Integer.parseInt(nums[0])+Integer.parseInt(nums[1])));
                i+=text.substring(i,len+i).length()-1;
            }else {
                sb.append(c_text[i]);
            }
        }
        return sb.toString();
    }

    @Override
    public void save() {

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
        int bufferPointer = pointer + 5;
        //前项缓冲最大容量
        int maxBufferPointer=buffer;
        if (bufferPointer > buffer) {
            return res;
        }
        //使用KMP算法进行字符串匹配
        matchMethod = new KMP();
        int maxLen = 0;
        //当前项缓冲区指针区间小于缓冲区区间时才查找
        while (calLen(window, pointer) > calLen(pointer, bufferPointer) && calLen(pointer,bufferPointer)<maxBufferPointer) {
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
        sb.append("(").append(res[0]).append(",").append(res[1] -1).append(")");
    }

    private void appendNoMatch(StringBuilder sb, String text, int pointer) {
        sb.append(text.substring(pointer, pointer + 1));
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
     * 检测当前左括号是否是压缩指标
     *
     * @param c_text     char[]文本
     * @param startIndex 开始下标
     * @return boolean
     */
    private boolean isCompress(char[] c_text, int startIndex) {
        boolean hasNum = false, hasComma = false;
        while (++startIndex < c_text.length) {
            if (c_text[startIndex] == ',') {
                hasComma = true;
                continue;
            }
            if (c_text[startIndex] == ')' && hasComma && hasNum) {
                return true;
            }
            try {
                Integer.parseInt(String.valueOf(c_text[startIndex]));
                hasNum = true;
            } catch (NumberFormatException e) {
                break;
            }

        }
        return false;

    }


}
