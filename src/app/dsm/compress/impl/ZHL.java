package app.dsm.compress.impl;

import org.junit.Test;

/**
 * @ClassName : app.dsm.compress.impl.ZHL
 * @Description :
 * @Date 2021-05-18 14:08:29
 * @Author ZhangHL
 */
public class ZHL {

    public int[] match(String text, String pattern) {
        //模式串最长前缀匹配
        char[] c_text = text.toCharArray();
        char[] c_pattern = pattern.toCharArray();
        int max = 0, lastIndex = 0, current = 0;
        boolean isMatch = false;
        for (int i = 0; i < c_text.length; i++) {
            for (int j = 0; j < c_pattern.length; j++) {
                if (i + j >= text.length()) {
                    break;
                }
                if (c_text[i + j] == c_pattern[j]) {
                    max = j;
                    if (!isMatch) {
                        lastIndex = i;
                    }
                    isMatch = true;
                } else {
                    isMatch = false;
                    break;
                }
            }
            if (max <= 1) {
                lastIndex = -1;
            }
        }
        return new int[]{lastIndex, max};
    }

    @Test
    public void test() {
        String text = "asdzxcmasdzxcjkasdzxiatyughjtyughjnisckoasfoafeasdzxingkjbjvhcdzhgtasdzxdfxtzasfasffchjaa";
        String pattern = "byughjlk";
        ZHL zhl = new ZHL();
        int[] res = zhl.match(text, pattern);
        System.out.println(res[0] + " " + res[1]);
    }
}
