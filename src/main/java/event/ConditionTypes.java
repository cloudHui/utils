package event;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 类型集合
 */
public class ConditionTypes {

	// 小于等于的事件需要在小于的类型添加 等于的事件也需要添加
	public static Set<Integer> lessTypeSet = new HashSet<>();

	// 大于等于的事件需要在大于的类型添加 等于的事件也需要添加
	public static Set<Integer> moreTypeSet = new HashSet<>();

	public static Set<Integer> sameTypeSet = new HashSet<>();

	static {
		//配一个枚举 Todo
		//CommonEventCondition instance = CommonEventCondition.getInstance();
		//Class<?> clazz = instance.getClass();
		//for (Method method : clazz.getDeclaredMethods()) {
		//	if (method.getName().contains("Same")) {
		//		addType(sameTypeSet, method, instance);
		//	}
		//	if (method.getName().contains("More")) {
		//		addType(moreTypeSet, method, instance);
		//	}
		//	if (method.getName().contains("Less")) {
		//		addType(lessTypeSet, method, instance);
		//	}
		//}
	}

	/**
	 * 添加类型
	 */
	private static void addType(Set<Integer> set, Method method, Object obj) {
		try {
			set.add((Integer) method.invoke(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
