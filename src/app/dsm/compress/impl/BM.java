package app.dsm.compress.impl;

import java.util.Arrays;

public class BM {
    // BM算法匹配字符串，匹配成功返回P在S中的首字符下标，匹配失败返回-1
    public static int indexOf(String source, String pattern) {
        char[] src = source.toCharArray();
        char[] ptn = pattern.toCharArray();
        int sLen = src.length;
        int pLen = ptn.length;

        // 模式串为空字符串，返回0
        if (pLen == 0) {
            return 0;
        }
        // 主串长度小于模式串长度，返回-1
        if (sLen < pLen) {
            return -1;
        }

        int[] BC = buildBadCharacter(ptn);
        int[] GS = buildGoodSuffix(ptn);

        // 从尾部开始匹配，其中i指向主串，j指向模式串
        for (int i = pLen - 1; i < sLen; ) {
            int j = pLen - 1;
            for (; src[i] == ptn[j]; i--, j--) {
                if (j == 0) {   // 匹配成功返回首字符下标
                    return i;
                }
            }

            // 每次后移“坏字符规则”和“好后缀规则”两者的较大值
            // 注意此时i（坏字符）已经向前移动，所以并非真正意义上的规则
            i += Math.max(BC[src[i]], GS[pLen - 1 - j]);
        }

        return -1;
    }

    // 坏字符规则表
    private static int[] buildBadCharacter(char[] pattern) {
        int pLen = pattern.length;
        final int CHARACTER_SIZE = 256; // 英文字符的种类，2^8
        int[] BC = new int[CHARACTER_SIZE]; // 记录坏字符出现时后移位数

        Arrays.fill(BC, pLen);  // 默认后移整个模式串长度
        for (int i = 0; i < pLen - 1; i++) {
            int ascii = pattern[i];  // 当前字符对应的ASCII值
            BC[ascii] = pLen - 1 - i;   // 对应的后移位数，若重复则以最右边为准
        }

        return BC;
    }

    // 非真正意义上的好字符规则表，后移位数还加上了当前好后缀的最大长度
    private static int[] buildGoodSuffix(char[] pattern) {
        int pLen = pattern.length;
        int[] GS = new int[pLen];   // 记录好后缀出现时后移位数
        int lastPrefixPos = pLen;   // 好后缀的首字符位置

        for (int i = pLen - 1; i >= 0; i--) {
            // 判断当前位置（不含）之后是否是好后缀，空字符也是好后缀
            if (isPrefix(pattern, i + 1)) {
                lastPrefixPos = i + 1;
            }
            // 如果是好后缀，则GS=pLen，否则依次为pLen+1、pLen+2、...
            GS[pLen - 1 - i] = lastPrefixPos - i + pLen - 1;
        }

        // 上面在比较好后缀时，是从模式串的首字符开始的，但实际上好后缀可能出现在模式串中间。
        // 比如模式串EXAMPXA，假设主串指针在比较P时发现是坏字符，那么XA就是好后缀，
        // 虽然它的首字符X与模式串的首字符E并不相等。此时suffixLen=2表示将主串指针后移至模式串末尾，
        // pLen-1-i=4表示真正的好字符规则，同样主串指针后移，使得模式串前面的XA对齐主串的XA
        for (int i = 0; i < pLen - 1; i++) {
            int suffixLen = suffixLength(pattern, i);
            GS[suffixLen] = pLen - 1 - i + suffixLen;
        }

        return GS;
    }

    // 判断是否是好后缀，即模式串begin（含）之后的子串是否匹配模式串的前缀
    private static boolean isPrefix(char[] pattern, int begin) {
        for (int i = begin, j = 0; i < pattern.length; i++, j++) {
            if (pattern[i] != pattern[j]) {
                return false;
            }
        }

        return true;
    }

    // 返回模式串中以pattern[begin]（含）结尾的后缀子串的最大长度
    private static int suffixLength(char[] pattern, int begin) {
        int suffixLen = 0;

        int i = begin;
        int j = pattern.length - 1;
        while (i >= 0 && pattern[i] == pattern[j]) {
            suffixLen++;
            i--;
            j--;
        }

        return suffixLen;
    }
}
