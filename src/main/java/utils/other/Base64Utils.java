package utils.other;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import utils.other.test.BattleEventEnum;
import utils.other.test.BattleEventListener;


public class Base64Utils {
	private static final Logger LOGGER = LoggerFactory.getLogger(Base64Utils.class);

	public Base64Utils() {
	}

	public static String encoder(byte[] data) {
		return (new BASE64Encoder()).encode(data);
	}

	@BattleEventListener(BattleEventEnum.ON_BE_HURT)
	public static byte[] decoder(String data) {
		try {
			return (new BASE64Decoder()).decodeBuffer(data);
		} catch (Exception var2) {
			LOGGER.error("failed for decoder({})", data, var2);
			return null;
		}
	}
}
