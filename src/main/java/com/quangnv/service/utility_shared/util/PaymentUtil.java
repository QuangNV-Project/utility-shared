package com.quangnv.service.utility_shared.util;

import com.quangnv.service.utility_shared.dto.ParsedPaymentDescription;
import com.quangnv.service.utility_shared.dto.VietQrConfigDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class PaymentUtil {
    private static final String vietQrTemplate = "https://img.vietqr.io/image/<BANK_ID>-<ACCOUNT_NO>-<TEMPLATE>.png?amount=<AMOUNT>&addInfo=<DESCRIPTION>&accountName=<ACCOUNT_NAME>";
    public static final String urlGetBankInfo = "https://api.vietqr.io/v2/banks";
    private static final Pattern DESCRIPTION_PATTERN =
            Pattern.compile("(.+)_([A-Z0-9]{6})");

    private static final String RANDOM_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int RANDOM_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generatePaymentString(String userName) {
        StringBuilder sb = new StringBuilder(userName.length() + 1 + RANDOM_LENGTH);
        sb.append(userName).append("_");

        for (int i = 0; i < RANDOM_LENGTH; i++) {
            int index = RANDOM.nextInt(RANDOM_CHARS.length());
            sb.append(RANDOM_CHARS.charAt(index));
        }

        return sb.toString();
    }

    public static String generateQrUrl(VietQrConfigDto config, BigDecimal amount) {
        Map<String, String> values = Map.of(
                "<BANK_ID>", config.getBankId(),
                "<ACCOUNT_NO>", config.getAccountNo(),
                "<TEMPLATE>", config.getTemplate(),
                "<ACCOUNT_NAME>", encodeValue(config.getAccountName()),
                "<DESCRIPTION>", encodeValue(config.getDescription()),
                "<AMOUNT>", amount.toPlainString()
        );

        String result = vietQrTemplate;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }

        return result;
    }

    // Nếu có ký tự đặc biệt, encode để URL hợp lệ
    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return value;
        }
    }

    public static ParsedPaymentDescription parseDescription(String description) {
        Matcher matcher = DESCRIPTION_PATTERN.matcher(description);
        if (matcher.matches()) {
            String userName = matcher.group(1);
            String code = matcher.group(2);
            return new ParsedPaymentDescription(userName, code);
        } else {
            throw new IllegalArgumentException("Invalid description format: " + description);
        }
    }

}
