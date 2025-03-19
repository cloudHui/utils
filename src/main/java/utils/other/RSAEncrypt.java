
package utils.other;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Objects;

public class RSAEncrypt {
	private static final String RSA = "RSA";
	private static final int MAXENCRYPTSIZE = 117;

	public RSAEncrypt() {
	}

	public static KeyPair makeKeyPair() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		return keyPairGenerator.generateKeyPair();
	}

	public static String getPublicKey(KeyPair keyPair) {
		PublicKey publicKey = keyPair.getPublic();
		byte[] keyBytes = publicKey.getEncoded();
		return Base64Utils.encoder(keyBytes);
	}

	public static String getPrivateKey(KeyPair keyPair) {
		PrivateKey privateKey = keyPair.getPrivate();
		byte[] keyBytes = privateKey.getEncoded();
		return Base64Utils.encoder(keyBytes);
	}

	public static PublicKey stringToPublicKey(String modulus, String exponent) throws Exception {
		try {
			BigInteger mod = new BigInteger(1, Objects.requireNonNull(Base64Utils.decoder(modulus)));
			BigInteger exp = new BigInteger(1, Objects.requireNonNull(Base64Utils.decoder(exponent)));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(mod, exp);
			return keyFactory.generatePublic(keySpec);
		} catch (Exception var7) {
			var7.printStackTrace();
			return null;
		}
	}

	public static PrivateKey stringToPrivateKey(String privateKey) throws Exception {
		byte[] keyBytes = Base64Utils.decoder(privateKey);
		assert keyBytes != null;
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}

	public static String encrypt(String content, String modulus, String exponent) throws Exception {
		return encrypt(content.getBytes(StandardCharsets.UTF_8), stringToPublicKey(modulus, exponent));
	}

	public static String encrypt(byte[] source, PublicKey publicKey) throws Exception {
		String encryptData = "";

		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(1, publicKey);
			int length = source.length;
			int offset = 0;
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();

			for (int i = 0; length - offset > 0; offset = i * 117) {
				byte[] cache;
				if (length - offset > 117) {
					cache = cipher.doFinal(source, offset, 117);
				} else {
					cache = cipher.doFinal(source, offset, length - offset);
				}

				outStream.write(cache, 0, cache.length);
				++i;
			}

			return Base64Utils.encoder(outStream.toByteArray());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException var9) {
			var9.printStackTrace();
		}

		return encryptData;
	}

	public static byte[] decrypt(String content, String privateKey) throws Exception {
		return decrypt(content.getBytes(), stringToPrivateKey(privateKey));
	}

	public static byte[] decrypt(byte[] content, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(2, privateKey);
		return cipher.doFinal(content);
	}
}
