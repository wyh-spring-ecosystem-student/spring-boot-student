package com.xiaolyuh.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.file.NoSuchFileException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 加解密工具
 *
 * @author yuhao.wang3
 */
public class RSAUtil {
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    private static final String CHARSET = "UTF-8";

    private static Logger logger = LoggerFactory.getLogger(RSAUtil.class);

    /**
     * 加密
     *
     * @param data      数据
     * @param publicKey 公钥
     * @return 密文
     * @throws UnsupportedEncodingException
     */
    public static String encryptByPublicKey(String data, String publicKey) throws UnsupportedEncodingException {
        if (data == null) {
            return null;
        }
        return Base64.encodeBase64String(encryptByPublicKey(data.getBytes(CHARSET), publicKey));
    }

    /**
     * 加密
     *
     * @param data      数据
     * @param publicKey 公钥
     * @return 密文
     * @throws UnsupportedEncodingException
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(1, publicK);
            return getDoFinalData(data, cipher, MAX_ENCRYPT_BLOCK);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("加密算法参数错误！");
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("秘钥无效，请检查秘钥是否正确！");
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("秘钥与期望不一致，请检查秘钥是否正确！");
        } catch (BadPaddingException | NoSuchPaddingException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalArgumentException(e.getCause());
        } catch (IllegalBlockSizeException e) {
            throw new IllegalArgumentException("加密块size有问题！");
        } catch (IOException e) {
            throw new IllegalArgumentException("数据IO问题！");
        }
    }

    /**
     * 解密
     *
     * @param encryptedData 密文
     * @param privateKey    私钥
     * @return 明文
     * @throws UnsupportedEncodingException
     */
    public static String decryptByPrivateKey(String encryptedData, String privateKey) throws UnsupportedEncodingException {
        return new String(decryptByPrivateKey(Base64.decodeBase64(encryptedData), privateKey), CHARSET);
    }

