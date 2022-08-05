package db.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class JedisManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(JedisManager.class);
	private static final String FILE_NAME = "redis.properties";
	private JedisPool jedisPool;

	private JedisManager() {
		this(FILE_NAME);
	}

	private JedisManager(String fileName) {
		this.jedisPool = createPool(fileName);
	}

	public Jedis getJedis() {
		return null == this.jedisPool ? null : this.jedisPool.getResource();
	}

	public static JedisManager create() {
		return create("redis.properties");
	}

	public static JedisManager create(String fileName) {
		return new JedisManager(fileName);
	}

	private static JedisPool createPool(String fileName) {
		InputStream inputStream = JedisManager.class.getClassLoader().getResourceAsStream(fileName);
		Properties properties = new Properties();

		try {
			properties.load(inputStream);
			String url = properties.getProperty("redis.url");
			int minIdle = getInt(properties.getProperty("redis.minIdle"), 0);
			int maxTotal = getInt(properties.getProperty("redis.maxTotal"), 8);
			int timeout = getInt(properties.getProperty("redis.timeout"), 3000);

			URI uri;
			try {
				uri = new URI(url);
			} catch (URISyntaxException var9) {
				throw new RuntimeException(var9);
			}

			JedisPoolConfig jedisPoolConfig = createJedisPoolConfig(minIdle, maxTotal);
			return new JedisPool(jedisPoolConfig, uri, timeout);
		} catch (Exception var10) {
			LOGGER.error("Error! occurred when initializing JedisManager", var10);
			return null;
		}
	}

	private static int getInt(String val, int defaultVal) {
		return null != val && !val.isEmpty() ? Integer.parseInt(val) : defaultVal;
	}

	private static JedisPoolConfig createJedisPoolConfig(int minIdle, int maxTotal) {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setMaxTotal(maxTotal);
		return jedisPoolConfig;
	}
}
