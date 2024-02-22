package utils.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;

public class WarningUtil {

	private final DingTalkClient client;

	public WarningUtil() {
		String url = "https://oapi.dingtalk.com/robot/send?access_token=cb2da625856eea700198d67a3ccdfbb1e4b9f4ef3cecaf3ada338b9aa348aa00";
		client = new DefaultDingTalkClient(url);
	}


	public void sendDingTalkMessage(String message) {
		new Thread(() -> asyncSendMessage(message)).start();
	}

	private void asyncSendMessage(String message) {
		OapiRobotSendRequest request = new OapiRobotSendRequest();
		request.setMsgtype("text");
		OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
		text.setContent(message);
		request.setText(text);
		OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
		List<String> atMobiles = new ArrayList<>();
		atMobiles.add("17671292550");
		atMobiles.add("13717949145");
		at.setAtMobiles(atMobiles);
		at.setIsAtAll("false");
		request.setAt(at);
		try {
			OapiRobotSendResponse response = client.execute(request);
			System.out.println("send DingTalk Robot isSuccess : " + response.isSuccess() + " ErrorCode:{} " + response.getErrcode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int warning = 10;//10分钟没数据报警
		WarningUtil util = new WarningUtil();
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.schedule(() -> {
			long now = System.currentTimeMillis();
			System.out.println("check new file start  now: " + now);
			boolean needNotice = FileChecker.hasNewFiles("url");
			if (needNotice) {
				long diff = now - FileChecker.lastModifiedTime;
				int minute = (int) (diff / (1000 * 60));
				if (minute > warning) {
					util.sendDingTalkMessage("报警: 长时间没有文件写入");
				}
			}
			long end = System.currentTimeMillis();
			System.out.println("check new file end now: " + end);
		}, 30, TimeUnit.MINUTES);

	}
}
