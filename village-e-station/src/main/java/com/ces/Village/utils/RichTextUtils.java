package com.ces.Village.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 富文本支持
 */
public class RichTextUtils {
    private static Pattern p_image= Pattern.compile("<img.*src\\s*=\\s*(.*?)[^>]*?>",Pattern.CASE_INSENSITIVE);
    private static Pattern r_image=Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)");


    /**
     * 提取富文本中纯文本
     */
    public static String getText(String richText) {
        String regx = "(<.+?>)|(</.+?>)";
        Matcher matcher = Pattern.compile(regx).matcher(richText);
        while (matcher.find()) {
            // 替换图片
            richText = matcher.replaceAll("").replace(" ", "");
        }
        return richText;
    }

    /**
     * 提取富文本中图片地址
     */
    public static List<String> getImgUrls(String richText) {
        List<String> list = new ArrayList<>();
        Matcher pMatcher = p_image.matcher(richText);
        while (pMatcher.find()) {
            // 得到<img />数据
            String img = pMatcher.group();
            // 匹配<img>中的src数据
            Matcher rMatcher = r_image.matcher(img);
            while (rMatcher.find()) {
                list.add(rMatcher.group(1));
            }
        }
        return list;
    }

}
