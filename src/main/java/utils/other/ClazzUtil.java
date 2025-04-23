package utils.other;

import java.io.File;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * Java类操作工具类
 */
public class ClazzUtil {

	/**
	 * 获取某个类的实现类
	 *
	 * @param except 需要排除的包名 offline 可以用，分割 "offline,handle"
	 */
	public static List<Class<?>> getAllAssignedClass(Class<?> cls, String except) throws Exception {
		List<Class<?>> classes = new ArrayList<>();
		for (Class<?> c : getClasses(cls, except)) {
			if (cls.isAssignableFrom(c) && !cls.equals(c)) {
				classes.add(c);
			}
		}
		return classes;
	}

	/**
	 * 获取某个类的同包下的所有其他类
	 *
	 * @param except 需要排除的包名 offline 可以用，分割 "offline,handle"
	 */
	public static List<Class<?>> getAllClassExceptPackageClass(Class<?> packageClass, String except) throws Exception {
		List<Class<?>> classes = new ArrayList<>();
		for (Class<?> c : getClasses(packageClass, except)) {
			if (!packageClass.equals(c)) {
				classes.add(c);
			}
		}
		return classes;
	}

	/**
	 * 读取指定类所在包下所有类
	 */
	public static List<Class<?>> getClasses(Class<?> packageClass, String except) throws Exception {
		return getClasses(packageClass.getPackage().getName(), packageClass, except);
	}

	/**
	 * 读取某个包下所有类
	 */
	public static List<Class<?>> getClasses(String pk) throws Exception {
		return getClasses(pk, null, null);
	}

	/**
	 * 读取某个包下所有类
	 */
	public static List<Class<?>> getClasses(String pk, Class<?> cls, String except) throws Exception {
		String path = pk.replace('.', '/');
		URL url;
		if (cls != null) {
			url = cls.getClassLoader().getResource(path);
		} else {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			url = classloader.getResource(path);
		}
		if (url == null) {
			throw new Exception("url get error！" + path);
		}
		String protocol = url.getProtocol();
		if ("file".equals(protocol)) { // 适用于class文件
			return getClasses(new File(url.getFile()), pk, except);
		} else if ("jar".equals(protocol)) { // 适用于jar包
			JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
			return getClassesFromJarFile(jarFile,except, pk);
		} else {
			throw new Exception("未识别的文件协议！" + protocol);
		}
	}

	public static List<Class<?>> getClassesFromJarFile(JarFile jarFile, String except, String include) throws Exception {
		List<Class<?>> classes = new ArrayList<>();
		Enumeration entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = (JarEntry) entries.nextElement();
			String name = entry.getName();
			if (name.endsWith(".class")) {
				name = name.substring(0, name.length() - 6).replaceAll("/", ".");
				if (!name.contains(include)) {
					continue;
				}
				if (needExcept(except, name)) {
					continue;
				}
				classes.add(Class.forName(name, false, Thread.currentThread().getContextClassLoader()));
			}
		}
		return classes;
	}


	//根据路径获取
	public static List<Class<?>> getClasses(File dir, String pk, String except) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<>();
		if (!dir.exists()) {
			return classes;
		}

		for (File f : dir.listFiles()) {
			if (f.isDirectory() && !needExcept(except, f.getName())) {
				classes.addAll(getClasses(f, pk + "." + f.getName(), except));
			}
			String name = f.getName();
			//排除
			if (name.endsWith(".class") && !name.contains("$")) {
				String className = pk + "." + name.substring(0, name.length() - 6);
				classes.add(Class.forName(className, false, Thread.currentThread().getContextClassLoader()));
			}
		}
		return classes;
	}

	private static boolean needExcept(String except, String name){
		String[] split = except.split(",");
		for(String value: split){
			if(name.equals(value)){
				return true;
			}
		}
		return false;
	}

	//动态获取，根据反射，比如获取xx.xx.xx.xx.Action 这个所有的实现类。 xx.xx.xx.xx 表示包名  Action为接口名或者类名
	public static List<Class<?>> getAllActionSubClass(String classPackageAndName) throws Exception {
		Field field;
		Vector v;
		Class<?> cls;
		List<Class<?>> allSubclass = new ArrayList<>();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Class<?> classOfClassLoader = classLoader.getClass();
		cls = Class.forName(classPackageAndName, false, Thread.currentThread().getContextClassLoader());
		while (classOfClassLoader != ClassLoader.class) {
			classOfClassLoader = classOfClassLoader.getSuperclass();
		}
		field = classOfClassLoader.getDeclaredField("classes");
		field.setAccessible(true);
		v = (Vector) field.get(classLoader);
		for (Object o : v) {
			Class<?> c = (Class<?>) o;
			if (cls.isAssignableFrom(c) && !cls.equals(c)) {
				allSubclass.add(c);
			}
		}
		return allSubclass;
	}

}