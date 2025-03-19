package utils.other;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SignUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(SignUtils.class);

    private static final String ALGORITHM = "AES/GCM/PKCS5Padding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;

    private static SecureRandom secureRandom = new SecureRandom();

    /**
     * AES 加密—— 实名认证系统规则
     * @param content
     * @param key
     * @return
     */
    public static String  encryptAu(String content, String key) {
        try {
            byte[] hexStr = HexUtils.decodeHex(key.toCharArray());
            //加密算法：AES/GCM/PKCS5Padding
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec skeySpec = new SecretKeySpec(hexStr, "AES");

            //随机生成iv 12位
            byte[] iv = new byte[IV_LENGTH_BYTE];
            secureRandom.nextBytes(iv);

            //数据加密， AES-GCM-128
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
            byte[] encrypted = cipher.doFinal(content.getBytes());          //数据加密

            //iv+加密数据 拼接  iv在前，加密数据在后
            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encrypted.length);
            byteBuffer.put(iv);
            byteBuffer.put(encrypted);
            byte[] cipherMessage = byteBuffer.array();

            //转换为Base64 Base64算法有多种变体， 这里使用的是java.util.Base64
            return Base64.getEncoder().encodeToString(cipherMessage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String content, String key) {
        try {
            byte[] hexStr = HexUtils.decodeHex(key);
            //加密算法：AES/GCM/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
            SecretKeySpec skewSpec = new SecretKeySpec(hexStr, "AES");

            //随机生成iv 12位
            byte[] iv = new byte[12];
            RandomUtils.randomByte(iv);

            //数据加密， AES-GCM-128
            cipher.init(Cipher.ENCRYPT_MODE, skewSpec, new GCMParameterSpec(128, iv));
            byte[] encrypted = cipher.doFinal(content.getBytes());          //数据加密

            //iv+加密数据 拼接  iv在前，加密数据在后
            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encrypted.length);
            byteBuffer.put(iv);
            byteBuffer.put(encrypted);
            byte[] cipherMessage = byteBuffer.array();

            //转换为Base64 Base64算法有多种变体， 这里使用的是java.util.Base64
            return Base64.getEncoder().encodeToString(cipherMessage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> param(String appID, String bizID, String time) {
        Map<String, String> params = new HashMap<>();
        params.put("Content-Type", "application/json; charset=utf-8");
        params.put("appId", appID);
        params.put("bizId", bizID);
        params.put("timestamps", time);
        return params;
    }

    public static String sign(String secretKey, Map<String, String> params, String encryptData) {

        String signStr = parseMapString(params);
        signStr = secretKey + signStr + encryptData;

        String sign = EncryptUtils.sha256(signStr);

        LOGGER.info("signStr:{} sign:{}", signStr, sign);
        return sign;
    }

    private static String parseMapString(Map<String, String> params) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            list.add(entry.getKey());
        }
        StringBuilder sb = new StringBuilder();
        Collections.sort(list);
        for (String key : list) {
            if ("sign".equals(key) || "Content-Type".equals(key)) {
                continue;
            }
            sb.append(key).append(params.get(key));
        }
        return sb.toString();
    }

    /**
     * 获取属性名数组
     */
    public static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 根据属性名获取属性值
     */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter);
            return method.invoke(o);
        } catch (Exception e) {
            return null;
        }
    }
}