    /**
     * 解密
     *
     * @param encryptedData 密文
     * @param privateKey    私钥
     * @return 明文
     * @throws UnsupportedEncodingException
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        try {
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(2, privateK);
            return getDoFinalData(encryptedData, cipher, MAX_DECRYPT_BLOCK);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("解密算法参数错误！");
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("秘钥无效，请检查秘钥是否正确！");
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("秘钥与期望不一致，请检查秘钥是否正确！");
        } catch (BadPaddingException | NoSuchPaddingException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalArgumentException(e.getCause());
        } catch (IllegalBlockSizeException e) {
            throw new IllegalArgumentException("解密块size有问题！");
        } catch (IOException e) {
            throw new IllegalArgumentException("数据IO问题！");
        }
    }

    /**
     * 签名
     *
     * @param data       签名数据
     * @param privateKey 私钥
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String sign(String data, String privateKey) throws UnsupportedEncodingException {
        if (data == null) {
            return null;
        }
        return Base64.encodeBase64String(sign(data.getBytes(CHARSET), privateKey));
    }

    /**
     * 签名
     *
     * @param data       数据
     * @param privateKey 私钥
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] sign(byte[] data, String privateKey) {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateK);
            signature.update(data);
            return signature.sign();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("加密算法参数错误！");
        } catch (SignatureException e) {
            throw new IllegalArgumentException("签名异常，请检查秘钥是否正确！");
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("秘钥无效，请检查秘钥是否正确！");
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("秘钥与期望不一致，请检查秘钥是否正确！");
        }
    }

    /**
     * 验证签名
     *
     * @param data      数据
     * @param publicKey 公钥
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean verify(String data, String signatureData, String publicKey) throws UnsupportedEncodingException {
        if (data == null || signatureData == null) {
            return false;
        }
        return verify(data.getBytes(CHARSET), Base64.decodeBase64(signatureData), publicKey);
    }

    /**
     * 验证签名
     *
     * @param data      数据
     * @param publicKey 公钥
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean verify(byte[] data, byte[] signatureData, String publicKey) {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicK = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicK);
            signature.update(data);
            return signature.verify(signatureData);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("加密算法参数错误！");
        } catch (SignatureException e) {
            throw new IllegalArgumentException("签名异常，请检查秘钥是否正确！");
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("秘钥无效，请检查秘钥是否正确！");
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("秘钥与期望不一致，请检查秘钥是否正确！");
        }
    }

    /**
     * the traditional io way
     *
     * @param file File
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(File file) throws IOException {

        return toByteArray(new FileInputStream(file));
    }

    /**
     * the traditional io way
     *
     * @param inputStream FileInputStream
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream inputStream) throws IOException {

        try (BufferedInputStream in = new BufferedInputStream(inputStream);
             ByteArrayOutputStream bos = new ByteArrayOutputStream(inputStream.available())) {

            int buf_size = in.available();
            byte[] buffer = new byte[buf_size];
            int len;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        }
    }

    private static byte[] getDoFinalData(byte[] data, Cipher cipher, int maxBlock) throws BadPaddingException, IllegalBlockSizeException, IOException {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        for (int i = 0; inputLen - offSet > 0; offSet = i * maxBlock) {
            byte[] cache;
            if (inputLen - offSet > maxBlock) {
                cache = cipher.doFinal(data, offSet, maxBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            ++i;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    public static void main(String args[]) throws UnsupportedEncodingException, NoSuchFieldException, NoSuchFileException {
        String testPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCE7vuntatVmQVp6xGlBa/U/cEkKtFjyhsTtn1inlYtw5KSasTfa/HMPwJKp1QchsGEt0usOkHHC9HuD8o1gKx/Dgjo6b/XGu6xhinyRjCJWLSHXGOq9VLryaThwZsRB4Bb5DU9NXkl8WE2ih8QEKO1143KeJ5SE38awi74im0dzQIDAQAB";
        String testPrivateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAITu+6e1q1WZBWnrEaUFr9T9wSQq0WPKGxO2fWKeVi3DkpJqxN9r8cw/AkqnVByGwYS3S6w6QccL0e4PyjWArH8OCOjpv9ca7rGGKfJGMIlYtIdcY6r1UuvJpOHBmxEHgFvkNT01eSXxYTaKHxAQo7XXjcp4nlITfxrCLviKbR3NAgMBAAECgYAHUgXHuYhi4Vdb+tbw6HxDVWoCbN01CpctIbqL6L5ELOXwbDLFPvOE1N9ybv6Bx6X2ggWHyXl/1ZXM70+qXJijHZNcDWPocbJDrOOHwePyNreSU0UGNRcBJ7RVnH2YDWut9GZFv88lUp6/xNUJ8QG7a4kOcLsb5L1YrkV+aEeiXQJBAO0rkR0a/ZHUcKgIzF+OaGnwMqiZHIPIZe7xkTTaEk5Qw6HWNOWR6jbwF9bomVBSAM78IFRrD8yUAR6CsAbhUWsCQQCPfNQX1ikUlDutvPGXxqbzUmiBDH2x7xbvPIySfCaWTjYm+YQDzHtfW5lx96HyWcNp/4ryFP8vNZ8p8DCN5kOnAkBqxIUkRCVIxAkfLC7NCa/pmQ9FJQBYNxvkUG1dDJrXFLatIWBYxLJanwUsYzO5m+DvTUNEnZnUMAC8+npB7qcXAkAmfoiv9GaE/Ned3qi53TOA58TdiipWiBwRBp931RLNFCJ3Bk2ib0NR69MYviSWTfqc/0+ZboSfd7VBnQyJpRLVAkB1sskxSIKMt3vGws0vyfY1B2GwosRtRRl+h9uQxcvM6is39OpKFwAmurL9n0SjtvSdIT/XltoP69hcgz83hem0";

        String data = "hello，中国";
        String enData = RSAUtil.encryptByPublicKey(data, testPublicKey);
        String sign = RSAUtil.sign(data, testPrivateKey);

        boolean verify = RSAUtil.verify(data, sign, testPublicKey);
        String deData = RSAUtil.decryptByPrivateKey(enData, testPrivateKey);

        System.out.println(String.format("data:%s", data));

        System.out.println(String.format("enData:%s", enData));
        System.out.println(String.format("sign:%s", sign));

        System.out.println(String.format("verify:%s", verify));
        System.out.println(String.format("deData:%s", deData));

    }


}

