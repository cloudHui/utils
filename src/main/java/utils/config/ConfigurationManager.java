package utils.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.utils.ConfigPathUtils;

public class ConfigurationManager {
	private static ConfigurationManager INSTANCE;
	private static final String FILE_NAME = "app.properties";
	private static final String SERVER_PREFIX = "server";
	private static final String CONNECT_PREFIX = "connect";
	private static final String PROPERTY_PREFIX = "property";
	private static final Pattern DEFAULT_PATTERN = Pattern.compile("([\\w\\d_]+)\\.([\\w\\d_]+)\\.([\\w\\d_]+)");
	private static final Pattern PROPERTY_PATTERN = Pattern.compile("property\\.([\\w\\d_.]+)");
	private Map<String, ServerConfiguration> serverConfigurationMap;
	private Map<String, ConnectConfiguration> connectConfigurationMap;
	private Map<String, String> propertiesMap;

	public static ConfigurationManager getInstance() {
		if (null == INSTANCE) {
			synchronized (FILE_NAME) {
				if (null == INSTANCE) {
					INSTANCE = new ConfigurationManager();
				}
			}
		}

		return INSTANCE;
	}

	private ConfigurationManager() {
		load();
	}

	public String getProperty(String name) {
		return null == this.propertiesMap ? null : this.propertiesMap.get(name);
	}

	public Map<String, ServerConfiguration> getServers() {
		return Collections.unmodifiableMap(this.serverConfigurationMap);
	}

	public Map<String, ConnectConfiguration> getConnects() {
		return Collections.unmodifiableMap(this.connectConfigurationMap);
	}

	public void load() {
		load(FILE_NAME);
	}

