package utils.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ExecCommand {

	/**
	 * 执行命令
	 *
	 * @return 命令结果
	 */
	public static String exeCommand(String command) {
		String cmd;
		List<String> results = new ArrayList<>();
		String osName = System.getProperty("os.name");
		if (osName.contains("Windows")) {
			cmd = "cmd.exe /c " + command;
		} else {
			cmd = command;
		}
		InputStream in;
		String result;
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
		if (!results.isEmpty()) {
			return results.get(0);
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

	public static void main(String[] args) {
		System.out.println(exeCommand("svn info"));
	}
}