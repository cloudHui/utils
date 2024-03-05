package monitor;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import threadtutil.thread.ExecutorPool;
import threadtutil.timer.Timer;
import utils.utils.DingTalkWaring;
import utils.utils.ExecCommand;

public class ServerMonitor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerMonitor.class);

	private static String TOKEN = "1b0465fd84a25434fd3915486a5ad795fbb504f466513aa2c4851e4baaeea5a5";

	private static String SECRET = "SECfc5c4b17ed35d6bf123d0ca439a9f91ac2c67c42d94f719aa003b1278189ca15";

	/**
	 * 检测服务器负载
	 */
	public static void checkCpuLoad() {
		checkCpuLoad(0, "", null, null);
	}

	/**
	 * 检测服务器负载
	 */
	public static void checkCpuLoad(String phone) {
		checkCpuLoad(0, phone, null, null);
	}

	/**
	 * 检测服务器负载
	 *
	 * @param warning 间隔的延迟检测时间 毫秒
	 * @param phone   要@的手机号
	 * @param token   机器人token
	 * @param secret  机器人密钥
	 */
	public static void checkCpuLoad(int warning, String phone, String token, String secret) {
		if (token != null) {
			TOKEN = token;
		}
		if (secret != null) {
			SECRET = secret;
		}
		//默认的最小间隔时间ms
		int DEFAULT_DELAY = 60 * 1000;
		if (warning < DEFAULT_DELAY) {
			warning = DEFAULT_DELAY;
		}
		int coreNUm = Runtime.getRuntime().availableProcessors();
		ExecutorPool pool = new ExecutorPool("waring", coreNUm);
		Timer timer = new Timer();
		timer.setRunners(pool);
		timer.register(1, warning, -1, MyTest -> {
			try {
				cpuLoadWaring(coreNUm, phone);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}, null);
	}

	/**
	 * cpu负载打印
	 *
	 * @param coreNUm 核心数
	 */
	private static void cpuLoadWaring(int coreNUm, String phone) {
		long start = System.currentTimeMillis();
		List<Double> cpuLoad = getCpuLoad();
		if (cpuLoad.isEmpty()) {
			try {
				throw new RuntimeException("cpuLoadWaring getCpu Info null uptime");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		LOGGER.info("cpuLoad:{}", cpuLoad);
		if (cpuLoad.get(0) > coreNUm * 0.7) {
			DingTalkWaring dingTalkWaring = new DingTalkWaring(TOKEN, SECRET);
			dingTalkWaring.sendMsg(JSON.toJSONString(cpuLoad), phone);
		}
		LOGGER.info("cpuLoadWaring  cost:{}ms", System.currentTimeMillis() - start);
	}

	/**
	 * 获取cpu负载
	 */
	private static List<Double> getCpuLoad() {
		List<Double> cpuLoads = new ArrayList<>();
		List<String> strings = ExecCommand.exeCommand("uptime");
		if (!strings.isEmpty()) {
			String s = strings.get(0);
			String[] s1 = s.split("load");
			if (s1.length > 1) {
				s = s1[1].trim().split(":")[1];
				s1 = s.split(",");
				cpuLoads.add(Double.parseDouble(s1[0]));
				cpuLoads.add(Double.parseDouble(s1[1]));
				cpuLoads.add(Double.parseDouble(s1[2]));
			}
		} else {
			LOGGER.error("exec uptime result null");
		}
		return cpuLoads;
	}
}
