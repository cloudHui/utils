package utils.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecCommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecCommand.class);

	/**
	 * 执行命令
	 *
	 * @param command 命令
	 * @return 命令结果
	 */
	public static List<String> exeCommand(String command) {
		long startTime = System.currentTimeMillis();
		String cmd;
		String osName = System.getProperty("os.name");
		if (osName.contains("Windows")) {
			cmd = "cmd.exe /c " + command;
		} else {
			cmd = command;
		}
		InputStream in;
		String result;
		List<String> results = new ArrayList<>();
		try {
			Process pro = Runtime.getRuntime().exec(cmd);
			in = pro.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(in));
			if ((result = read.readLine()) != null) {
				do {
					results.add(result);
				} while (read.readLine() != null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info("exeCommand:{} cost:{}ms", command, System.currentTimeMillis() - startTime);
		return results;
	}

	/**
	 * 执行命令干活去指定结果
	 */
	public static String exeCommand(String command, String targetResult) {
		List<String> exeCommand = exeCommand(command);
		return getExeCommandResult(exeCommand, targetResult);
	}

	/**
	 * 获取需要的命令结果
	 */
	public static String getExeCommandResult(List<String> results, String targetResult) {
		if (results == null || results.isEmpty()) {
			return "";
		}
		for (String temp : results) {
			if (temp.contains(targetResult)) {
				return temp.substring(temp.indexOf(targetResult)).trim();
			}
		}
		return "";
	}

	/**
	 * 执行脚本
	 */
	public static void exeBatSh(String batName) {
		String cmd;
		String osName = System.getProperty("os.name");
		if (osName.contains("Windows")) {
			cmd = "cmd.exe /c " + batName;
		} else {
			cmd = "sh " + batName;
		}
		try {
			Runtime.getRuntime().exec(cmd).waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 是否是windows系统
	 */
	public static boolean isWindows() {
		String osName = System.getProperty("os.name");
		return osName.contains("Windows");
	}


	public static void main(String[] args) throws InterruptedException {
		new Thread(() -> {
			// 通过cmd程序执行cmd命令
			try {
				boolean isWindows = isWindows();
				if (isWindows) {
					String strCmd = "ipconfig";
					Runtime.getRuntime().exec("cmd /c  " + strCmd).waitFor();
				} else {
					String strCmd = "./UpdateSVN.sh";
					Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", strCmd}).waitFor();
				}
				Thread.sleep(2000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}).start();
		//System.out.println(new Timestamp(new Date().getTime()));
		Thread.sleep(5000);
		System.out.println(new Timestamp(new Date().getTime()));
	}
}
