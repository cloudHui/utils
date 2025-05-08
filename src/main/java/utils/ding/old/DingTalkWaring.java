//package utils.ding.old;
//
//import com.alibaba.fastjson.JSON;
//import http.client.HttpClientPool;
//import org.apache.commons.codec.binary.Base64;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import utils.other.JsonUtils;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class DingTalkWaring {
//    private static final Logger LOGGER = LoggerFactory.getLogger(DingTalkWaring.class);
//
//    private final String accessToken;
//
//    private final String secret;
//
//    private HttpClientPool clientPool;
//
//    public DingTalkWaring() {
//        this.accessToken = "c025bd04abf39f4b19aca4ac4d277b073619d231cde1875f458e3bb10badf80b";
//        this.secret = "SEC74fa5d21ccd682f91446220528864a4bf00d580e82e15ee38eec3be18b6374b5";
//        clientPool = new HttpClientPool();
//        clientPool.init(10);
//    }
//
//    public DingTalkWaring(String accessToken, String secret) {
//        this.accessToken = accessToken;
//        this.secret = secret;
//    }
//
//    public static void main(String[] args) {
//        DingTalkWaring dingTalkWaring = new DingTalkWaring();
//        dingTalkWaring.sendMsg("测试@刘云辉");
//    }
//
//    /**
//     * 发送钉钉消息
//     *
//     * @param message 要发送的报警消息
//     */
//    public void sendMsg(String message) {
//        sendMsg(message, "");
//    }
//
//    /**
//     * 发送钉钉消息
//     *
//     * @param message 要发送的报警消息
//     * @param phones  要at的电话号码 逗号隔开
//     */
//    public void sendMsg(String message, String phones) {
//        long timestamp = System.currentTimeMillis();
//        OapiRobotSendRequest request = new OapiRobotSendRequest();
//        request.setMsgtype("text");
//        Text text = new Text();
//        text.setContent(message);
//        request.setText(JsonUtils.writeValue(text));
//        At at = new At();
//        if (phones != null && phones.length() > 0) {
//            String[] phoneArray = phones.split(",");
//            List<String> atMobiles = new ArrayList<>(Arrays.asList(phoneArray));
//            at.setAtMobiles(atMobiles);
//            at.setIsAtAll("false");
//            request.setAt(JsonUtils.writeValue(at));
//        }
//        String url = "";
//        String string = "";
//        try {
//            String URL = "https://oapi.dingtalk.com/robot/send?access_token=";
//            url = URL + accessToken + "&timestamp=" + timestamp + "&sign=" + getSign(timestamp, secret);
//            string = JSON.toJSONString(request);
//            String response = clientPool.sendPost(url, string);
//            LOGGER.info("[sendMsg message:{} url:{} param:{} res:{}]", message, url, string, response);
//            LOGGER.info("[param:{} ", string);
//            LOGGER.info("[res:{}]", response);
//        } catch (Exception e) {
//            LOGGER.error("{} {} ", url, string, e);
//        }
//        LOGGER.info("cost:{}ms", System.currentTimeMillis() - timestamp);
//    }
//
//    private static String getSign(long timestamp, String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
//        String stringToSign = timestamp + "\n" + secret;
//        Mac mac = Mac.getInstance("HmacSHA256");
//        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
//        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
//        return URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
//    }
//}
