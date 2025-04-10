package k_webtoons.k_webtoons.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils {
    // SHA-256을 사용하여 파일 이름을 해시화하고 확장자는 .jpg로 고정
    public static String generateHashedFileName(String originalFileName) throws NoSuchAlgorithmException {
        // 파일 이름을 해시화
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(originalFileName.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();

        // 바이트 배열을 16진수 문자열로 변환
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        // 해시값 뒤에 .jpg 확장자를 추가하여 반환
        return hexString.toString() + ".jpg";
    }
}

