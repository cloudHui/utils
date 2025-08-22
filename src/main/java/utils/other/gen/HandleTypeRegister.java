//package utils.other.gen;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import annotation.ProcessType;
//import utils.other.ClazzUtil;
//
///**
// * 处理类型注册绑定工具类
// */
//public class HandleTypeRegister {
//	protected static final ICFLogger logger = CFLoggerFactory.getLogger(HandleTypeRegister.class);
//
//	/**
//	 * 通用绑定方法
//	 *
//	 * @param packageClass 要扫描的包类
//	 * @param processorMap 处理器映射
//	 */
//	public static void bindAllProcessorInPackage(Class<?> packageClass, Map<Integer, IProcessor> processorMap) {
//		bindProcessors(packageClass, processorMap, null);
//	}
//
//	/**
//	 * 通用绑定方法
//	 *
//	 * @param packageClass 要扫描的包类
//	 * @param processorMap 处理器映射
//	 * @param except       包过滤条件（可选）
//	 */
//	public static void bindAllProcessorWithExceptPackage(Class<?> packageClass, Map<Integer, IProcessor> processorMap, String except) {
//		bindProcessors(packageClass, processorMap, except);
//	}
//
//	/**
//	 * 初始处理工厂
//	 *
//	 * @param factoryClass 包类
//	 * @param handles      处理存储map
//	 * @param <T>          处理类 对象
//	 */
//	public static <T> void initFactory(Class<?> factoryClass, Map<Integer, T> handles) {
//		bindProcessors(factoryClass, handles, null);
//	}
//
//	/**
//	 * 通用绑定方法
//	 *
//	 * @param packageClass 要扫描的包类
//	 * @param processorMap 处理器映射
//	 * @param filter       包过滤条件（可选）
//	 */
//	private static <T> void bindProcessors(Class<?> packageClass, Map<Integer, T> processorMap, String filter) {
//		try {
//			List<Class<?>> classes = ClazzUtil.getClasses(packageClass, filter != null ? filter : "");
//			Map<Class<?>, T> classProcessMap = new HashMap<>();
//
//			for (Class<?> aclass : classes) {
//
//				ProcessType processesType = aclass.getAnnotation(ProcessType.class);
//				if (processesType == null) {
//					continue;
//				}
//
//				int[] value = processesType.value();
//				for (int processId : value) {
//					putHandle(processId, aclass, processorMap, classProcessMap);
//				}
//			}
//
//		} catch (Exception e) {
//			logger.error(" {} bind processors error", packageClass.getPackage().getName(), e);
//		}
//	}
//
//	/**
//	 * 处理器绑定核心方法
//	 *
//	 * @param type            处理类型
//	 * @param aclass          处理类
//	 * @param handles         处理器存储map
//	 * @param classProcessMap 防止重复处理类型 实例化多个处理器 的存储map
//	 * @param <T>             处理类 对象
//	 */
//	private static <T> void putHandle(int type, Class<?> aclass, Map<Integer, T> handles, Map<Class<?>, T> classProcessMap) {
//		T handler = handles.get(type);
//		if (handler != null) {
//			logger.error("putHandle same type:{} old:{} new:{} ",
//					type, handler.getClass(), aclass);
//		}
//
//		handler = classProcessMap.computeIfAbsent(aclass, k -> {
//			try {
//				return (T) aclass.getConstructor().newInstance();
//			} catch (Exception e) {
//				logger.error("init {} newInstance fail type {} ", aclass, type, e);
//			}
//			return null;
//		});
//
//		if (handler != null) {
//			handles.put(type, handler);
//		}
//	}
//}
