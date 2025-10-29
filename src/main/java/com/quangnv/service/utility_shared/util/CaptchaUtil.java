package com.quangnv.service.utility_shared.util;

import com.quangnv.service.utility_shared.constant.CommonConstant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;


@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CaptchaUtil {

    @NonFinal
    static Random random;

    private String generateRandomText(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Số từ 0-9
        }
        return sb.toString();
    }

    private static  String generateImage(String text) {
        BufferedImage image = new BufferedImage(CommonConstant.WIDTH, CommonConstant.HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Vẽ nền màu nâu nhạt
        g2d.setColor(new Color(204, 153, 102)); // #cc9966
        g2d.fillRect(0, 0, CommonConstant.WIDTH, CommonConstant.HEIGHT);

        // Vẽ chữ số với màu và kiểu ngẫu nhiên
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(Color.BLACK);
        int x = 10;
        for (char c : text.toCharArray()) {
            g2d.drawString(String.valueOf(c), x, 30);
            x += 30;
        }

        // Thêm đường cong làm rối (noise)
        g2d.setColor(new Color(222, 222, 108)); // #dede6c
        int x1 = random.nextInt(CommonConstant.WIDTH);
        int y1 = random.nextInt(CommonConstant.HEIGHT);
        int x2 = random.nextInt(CommonConstant.WIDTH);
        int y2 = random.nextInt(CommonConstant.HEIGHT);
        g2d.drawLine(x1, y1, x2, y2);

        // Chuyển BufferedImage thành base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo CAPTCHA", e);
        } finally {
            g2d.dispose();
        }
    }
}
