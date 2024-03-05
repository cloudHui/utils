package utils.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import monitor.ServerMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

/**
 * 服务使用情况
 */
public class ServerUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerMonitor.class);

	private static ServerUtils instance = new ServerUtils();

	private SystemInfo systemInfo;
	private OperatingSystem systemOS;
	private int currentProcID = 0;

	public static ServerUtils getInstance() {
		return instance;
	}

	public void init() {
		systemInfo = new SystemInfo();
		systemOS = systemInfo.getOperatingSystem();
	}

	private boolean isVaild() {
		if (systemOS == null) {
			return false;
		}
		if (currentProcID <= 0) {
			currentProcID = getPID();
		}
		return currentProcID > 0;
	}

	private OSProcess getProcessInfo() {
		if (!isVaild()) {
			return null;
		}
		return systemOS.getProcess(currentProcID, false);
	}

	private int getPID() {
		int PID;
		try {
			String name = ManagementFactory.getRuntimeMXBean().getName();
			PID = Integer.parseInt(name.substring(0, name.indexOf('@')));
		} catch (Throwable e) {
			PID = 0;
		}
		return PID;
	}

	private long lastUserTime = 0;
	private long lastKernelTime = 0;
	private long lastUpTime = 0;

	private void recordMonitor() {
		OSProcess processInfo = getProcessInfo();
		if (processInfo == null) {
			return;
		}
		if (lastUserTime == 0) {
			lastKernelTime = processInfo.getKernelTime();
			lastUserTime = processInfo.getUserTime();
			lastUpTime = processInfo.getUpTime();
		} else {
			long curKernelTime = processInfo.getKernelTime();
			long curUserTime = processInfo.getUserTime();
			long curUpTime = processInfo.getUpTime();
			long sensorKernelTime = curKernelTime - lastKernelTime;
			long sensorUserTime = curUserTime - lastUserTime;
			long sensorUpTime = curUpTime - lastUpTime;
			lastKernelTime = curKernelTime;
			lastUserTime = curUserTime;
			lastUpTime = curUpTime;
			if (sensorUpTime > 0 && (sensorKernelTime + sensorUserTime) > 0) {
				long cpuUsed = Math.round(100d * (sensorKernelTime + sensorUserTime) / sensorUpTime);
				if (cpuUsed > 100) {
					cpuUsed = 100;
				}
			}
		}
		long memSize = getMemSize(processInfo);
	}

	public long getMemSize() {
		OSProcess processInfo = getProcessInfo();
		if (processInfo == null) {
			return 0;
		}
		return getMemSize(processInfo);
	}

	private long getMemSize(OSProcess processInfo) {
		return Math.round(processInfo.getResidentSetSize() / 1024d / 1024d);
	}

	private boolean init = false;
	private boolean isWindows = false;

	private boolean isWindows() {
		if (init)
			return isWindows;
		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) {
			isWindows = true;
			init = true;
			return true;
		}
		init = true;
		return false;
	}

	private long strValue2long(String strValue) {
		if (strValue == null || strValue.isEmpty()) {
			return 0;
		}
		float fValue = Float.parseFloat(strValue) / 1024f;
		return Math.round(fValue);
	}

	private void recordGCMonitor() {
		try {
			String strCmd = "jstat -gc " + currentProcID;
			boolean isWindows = isWindows();
			Process process;
			if (!isWindows) {
				process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", strCmd});
			} else {
				process = Runtime.getRuntime().exec("cmd /c " + strCmd);
			}
			// 读取屏幕输出
			BufferedReader strCon = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String strLine;
			List<String> listName = new ArrayList<>();
			List<String> listValue = new ArrayList<>();
			while ((strLine = strCon.readLine()) != null) {
				String[] strArray = strLine.split(" ");
				if (strLine.contains("OC")) {
					for (String s : strArray) {
						if (!s.isEmpty()) {
							listName.add(s);
						}
					}
				} else {
					for (String s : strArray) {
						if (!s.isEmpty()) {
							listValue.add(s);
						}
					}
				}
			}
			if (listName.size() != listValue.size()) {
				return;
			}
			long S0UValue = 0;
			long S1UValue = 0;
			long EUValue = 0;
			long OUValue = 0;
			long MUValue = 0;
			for (int i = 0; i < listName.size(); i++) {
				String strName = listName.get(i);
				String strValue = listValue.get(i);
				switch (strName) {
					case "S0U":
						S0UValue = strValue2long(strValue);
						break;
					case "S1U":
						S1UValue = strValue2long(strValue);
						break;
					case "EU":
						EUValue = strValue2long(strValue);
						break;
					case "OU":
						OUValue = strValue2long(strValue);
						break;
					case "MU":
						MUValue = strValue2long(strValue);
						break;
				}
			}
			long totalUsed = S0UValue + S1UValue + EUValue + OUValue + MUValue;
		} catch (Exception e) {
			LOGGER.info("jstat -gc {} failed exception :{}.", currentProcID, e.getMessage());
		}
	}
}
