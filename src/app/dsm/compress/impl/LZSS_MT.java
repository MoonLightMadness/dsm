package app.dsm.compress.impl;

import app.dsm.compress.Fnode;
import app.dsm.compress.MatchMethod;
import app.utils.special.RTimer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : app.dsm.compress.impl.LZSS_MT
 * @Description :
 * @Date 2021-05-18 08:50:11
 * @Author ZhangHL
 */
public class LZSS_MT implements Runnable {

    StringBuilder sb;

    String text;

    MatchMethod matchMethod;

    List<Fnode> fnodes;

    List<HuffmanTree.Node> huff;

    public void init(String text, StringBuilder sb) {
        this.text = text;
        this.sb = sb;
        matchMethod = new KMP();
        fnodes = new ArrayList<>();
        huff = new ArrayList<>();
    }

    @Override
    public void run() {
        this.doEncode();
    }

    public void doEncode() {
        //初始化检查
        initCheck(text);
        //一些常数
        //滑动窗口最大长度
        int windowMax = 1000;
        //初始化滑动窗口、前项缓冲区、指针长度
        int window = 0, buffer = text.length() - 1, pointer = 0;
        int[] res;
        //RTimer rTimer = new RTimer();
        //rTimer.start();
        while (pointer <= buffer) {
            //找到最长匹配字符串
            res = findLongestMatchStringInKMP(text, window, windowMax, buffer, pointer);
            //如果找到了匹配字符串
            if (res[0] != -1) {
                res[0] += window;
                //System.out.println(res[0] + " " + res[1]);
                if(huff.size()>64){
                    huff.remove(huff.size() - 1);
                }
                if (!isHuffmanMatch) {
                    this.newTree(text.substring(res[0], res[0] + res[1]), res);
                    isHuffmanMatch = false;
                }
                isHuffmanMatch = false;
                //将匹配项添加到结果集中
                appendMatch(sb, text, res);
                pointer += res[1];
                fnodes.add(new Fnode(res[0], res[1]));
            } else {
                //如果没有匹配到字符串
                appendNoMatch(sb, text, pointer);
                pointer++;
            }
            //检测滑动窗口有没有超过最大值
            if (calLen(window, pointer) > windowMax) {
                window += calLen(window, pointer) - windowMax - 1;
            }
        }
    }

    public String decode(String text) {
        char[] c_text = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < c_text.length; i++) {
            if (c_text[i] == (char) 65535) {
                int index = c_text[i + 1];
                int offset = c_text[i + 2];
                sb.append(sb.subSequence(index, index + offset));
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
        int maxBufferPointer = 256;

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
     * 找到最长匹配字符串
     *
     * @param text    文本
     * @param window  滑动窗口，该值应为其起点
     * @param buffer  前项缓冲区，该值为其终点
     * @param pointer 指针，滑动窗口的终点和前项缓冲区的起点
     * @return int 最长匹配字符串在滑动窗口中的位置,int[0]表示其实起始位置，int[1]代表长度
     */
    private int[] findLongestMatchStringInBM(String text, int window, int max_window, int buffer, int pointer) {
        //初始化结果集
        int[] res = new int[]{-1, 1};

        //前项缓冲最大容量
        int maxBufferPointer = 256;
        int sentence = pointer + 1;
        int maxLen = 0, index = 0;
        while (sentence < text.length() && (sentence - pointer) < maxBufferPointer) {
            if ((index = BM.indexOf(text.substring(window, pointer), text.substring(pointer, sentence))) != -1) {
                maxLen = sentence - pointer;
                sentence++;
                continue;
            } else {
                if (maxLen > 3) {
                    res[0] = index;
                    res[1] = maxLen;
                    return res;
                } else {
                    res[0] = -1;
                    return res;
                }
            }
        }

        return res;
    }

    boolean isHuffmanMatch = false;

    /**
     * 找到最长匹配字符串
     *
     * @param text    文本
     * @param window  滑动窗口，该值应为其起点
     * @param buffer  前项缓冲区，该值为其终点
     * @param pointer 指针，滑动窗口的终点和前项缓冲区的起点
     * @return int 最长匹配字符串在滑动窗口中的位置,int[0]表示其实起始位置，int[1]代表长度
     */
    private int[] findLongestMatchStringInKMP(String text, int window, int max_window, int buffer, int pointer) {
        //初始化结果集
        int[] res = new int[]{-1, 1};
        //前项缓冲区指针
        int bufferPointer = pointer + 2;
        //前项缓冲最大容量
        int maxBufferPointer = 16;
        if (bufferPointer > buffer) {
            return res;
        }
        //先在哈夫曼树中搜素,如果能找到且长度大于3，则直接返回
        while (calLen(window, pointer) > calLen(pointer, bufferPointer) && calLen(pointer, bufferPointer) < maxBufferPointer) {
            int huffmansize = 8;
            if (bufferPointer<text.length()&&calLen(pointer, bufferPointer) >= huffmansize) {
                //System.out.println(pointer+" "+bufferPointer);
                res = this.searchInHuffmanTree(text.substring(pointer, bufferPointer));
                if (res[0] != -1) {
                    //System.out.println("我day到rua！");
                    isHuffmanMatch = true;
                    return res;
                }
            }
            bufferPointer++;
        }
        bufferPointer = pointer + 2;
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

    private void newTree(String str, int[] sign) {
        HuffmanTree.Node<String> node = new HuffmanTree.Node<>(str, 1);
        int[] newSign = new int[]{sign[0], sign[1]};
        node.sign = newSign;
        huff.add(node);
        HuffmanTree.quickSort(huff);
    }

    private int[] searchInHuffmanTree(String pattern) {
        int[] res = new int[]{-1, 1};
        for (HuffmanTree.Node<String> node : huff) {
            if (pattern.length() > node.sign[1]) {
                break;
            }
            if (node.data != null && node.data.equals(pattern)) {
                node.weight += 1;
                res = node.sign;
                HuffmanTree.quickSort(huff);
                return res;
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
        File f = new File(System.currentTimeMillis() + ".lzss");
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

    public List<HuffmanTree.Node> getHuff() {
        return huff;
    }

    public void setHuff(List<HuffmanTree.Node> huff) {
        this.huff = huff;
    }
}
