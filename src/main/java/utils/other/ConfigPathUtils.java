package utils.other;

import java.io.File;

public class ConfigPathUtils {

	private static final String RESOURCE_PATH = getSystemSeparator() + "resources" + getSystemSeparator();
	private static final String LOGCONFIG_PATH = "log" + getSystemSeparator();
	private static final String SERVERCONFIG_PATH = "server" + getSystemSeparator();
	private static final String XML_PATH = "xml" + getSystemSeparator();
	private static final String CONFIG_PATH = "config" + getSystemSeparator();
	private static final String PERSIST_PATH = "persist" + getSystemSeparator();
	private static final String REDIS_PATH = "redis" + getSystemSeparator();
	private static final String MYBATIS_PATH = "mybatis" + getSystemSeparator();
	private static final String JAVASCRIPT_PATH = "javascript" + getSystemSeparator();
	private static final String PYTHON_PATH = "python" + getSystemSeparator();
	private static final String JYTHON_PATH = "jython" + getSystemSeparator();
	private static final String LOG_PATH = getSystemSeparator() + "logs" + getSystemSeparator();

	public static String getProjectPath() {
		return System.getProperty("user.dir");
	}

	public static String getSystemSeparator() {
		return System.getProperty("file.separator");
	}

	public static String getResourceFilePath() {
		return getProjectPath() + RESOURCE_PATH;
	}

	public static String getLogFilePath() {
		return getResourceFilePath() + LOGCONFIG_PATH;
	}

	public static String getResourceXMLConfigFilePath() {
		return getResourceFilePath() + XML_PATH;
	}

	public static String getConfigFilePath() {
		return getProjectPath() + CONFIG_PATH;
	}

	public static String getNetworkFilePath() {
		return getResourceFilePath() + SERVERCONFIG_PATH;
	}

	public static String getServerConfigFilePath() {
		return getResourceFilePath() + SERVERCONFIG_PATH;
	}

	public static String getPersistFilePath() {
		return getResourceFilePath() + PERSIST_PATH;
	}

	public static String getRedisFilePath() {
		return getPersistFilePath() + REDIS_PATH;
	}

	public static String getMyBatisFilePath() {
		return getPersistFilePath() + MYBATIS_PATH;
	}

	public static String getJavaScriptPath() {
		return getResourceFilePath() + JAVASCRIPT_PATH;
	}

	public static String getPythonPath() {
		return getResourceFilePath() + PYTHON_PATH;
	}

	public static String getJythonPath() {
		return getResourceFilePath() + JYTHON_PATH;
	}

	public static String getLogPath() {
		return LOG_PATH;
	}

	public static String getSubPath(String pathName) {
		return getSystemSeparator() + pathName + getSystemSeparator();
	}

	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			return false;
		}
		if (!destDirName.endsWith(getSystemSeparator())) {
			destDirName = destDirName + getSystemSeparator();
		}
		return dir.mkdirs();
	}
}
