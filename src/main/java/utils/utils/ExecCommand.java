package utils.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExecCommand {

	/**
	 * 执行命令
	 *
	 * @param command        命令
	 * @param needResultChar 需要的结果字符串
	 * @return 命令结果
	 */
	public static String exeCommand(String command, String needResultChar) {
		String cmd;
		String osName = System.getProperty("os.name");
		if (osName.contains("Windows")) {
			cmd = "cmd.exe /c " + command;
		} else {
			cmd = command;
		}
		InputStream in;
		String result = "";
		try {
			Process pro = Runtime.getRuntime().exec(cmd);
			in = pro.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(in));
			if ((result = read.readLine()) != null) {
				do {
					if (needResultChar != null && needResultChar.length() > 0 && result.contains(needResultChar)) {
						return result;
					}
				} while (read.readLine() != null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
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
		System.out.println(exeCommand("svn info", ""));
	}
}