	private synchronized void load(String fileName) {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
		Properties properties = new Properties();

		try {
			if (inputStream == null) {
				String filePath = ConfigPathUtils.getConfigFilePath() + fileName;
				File file = new File(filePath);
				if (file.exists()) {
					inputStream = new FileInputStream(filePath);
				}
				if (inputStream == null) {
					filePath = ConfigPathUtils.getResourceFilePath() + fileName;
					file = new File(filePath);
					if (file.exists()) {
						inputStream = new FileInputStream(filePath);
					}
				}
				if (inputStream == null) {
					filePath = ConfigPathUtils.getProjectPath() + fileName;
					file = new File(filePath);
					if (file.exists()) {
						inputStream = new FileInputStream(filePath);
					}
				}
			}

			properties.load(new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)));
		} catch (IOException var5) {
			throw new RuntimeException("Error! failed for load " + fileName, var5);
		}

		this.parse(properties);
	}

	private void parse(Properties properties) {
		String key;
		String value;

		for (Entry<Object, Object> entry : properties.entrySet()) {
			key = (String) entry.getKey();
			value = (String) entry.getValue();
			if (key.startsWith("server.")) {
				this.parseServer(key, value);
			} else if (key.startsWith("connect.")) {
				this.parseConnect(key, value);
			} else if (key.startsWith("property.")) {
				this.parseProperty(key, value);
			} else {
				this.addToPropertiesMap(key, value);
			}
		}

	}

	private void parseServer(String key, String value) {
		Matcher matcher = DEFAULT_PATTERN.matcher(key);
		if (matcher.matches()) {
			if (matcher.groupCount() != 3) {
				throw new RuntimeException(String.format("Invalid server configuration key(%s) formatter!", key));
			}

			String name = matcher.group(2);
			String field = matcher.group(3);
			ServerConfiguration server = null;
			if (null == this.serverConfigurationMap) {
				this.serverConfigurationMap = new HashMap<>();
			} else {
				server = this.serverConfigurationMap.get(name);
			}

			if (null == server) {
				server = new ServerConfiguration();
				server.setName(name);
				this.serverConfigurationMap.put(name, server);
			}

			try {
				this.setField(server, field, value);
			} catch (Exception var8) {
				throw new RuntimeException(String.format("Error! failed for parsing server(%s=%s)!", key, value), var8);
			}
		}

	}

	private void parseConnect(String key, String value) {
		Matcher matcher = DEFAULT_PATTERN.matcher(key);
		if (matcher.matches()) {
			if (matcher.groupCount() != 3) {
				throw new RuntimeException(String.format("Invalid connect configuration key(%s) formatter!", key));
			}

			String name = matcher.group(2);
			String fieldName = matcher.group(3);
			ConnectConfiguration conf = null;
			if (this.connectConfigurationMap == null) {
				this.connectConfigurationMap = new HashMap<>();
			} else {
				conf = this.connectConfigurationMap.get(name);
			}

			if (conf == null) {
				conf = new ConnectConfiguration();
				conf.setName(name);
				this.connectConfigurationMap.put(name, conf);
			}

			try {
				this.setField(conf, fieldName, value);
			} catch (Exception var8) {
				throw new RuntimeException(String.format("Error! failed for parsing connect(%s=%s)!", key, value), var8);
			}
		}

	}

	private void parseProperty(String key, String value) {
		Matcher matcher = PROPERTY_PATTERN.matcher(key);
		if (matcher.matches()) {
			if (matcher.groupCount() != 1) {
				throw new RuntimeException(String.format("Invalid property configuration key(%s) formatter!", key));
			}

			String name = matcher.group(1);
			this.addToPropertiesMap(name, value);
		}

	}

	private void addToPropertiesMap(String key, String value) {
		if (this.propertiesMap == null) {
			this.propertiesMap = new HashMap<>();
		}

		this.propertiesMap.put(key, value);
	}

	private void setField(Object object, String fieldName, String value) throws NoSuchFieldException, IllegalAccessException {
		Class<?> type = object.getClass();
		Field field = type.getDeclaredField(fieldName);
		boolean needSet = !field.isAccessible();
		if (needSet) {
			field.setAccessible(true);
		}

		try {
			field.set(object, this.parseValue(field.getType(), value));
		} finally {
			if (needSet) {
				field.setAccessible(false);
			}

		}

	}

	private Object parseValue(Class<?> type, String value) {
		String var3;
		byte var4;
		if (type.isPrimitive()) {
			var3 = type.getName();
			var4 = -1;
			if (var3.equals("double")) {
				var4 = 5;
			}
			if (var3.equals("int")) {
				var4 = 2;
			}
			if (var3.equals("byte")) {
				var4 = 0;
			}
			if (var3.equals("long")) {
				var4 = 3;
			}
			if (var3.equals("boolean")) {
				var4 = 6;
			}
			if (var3.equals("float")) {
				var4 = 4;
			}
			if (var3.equals("short")) {
				var4 = 1;
			}

			switch (var4) {
				case 0:
					return Byte.parseByte(value);
				case 1:
					return Short.parseShort(value);
				case 2:
					return Integer.parseInt(value);
				case 3:
					return Long.parseLong(value);
				case 4:
					return Float.parseFloat(value);
				case 5:
					return Double.parseDouble(value);
				case 6:
					return Boolean.parseBoolean(value);
			}
		} else {
			var3 = type.getName();
			var4 = -1;
			if (var3.equals("java.lang.String")) {
				var4 = 0;
			}
			if (var4 == 0) {
				return value;
			}
		}

		throw new UnsupportedOperationException("Only primitive type allowed now! type=" + type.getName());
	}

	public Integer getInt(String name, Integer defaultValue) {
		String property = this.getProperty(name);
		return property == null ? defaultValue : Integer.parseInt(property);
	}

	@Override
	public String toString() {
		return "ConfigurationManager{" +
				"serverConfigurationMap=" + serverConfigurationMap +
				", connectConfigurationMap=" + connectConfigurationMap +
				", propertiesMap=" + propertiesMap +
				'}';
	}
}
