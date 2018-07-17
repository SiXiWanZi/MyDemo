package com.whut.demo.utils;

/**
 * <pre>
 *  desc:
 *  Created by 忘尘无憾 on 2017/10/11.
 *  version:
 * </pre>
 */

public class RegexUtil {
    public static boolean isPhoneNum(String phone) {
        return true;
    }

    /**
     * 是否字符串为null或者为空
     *
     * @param str
     * @return 为空true 否则false
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 是否字符串不为空或者为null
     *
     * @param str
     * @return 不为空true 否则false
     */
    public static boolean isNotNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    /**
     * 校验字符串是否全是中文
     *
     * @param s 被校验的字符串
     * @return true代表字符串全是中文
     */
    public static boolean isChinese(String s) {
        boolean res = true;
        char[] zf = s.toCharArray();
        for (char c : zf) {
            if (!isChinese(c)) {
                res = false;
                break;
            }
        }
        return res;

    }

    /**
     * 校验字符串是否不全是中文
     * <br />即代表校验是否有非中文字符
     *
     * @param s 被校验的字符串
     * @return true代表字符串中有不是中文的字符
     */
    public static boolean isNotChinese(String s) {
        return !isChinese(s);
    }

    /**
     * 判断字符是否是中文
     *
     * @param c 被校验的字符
     * @return true表示是汉字
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
