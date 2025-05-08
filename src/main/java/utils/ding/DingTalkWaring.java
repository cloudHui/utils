package utils.ding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.TaobaoClient;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DingTalkWaring {
	private static final Logger LOGGER = LoggerFactory.getLogger(DingTalkWaring.class);

	private final String accessToken;

	private final String secret;

	public DingTalkWaring() {
		this.accessToken = "c025bd04abf39f4b19aca4ac4d277b073619d231cde1875f458e3bb10badf80b";
		this.secret = "SEC74fa5d21ccd682f91446220528864a4bf00d580e82e15ee38eec3be18b6374b5";
	}

	public DingTalkWaring(String accessToken, String secret) {
		this.accessToken = accessToken;
		this.secret = secret;
	}

	public static void main(String[] args) {
		DingTalkWaring dingTalkWaring = new DingTalkWaring();
		dingTalkWaring.sendMsg("钉钉自带api","17671292550");
	}

	/**
	 * 发送钉钉消息
	 *
	 * @param message 要发送的报警消息
	 * @param phones  要at的电话号码 逗号隔开
	 */
	public void sendMsg(String message, String phones) {
		long timestamp = System.currentTimeMillis();
		OapiRobotSendRequest request = new OapiRobotSendRequest();
		request.setMsgtype("text");
		OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
		text.setContent(message);
		request.setText(text);
		OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
		if (phones != null && phones.length() > 0) {
			String[] phoneArray = phones.split(",");
			List<String> atMobiles = new ArrayList<>(Arrays.asList(phoneArray));
			at.setAtMobiles(atMobiles);
			at.setIsAtAll("false");
			request.setAt(at);
		}
		try {
			String URL = "https://oapi.dingtalk.com/robot/send?access_token=";
			String url = URL + accessToken + "&timestamp=" + timestamp + "&sign=" + getSign(timestamp, secret);
			TaobaoClient client = new DefaultDingTalkClient(url);
			OapiRobotSendResponse response = client.execute(request);
			LOGGER.info("[sendMsg message:{} url:{} param:{} res:{}]", message, url, JSON.toJSONString(request), JSON.toJSONString(response));
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOGGER.info("cost:{}ms", System.currentTimeMillis() - timestamp);
	}

	private static String getSign(long timestamp, String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
		String stringToSign = timestamp + "\n" + secret;
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
		byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
		return URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
	}
}
