package utils.other;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

public class IpUtil {

	/**
	 * 获取外网ip
	 *
	 * @return 外网ip地址
	 */
	public static String getOutIp() {
		List<String> results = ExecCommand.exeCommand("curl icanhazip.com");
		if (results.isEmpty()) {
			return "";
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
	 * 获取内网ip
	 */
	public static String getLocal() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue;
				}
				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = addresses.nextElement();
					if (address instanceof Inet4Address) {
						return address.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
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

//	public static void main(String[] args) {
//		System.out.println(getLocalIP());
//		System.out.println(getOutIp());
//		System.out.println(getHostName());
//	}
}
