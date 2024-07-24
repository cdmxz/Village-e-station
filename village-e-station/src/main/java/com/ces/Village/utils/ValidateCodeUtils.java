package com.ces.Village.utils;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


/**
 * 随机生成验证码工具类
 */
@Component
@Log4j2
public class ValidateCodeUtils {
    /**
     * 管理员图片验证码
     */
    private static final Random random = new Random();
    private final int width = 165; //验证码的宽
    private final int height = 45; //验证码的高
    private final int lineSize = 30; //验证码中夹杂的干扰线数量
    private final int randomStrNum = 4; //验证码字符个数

    private final String randomString = "023456789ABCDEFGHJKLMNOPQRSTUVWSYZ";

    //字体的设置
    private Font getFont() {
        return new Font("Times New Roman", Font.BOLD, 40);
    }

    //颜色的设置
    private static Color getRandomColor(int fc, int bc) {

        fc = Math.min(fc, 255);
        bc = Math.min(bc, 255);

        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 12);

        return new Color(r, g, b);
    }

    //干扰线的绘制
    private void drawLine(Graphics g) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(20);
        int yl = random.nextInt(10);
        g.drawLine(x, y, x + xl, y + yl);

    }

    //随机字符的获取
    private String getRandomString(int num) {
        num = num > 0 ? num : randomString.length();
        return String.valueOf(randomString.charAt(random.nextInt(num)));
    }

    //字符串的绘制
    private String drawString(Graphics g, String randomStr, int i) {
        g.setFont(getFont());
        g.setColor(getRandomColor(108, 190));
        //System.out.println(random.nextInt(randomString.length()));
        String rand = getRandomString(random.nextInt(randomString.length()));
        randomStr += rand;
        g.translate(random.nextInt(3), random.nextInt(6));
        g.drawString(rand, 40 * i + 10, 30);
        return randomStr;
    }

    /**
     * 生成随机图片
     *
     * @param response
     * @return
     */
    public String getRandomCodeImage(HttpServletResponse response) {
//        HttpSession session = request.getSession();
        // BufferedImage类是具有缓冲区的Image类，Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.fillRect(0, 0, width, height);
        g.setColor(getRandomColor(105, 189));
        g.setFont(getFont());
        // 干扰线
        for (int i = 0; i < lineSize; i++) {
            drawLine(g);
        }
        // 随机字符
        String randomStr = "";
        for (int i = 0; i < randomStrNum; i++) {
            randomStr = drawString(g, randomStr, i);
        }
        g.dispose();
        try {
            //  将图片以png格式返回，返回的是图片
            ImageIO.write(image, "PNG", response.getOutputStream());
        } catch (Exception e) {
            log.error("无法生成验证码图片：{}", e.getMessage());
        }
        return randomStr;
    }

    /**
     * 随机生成4位数字符串验证码
     * @return
     */
    public static String generateValidateCode(){
        Random rdm = new Random();
        // 生成4位随机验证码
        return  Integer.toString(1000 + rdm.nextInt(9000));
    }
}
