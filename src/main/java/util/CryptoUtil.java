package util;

import it.unisa.dia.gas.jpbc.Element;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.*;
import java.util.Random;

public class CryptoUtil {
    /**
     * 获取哈希值
     *
     * @param mode    哈希模式
     * @param element 要哈希的值
     * @return 哈希过后的值
     */
    public static byte[] getHash(String mode, Element element) {
        byte[] hash_value = null;

        try {
            MessageDigest md = MessageDigest.getInstance(mode);
            hash_value = md.digest(element.toBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash_value;
    }

    /**
     * 获取哈希值
     *
     * @param mode  哈希模式
     * @param bytes 要哈希的值
     * @return 哈希过后的值
     */
    public static byte[] getHash(String mode, byte[] bytes) {
        byte[] hash_value = null;

        try {
            MessageDigest md = MessageDigest.getInstance(mode);
            hash_value = md.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash_value;
    }

    /**
     * ElGamal加密函数
     *
     * @param key  加密密钥
     * @param data 要加密的数据
     * @return 密文
     */
    public static Element[] ElGamalEncrypt(Element p, Element key, Element data) {
        int k = new Random().nextInt();
        Element[] secret = new Element[2];

        secret[0] = p.duplicate().pow(BigInteger.valueOf(k));
        secret[1] = data.duplicate().add(key.duplicate().pow(BigInteger.valueOf(k)));

        return secret;

    }

    /**
     * ElGamal解密函数
     *
     * @param key  密钥
     * @param data 密文
     * @return 明文
     */
    public static Element ElGamalDecrypt(Element key, Element[] data) {
        return data[1].sub(data[0].mulZn(key));
    }

    /**
     * AES加密函数
     *
     * @param key  加密密钥,128或256位
     * @param data 要加密的数据
     * @return 密文
     */
    public static byte[] AESEncrypt(byte[] key, byte[] data) {
        String key_algorithm = "AES";
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
            Key key1 = initKeyForAES(new String(key));
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key1.getEncoded(), key_algorithm));
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeyException |
                NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES解密函数
     *
     * @param key  密钥
     * @param data 密文
     * @return 明文
     */
    public static byte[] AESDecrypt(byte[] key, byte[] data) {
        String key_algorithm = "AES";
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
            Key key1 = initKeyForAES(new String(key));
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key1.getEncoded(), key_algorithm));
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Key initKeyForAES(String key) throws NoSuchAlgorithmException {
        if (null == key || key.length() == 0) {
            throw new NullPointerException("key not is null");
        }
        SecretKeySpec key2;
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(key.getBytes());
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            key2 = new SecretKeySpec(enCodeFormat, "AES");
        } catch (NoSuchAlgorithmException ex) {
            throw new NoSuchAlgorithmException();
        }
        return key2;
    }
}
