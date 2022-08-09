package utils.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class IpUtil {

	/**
	 * 获取外网ip
	 *
	 * @return 外网ip地址
	 */
	public static String getOutIp() {
		String cmd = "curl icanhazip.com";
		List<String> results = new ArrayList<>();
		String osName = System.getProperty("os.name");
		String command;
		if (osName.contains("Windows")) {
			command = "cmd.exe /c " + cmd;
		} else {
			command = cmd;
		}
		InputStream in;
		String result;
		try {
			Process pro = Runtime.getRuntime().exec(command);
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
		return results.get(0);
	}


	/**
	 * 获取内网ip
	 *
	 * @return 内网ip
	 */
	public static String getLocalIP() {
		try {
			StringBuilder sb = new StringBuilder();

			Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				Enumeration addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = (InetAddress) addresses.nextElement();
					if (ip instanceof Inet4Address) {
						if ("127.0.0.1".equals(ip.getHostAddress())) {
							continue;
						}
						sb.append(ip.getHostAddress());
						break;
					}
				}
				if (sb.length() > 0) {
					break;
				}
			}
			return sb.toString();
		} catch (Exception ignored) {
		}
		return null;
	}

	/**
	 * 获取计算机名称
	 *
	 * @return 计算机名称
	 */
	public static String getHostName() {
		InetAddress adar;
		try {
			adar = InetAddress.getLocalHost();
			//获取本机计算机名称
			return adar.getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(getLocalIP());
		System.out.println(getOutIp());
		System.out.println(getHostName());
	}
}
