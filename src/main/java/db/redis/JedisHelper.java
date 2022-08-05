package db.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class JedisHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(JedisHelper.class);
	public static final JedisManager jedisManager = JedisManager.create();

	public JedisHelper() {
	}

	public static Boolean exists(String key) {
		return exists(jedisManager, key);
	}

	public static Boolean exists(JedisManager jedisManager, String key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		Boolean var4;
		try {
			var4 = jedis.exists(key);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static Long expire(String key, int seconds) {
		return expire(jedisManager, key, seconds);
	}

	public static Long expire(JedisManager jedisManager, String key, int seconds) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		Long var5;
		try {
			var5 = jedis.expire(key, seconds);
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static Long ttl(String key) {
		return ttl(jedisManager, key);
	}

	public static Long ttl(JedisManager jedisManager, String key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		Long var4;
		try {
			var4 = jedis.ttl(key);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static Long ttl(byte[] key) {
		return ttl(jedisManager, key);
	}

	public static Long ttl(JedisManager jedisManager, byte[] key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		Long var4;
		try {
			var4 = jedis.ttl(key);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static Long persist(String key) {
		return persist(jedisManager, key);
	}

	public static Long persist(JedisManager jedisManager, String key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		Long var4;
		try {
			var4 = jedis.persist(key);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static Long incr(String key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var2 = null;

		Long var3;
		try {
			var3 = jedis.incr(key);
		} catch (Throwable var12) {
			var2 = var12;
			throw var12;
		} finally {
			if (jedis != null) {
				if (var2 != null) {
					try {
						jedis.close();
					} catch (Throwable var11) {
						var2.addSuppressed(var11);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var3;
	}

	public static Long incr(byte[] key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var2 = null;

		Long var3;
		try {
			var3 = jedis.incr(key);
		} catch (Throwable var12) {
			var2 = var12;
			throw var12;
		} finally {
			if (jedis != null) {
				if (var2 != null) {
					try {
						jedis.close();
					} catch (Throwable var11) {
						var2.addSuppressed(var11);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var3;
	}

	public static Long incrBy(String key, long increment) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		Long var5;
		try {
			var5 = jedis.incrBy(key, increment);
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static Long lpush(byte[] key, byte[] data) {
		return lpush(jedisManager, key, data);
	}

	public static Long lpush(JedisManager jedisManager, byte[] key, byte[] data) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		Long var5;
		try {
			var5 = jedis.lpush(key, new byte[][]{data});
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static Long lpush(String key, String data) {
		return lpush(jedisManager, key, data);
	}

	public static Long lpush(JedisManager jedisManager, String key, String data) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		Long var5;
		try {
			var5 = jedis.lpush(key, new String[]{data});
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static Long rpush(String key, String data) {
		return rpush(jedisManager, key, data);
	}

	public static Long rpush(JedisManager jedisManager, String key, String data) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		Long var5;
		try {
			var5 = jedis.rpush(key, new String[]{data});
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static Long rpush(byte[] key, byte[] data) {
		return rpush(jedisManager, key, data);
	}

	public static Long rpush(JedisManager jedisManager, byte[] key, byte[] data) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		Long var5;
		try {
			var5 = jedis.rpush(key, new byte[][]{data});
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static String lpop(String key) {
		return lpop(jedisManager, key);
	}

	public static String lpop(JedisManager jedisManager, String key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		String var4;
		try {
			var4 = jedis.lpop(key);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static byte[] lpop(byte[] key) {
		return lpop(jedisManager, key);
	}

	public static byte[] lpop(JedisManager jedisManager, byte[] key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		byte[] var4;
		try {
			var4 = jedis.lpop(key);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static String rpop(String key) {
		return rpop(jedisManager, key);
	}

	public static String rpop(JedisManager jedisManager, String key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		String var4;
		try {
			var4 = jedis.rpop(key);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static byte[] rpop(byte[] key) {
		return rpop(jedisManager, key);
	}

	public static byte[] rpop(JedisManager jedisManager, byte[] key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		byte[] var4;
		try {
			var4 = jedis.rpop(key);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static List<String> lrange(String key, long start, long end) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var6 = null;

		List var7;
		try {
			var7 = jedis.lrange(key, start, end);
		} catch (Throwable var16) {
			var6 = var16;
			throw var16;
		} finally {
			if (jedis != null) {
				if (var6 != null) {
					try {
						jedis.close();
					} catch (Throwable var15) {
						var6.addSuppressed(var15);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var7;
	}

	public static List<byte[]> lrange(byte[] key, long start, long end) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var6 = null;

		List var7;
		try {
			var7 = jedis.lrange(key, start, end);
		} catch (Throwable var16) {
			var6 = var16;
			throw var16;
		} finally {
			if (jedis != null) {
				if (var6 != null) {
					try {
						jedis.close();
					} catch (Throwable var15) {
						var6.addSuppressed(var15);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var7;
	}

	public static Long llen(String key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var2 = null;

		Long var3;
		try {
			var3 = jedis.llen(key);
		} catch (Throwable var12) {
			var2 = var12;
			throw var12;
		} finally {
			if (jedis != null) {
				if (var2 != null) {
					try {
						jedis.close();
					} catch (Throwable var11) {
						var2.addSuppressed(var11);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var3;
	}

	public static Long llen(byte[] key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var2 = null;

		Long var3;
		try {
			var3 = jedis.llen(key);
		} catch (Throwable var12) {
			var2 = var12;
			throw var12;
		} finally {
			if (jedis != null) {
				if (var2 != null) {
					try {
						jedis.close();
					} catch (Throwable var11) {
						var2.addSuppressed(var11);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var3;
	}

	public static String set(String key, String value) {
		return set(jedisManager, key, value);
	}

	public static String set(JedisManager jedisManager, String key, String value) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		String var5;
		try {
			var5 = jedis.set(key, value);
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static String set(byte[] key, byte[] value) {
		return set(jedisManager, key, value);
	}

	public static String set(JedisManager jedisManager, byte[] key, byte[] value) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		String var5;
		try {
			var5 = jedis.set(key, value);
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static Long setnx(String key, String value) {
		return setnx(jedisManager, key, value);
	}

	public static Long setnx(JedisManager jedisManager, String key, String value) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		Long var5;
		try {
			var5 = jedis.setnx(key, value);
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static String get(String key) {
		return get(jedisManager, key);
	}

	public static String get(JedisManager jedisManager, String key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		String var4;
		try {
			var4 = jedis.get(key);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static byte[] get(byte[] key) {
		return get(jedisManager, key);
	}

	public static byte[] get(JedisManager jedisManager, byte[] key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		byte[] var4;
		try {
			var4 = jedis.get(key);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static Long del(String key) {
		return del(jedisManager, key);
	}

	public static Long del(JedisManager jedisManager, String key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		Long var4;
		try {
			var4 = jedis.del(key);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static Long hlen(String key) {
		return hlen(jedisManager, key);
	}

	public static Long hlen(JedisManager jedisManager, String key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		Long var4;
		try {
			var4 = jedis.hlen(key);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static boolean hexists(String key, String field) {
		return hexists(jedisManager, key, field);
	}

	public static Set<String> hkeys(JedisManager jedisManager, String key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		Set var4;
		try {
			var4 = jedis.hkeys(key);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static Set<String> hkeys(String key) {
		return hkeys(jedisManager, key);
	}

	public static boolean hexists(JedisManager jedisManager, String key, String field) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		boolean var5;
		try {
			var5 = jedis.hexists(key, field);
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static Long hset(String key, String field, String value) {
		return hset(jedisManager, key, field, value);
	}

	public static Long hset(JedisManager jedisManager, String key, String field, String value) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var5 = null;

		Long var6;
		try {
			var6 = jedis.hset(key, field, value);
		} catch (Throwable var15) {
			var5 = var15;
			throw var15;
		} finally {
			if (jedis != null) {
				if (var5 != null) {
					try {
						jedis.close();
					} catch (Throwable var14) {
						var5.addSuppressed(var14);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var6;
	}

	public static String hmset(String key, Map<String, String> data) {
		return hmset(jedisManager, key, data);
	}

	public static String hmset(JedisManager jedisManager, String key, Map<String, String> data) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		String var5;
		try {
			var5 = jedis.hmset(key, data);
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static Long hset(byte[] key, byte[] field, byte[] value) {
		return hset(jedisManager, key, field, value);
	}

	public static Long hset(JedisManager jedisManager, byte[] key, byte[] field, byte[] value) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var5 = null;

		Long var6;
		try {
			var6 = jedis.hset(key, field, value);
		} catch (Throwable var15) {
			var5 = var15;
			throw var15;
		} finally {
			if (jedis != null) {
				if (var5 != null) {
					try {
						jedis.close();
					} catch (Throwable var14) {
						var5.addSuppressed(var14);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var6;
	}

	public static String hmset(byte[] key, Map<byte[], byte[]> data) {
		return hmset(jedisManager, key, data);
	}

	public static String hmset(JedisManager jedisManager, byte[] key, Map<byte[], byte[]> data) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		String var5;
		try {
			var5 = jedis.hmset(key, data);
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static Long hsetnx(String key, String field, String value) {
		return hsetnx(jedisManager, key, field, value);
	}

	public static Long hsetnx(JedisManager jedisManager, String key, String field, String value) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var5 = null;

		Long var6;
		try {
			var6 = jedis.hsetnx(key, field, value);
		} catch (Throwable var15) {
			var5 = var15;
			throw var15;
		} finally {
			if (jedis != null) {
				if (var5 != null) {
					try {
						jedis.close();
					} catch (Throwable var14) {
						var5.addSuppressed(var14);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var6;
	}

	public static Long hsetnx(byte[] key, byte[] field, byte[] value) {
		return hsetnx(jedisManager, key, field, value);
	}

	public static Long hsetnx(JedisManager jedisManager, byte[] key, byte[] field, byte[] value) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var5 = null;

		Long var6;
		try {
			var6 = jedis.hsetnx(key, field, value);
		} catch (Throwable var15) {
			var5 = var15;
			throw var15;
		} finally {
			if (jedis != null) {
				if (var5 != null) {
					try {
						jedis.close();
					} catch (Throwable var14) {
						var5.addSuppressed(var14);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var6;
	}

	public static String hget(String key, String field) {
		return hget(jedisManager, key, field);
	}

	public static String hget(JedisManager jedisManager, String key, String field) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		String var6;
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("hget {} {}", key, field);
			}

			String result = jedis.hget(key, field);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("hget result:{}", result);
			}

			var6 = result;
		} catch (Throwable var15) {
			var4 = var15;
			throw var15;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var14) {
						var4.addSuppressed(var14);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var6;
	}

	public static byte[] hget(byte[] key, byte[] field) {
		return hget(jedisManager, key, field);
	}

	public static byte[] hget(JedisManager jedisManager, byte[] key, byte[] field) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		byte[] var5;
		try {
			var5 = jedis.hget(key, field);
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static List<String> hmget(String key, List<String> fields) {
		return hmget(jedisManager, key, fields);
	}

	public static List<String> hmget(JedisManager jedisManager, String key, List<String> fields) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("hmget {}", key);
			}

			String[] f = new String[fields.size()];
			int i = 0;

			for (int size = fields.size(); i < size; ++i) {
				f[i] = (String) fields.get(i);
			}

			List var17 = jedis.hmget(key, f);
			return var17;
		} catch (Throwable var15) {
			var4 = var15;
			throw var15;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var14) {
						var4.addSuppressed(var14);
					}
				} else {
					jedis.close();
				}
			}

		}
	}

	public static List<byte[]> hmget(byte[] key, List<byte[]> fields) {
		return hmget(jedisManager, key, fields);
	}

	public static List<byte[]> hmget(JedisManager jedisManager, byte[] key, List<byte[]> fields) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		try {
			byte[][] f = new byte[fields.size()][];
			int i = 0;

			for (int size = fields.size(); i < size; ++i) {
				f[i] = (byte[]) fields.get(i);
			}

			List var17 = jedis.hmget(key, f);
			return var17;
		} catch (Throwable var15) {
			var4 = var15;
			throw var15;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var14) {
						var4.addSuppressed(var14);
					}
				} else {
					jedis.close();
				}
			}

		}
	}

	public static Map<String, String> hgetAll(String key) {
		return hgetAll(jedisManager, key);
	}

	public static Map<String, String> hgetAll(JedisManager jedisManager, String key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		Map var4;
		try {
			var4 = jedis.hgetAll(key);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static Map<byte[], byte[]> hgetAll(byte[] key) {
		return hgetAll(jedisManager, key);
	}

	public static Map<byte[], byte[]> hgetAll(JedisManager jedisManager, byte[] key) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		Map var4;
		try {
			var4 = jedis.hgetAll(key);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static Long hincrBy(String key, String field, long value) {
		return hincrBy(jedisManager, key, field, value);
	}

	public static Long hincrBy(JedisManager jedisManager, String key, String field, long value) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var6 = null;

		Long var7;
		try {
			var7 = jedis.hincrBy(key, field, value);
		} catch (Throwable var16) {
			var6 = var16;
			throw var16;
		} finally {
			if (jedis != null) {
				if (var6 != null) {
					try {
						jedis.close();
					} catch (Throwable var15) {
						var6.addSuppressed(var15);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var7;
	}

	public static Long hdel(String key, String field) {
		return hdel(jedisManager, key, field);
	}

	public static Long hdel(JedisManager jedisManager, String key, String field) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		Long var5;
		try {
			var5 = jedis.hdel(key, new String[]{field});
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static Long zadd(String key, double score, String member) {
		return zadd(jedisManager, key, score, member);
	}

	public static Long zadd(JedisManager jedisManager, String key, double score, String member) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var6 = null;

		Long var7;
		try {
			var7 = jedis.zadd(key, score, member);
		} catch (Throwable var16) {
			var6 = var16;
			throw var16;
		} finally {
			if (jedis != null) {
				if (var6 != null) {
					try {
						jedis.close();
					} catch (Throwable var15) {
						var6.addSuppressed(var15);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var7;
	}

	public static Double zincrby(String key, String member, double increment) {
		return zincrby(jedisManager, key, member, increment);
	}

	public static Double zincrby(JedisManager jedisManager, String key, String member, double increment) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var6 = null;

		Double var7;
		try {
			var7 = jedis.zincrby(key, increment, member);
		} catch (Throwable var16) {
			var6 = var16;
			throw var16;
		} finally {
			if (jedis != null) {
				if (var6 != null) {
					try {
						jedis.close();
					} catch (Throwable var15) {
						var6.addSuppressed(var15);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var7;
	}

	public static Double zscore(String key, String member) {
		return zscore(jedisManager, key, member);
	}

	public static Double zscore(JedisManager jedisManager, String key, String member) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		Double var5;
		try {
			var5 = jedis.zscore(key, member);
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static Long zrem(String key, String memeber) {
		return zrem(jedisManager, key, memeber);
	}

	public static Long zrem(JedisManager jedisManager, String key, String memeber) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		Long var5;
		try {
			var5 = jedis.zrem(key, new String[]{memeber});
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static Set<String> zrange(String key, long start, long end) {
		return zrange(jedisManager, key, start, end);
	}

	public static Set<String> zrange(JedisManager jedisManager, String key, long start, long end) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var7 = null;

		Set var8;
		try {
			var8 = jedis.zrange(key, start, end);
		} catch (Throwable var17) {
			var7 = var17;
			throw var17;
		} finally {
			if (jedis != null) {
				if (var7 != null) {
					try {
						jedis.close();
					} catch (Throwable var16) {
						var7.addSuppressed(var16);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var8;
	}

	public static Set<Tuple> zrangeWithScores(String key, long start, long end) {
		return zrangeWithScores(jedisManager, key, start, end);
	}

	public static Set<Tuple> zrangeWithScores(JedisManager jedisManager, String key, long start, long end) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var7 = null;

		Set var8;
		try {
			var8 = jedis.zrangeWithScores(key, start, end);
		} catch (Throwable var17) {
			var7 = var17;
			throw var17;
		} finally {
			if (jedis != null) {
				if (var7 != null) {
					try {
						jedis.close();
					} catch (Throwable var16) {
						var7.addSuppressed(var16);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var8;
	}

	public static Set<String> zrevrange(String key, long start, long end) {
		return zrevrange(jedisManager, key, start, end);
	}

	public static Set<String> zrevrange(JedisManager jedisManager, String key, long start, long end) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var7 = null;

		Set var8;
		try {
			var8 = jedis.zrevrange(key, start, end);
		} catch (Throwable var17) {
			var7 = var17;
			throw var17;
		} finally {
			if (jedis != null) {
				if (var7 != null) {
					try {
						jedis.close();
					} catch (Throwable var16) {
						var7.addSuppressed(var16);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var8;
	}

	public static Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
		return zrevrangeWithScores(jedisManager, key, start, end);
	}

	public static Set<Tuple> zrevrangeWithScores(JedisManager jedisManager, String key, long start, long end) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var7 = null;

		Set var8;
		try {
			var8 = jedis.zrevrangeWithScores(key, start, end);
		} catch (Throwable var17) {
			var7 = var17;
			throw var17;
		} finally {
			if (jedis != null) {
				if (var7 != null) {
					try {
						jedis.close();
					} catch (Throwable var16) {
						var7.addSuppressed(var16);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var8;
	}

	public static Set<String> zrangeByScore(String key, double min, double max) {
		return zrangeByScore(jedisManager, key, min, max);
	}

	public static Set<String> zrangeByScore(JedisManager jedisManager, String key, double min, double max) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var7 = null;

		Set var8;
		try {
			var8 = jedis.zrangeByScore(key, min, max);
		} catch (Throwable var17) {
			var7 = var17;
			throw var17;
		} finally {
			if (jedis != null) {
				if (var7 != null) {
					try {
						jedis.close();
					} catch (Throwable var16) {
						var7.addSuppressed(var16);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var8;
	}

	public static Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		return zrangeByScoreWithScores(jedisManager, key, min, max);
	}

	public static Set<Tuple> zrangeByScoreWithScores(JedisManager jedisManager, String key, double min, double max) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var7 = null;

		Set var8;
		try {
			var8 = jedis.zrangeByScoreWithScores(key, min, max);
		} catch (Throwable var17) {
			var7 = var17;
			throw var17;
		} finally {
			if (jedis != null) {
				if (var7 != null) {
					try {
						jedis.close();
					} catch (Throwable var16) {
						var7.addSuppressed(var16);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var8;
	}

	public static List<Object> pipelined(Consumer<Pipeline> commands) {
		return pipelined(jedisManager, commands);
	}

	public static List<Object> pipelined(JedisManager jedisManager, Consumer<Pipeline> commands) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		List var5;
		try {
			Pipeline pipeline = jedis.pipelined();
			commands.accept(pipeline);
			var5 = pipeline.syncAndReturnAll();
		} catch (Throwable var14) {
			var3 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var3.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static Object eval(String script) {
		return eval(jedisManager, script);
	}

	public static Object eval(JedisManager jedisManager, String script) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		Object var4;
		try {
			var4 = jedis.eval(script);
		} catch (Throwable var13) {
			var3 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var3.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var4;
	}

	public static Object eval(String script, List<String> keys, List<String> args) {
		return eval(jedisManager, script, keys, args);
	}

	public static Object eval(JedisManager jedisManager, String script, List<String> keys, List<String> args) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var5 = null;

		Object var6;
		try {
			var6 = jedis.eval(script, keys, args);
		} catch (Throwable var15) {
			var5 = var15;
			throw var15;
		} finally {
			if (jedis != null) {
				if (var5 != null) {
					try {
						jedis.close();
					} catch (Throwable var14) {
						var5.addSuppressed(var14);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var6;
	}

	public static Object eval(String script, int keyCount, String... params) {
		return eval(jedisManager, script, keyCount, params);
	}

	public static Object eval(JedisManager jedisManager, String script, int keyCount, String... params) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var5 = null;

		Object var6;
		try {
			var6 = jedis.eval(script, keyCount, params);
		} catch (Throwable var15) {
			var5 = var15;
			throw var15;
		} finally {
			if (jedis != null) {
				if (var5 != null) {
					try {
						jedis.close();
					} catch (Throwable var14) {
						var5.addSuppressed(var14);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var6;
	}

	public static Long publish(JedisManager jedisManager, String channel, String message) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		Long var5;
		try {
			var5 = jedis.publish(channel, message);
		} catch (Throwable var14) {
			var4 = var14;
			throw var14;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var13) {
						var4.addSuppressed(var13);
					}
				} else {
					jedis.close();
				}
			}

		}

		return var5;
	}

	public static void subscribe(JedisPubSub jedisPubSub, String... channel) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var3 = null;

		try {
			jedis.subscribe(jedisPubSub, channel);
		} catch (Throwable var12) {
			var3 = var12;
			throw var12;
		} finally {
			if (jedis != null) {
				if (var3 != null) {
					try {
						jedis.close();
					} catch (Throwable var11) {
						var3.addSuppressed(var11);
					}
				} else {
					jedis.close();
				}
			}

		}

	}

	public static void subscribe(JedisManager jedisManager, JedisPubSub jedisPubSub, String... channel) {
		Jedis jedis = jedisManager.getJedis();
		Throwable var4 = null;

		try {
			jedis.subscribe(jedisPubSub, channel);
		} catch (Throwable var13) {
			var4 = var13;
			throw var13;
		} finally {
			if (jedis != null) {
				if (var4 != null) {
					try {
						jedis.close();
					} catch (Throwable var12) {
						var4.addSuppressed(var12);
					}
				} else {
					jedis.close();
				}
			}

		}

	}
}
