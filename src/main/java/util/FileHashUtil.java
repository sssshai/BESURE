package util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileHashUtil {

    /**
     * 计算文件哈希值得函数
     *
     * @param filePath 文件所在的位置，可为相对位置，也可为绝对位置
     * @return 文件哈希值的字符串形式
     */
    public static String fileHashValue(String filePath) {
        try (InputStream is = new BufferedInputStream(new FileInputStream(filePath))) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024 * 8];
            int len;
            while ((len = is.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            byte[] value = md.digest();
            return bytesToHexString(value);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String stringHashValue(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
