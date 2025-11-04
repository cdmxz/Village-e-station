package com.ces.village.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StringUtils {
    public static boolean isEmptyOrNull(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 列表去重
     *
     * @param ss
     * @return
     */
    public static List<String> listUnique(List<String> ss) {
        Set<String> set = new HashSet<>(ss);
        return set.stream().toList();
    }

    /**
     * 如果字符串长度超过maxLength，则截取0~maxLength-1
     * 否则返回原字符串
     * @param input
     * @return
     */
    public static String truncate(String input, int maxLength) {
        if (input != null && input.length() > maxLength) {
            return input.substring(0, maxLength);
        }
        return input;
    }
}
