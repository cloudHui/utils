package utils.other;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	private static final Logger LOGGER = LoggerFactory.getLogger(MD5Utils.class);

	public MD5Utils() {
	}

	public static String MD5(String data) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(data.getBytes());
			byte[] d = messageDigest.digest();
			StringBuilder sb = new StringBuilder();
			int i = 0;
			int val = 0;

			for (int size = d.length; i < size; ++i) {
				val = d[i];
				if (val < 0) {
					val += 256;
				}

				if (val < 16) {
					sb.append("0");
				}

				sb.append(Integer.toHexString(val));
			}

			return sb.toString();
		} catch (NoSuchAlgorithmException var7) {
			LOGGER.error("{}", data, var7);
			return null;
		}
	}
}
