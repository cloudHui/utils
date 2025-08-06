package event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class DistinctEventHandler {

	private final Map<Class, List<CallBackListener>> paramMethodMap = new HashMap<>();

	/**
	 * 使用 父类作为通用回调方法参数 减少重复代码
	 */
	@SuppressWarnings("unchecked")
	public <T extends DistinctEvent> void registerEvent(T event, long distinctId, Object owner, String callbackName) throws Exception {
		List<CallBackListener> list;
		Class type = event.getClass();
		if (!paramMethodMap.containsKey(type)) {
			list = new ArrayList<>();
			paramMethodMap.put(type, list);
		}
		list = paramMethodMap.get(type);

		Method callback = owner.getClass().getMethod(callbackName, type.getSuperclass(), Long.class);
		CallBackListener cbl = new CallBackListener();
		cbl.owner = owner;
		cbl.callback = callback;
		cbl.event = event;
		cbl.distinctId = distinctId;

		if (!list.contains(cbl)) {
			list.add(cbl);
		}
	}

	public <T extends DistinctEvent, E extends DistinctObj> void handleEvent(T event, E distinctObj, Object owner, String callbackName) throws Exception {
		Method callback = owner.getClass().getMethod(callbackName, event.getClass(), distinctObj.getClass());
		callback.invoke(owner, event, distinctObj);
	}

	public <T extends DistinctEvent> void send(T param) throws Exception {
		Class<? extends DistinctEvent> type = param.getClass();

		List<CallBackListener> list = new ArrayList<>(paramMethodMap.getOrDefault(type, new ArrayList<>()));

		if (list.isEmpty()) {
			return;
		}
		Set<List<CallBackListener>> tempSet = new HashSet<>();
		tempSet.add(new ArrayList<>(list));

		list.sort((CallBackListener o1, CallBackListener o2) -> o2.event.priority.compareTo(o1.event.priority));
		for (CallBackListener cbl : list) {
			if (!checkHaveCallBack(cbl, tempSet)) {
				return;
			}
			if (cbl.event.isConditionMeet(param))
				cbl.callback.invoke(cbl.owner, param, cbl.distinctId);
		}
	}

	private boolean checkHaveCallBack(CallBackListener cbl, Set<List<CallBackListener>> hashSet) {
		for (List<CallBackListener> list : hashSet) {
			if (list.contains(cbl)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public <T extends DistinctEvent> boolean checkEvent(T event, long distinctId, String callbackName, Object owner) {
		Class<? extends DistinctEvent> type = event.getClass();
		if (!paramMethodMap.containsKey(type)) {
			return false;
		}
		List<CallBackListener> list = paramMethodMap.get(type);
		Method callback;
		try {
			callback = owner.getClass().getMethod(callbackName, event.getClass(), Long.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return false;
		}
		CallBackListener cbl = new CallBackListener();
		cbl.event = event;
		cbl.distinctId = distinctId;
		cbl.owner = owner;
		cbl.callback = callback;
		return list.contains(cbl);
	}

	public <T extends DistinctEvent> boolean checkEvent(T param) {
		Class<? extends DistinctEvent> type = param.getClass();
		return paramMethodMap.containsKey(type);
	}

	public <T extends DistinctEvent, E extends DistinctObj> void unRegister(Class<T> param, long distinctId) {
		if (paramMethodMap.containsKey(param)) {
			List<CallBackListener> list = paramMethodMap.get(param);
			for (CallBackListener cbl : list) {
				if (cbl.distinctId == distinctId) {
					list.remove(cbl);
					break;
				}
			}
		}
	}

	public <T extends DistinctEvent> void unRegister(Class<T> param) {
		paramMethodMap.remove(param);
	}


	private static class CallBackListener<T extends DistinctEvent> {
		T event;
		long distinctId;
		Object owner;
		Method callback;

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (!(obj instanceof CallBackListener)) {
				return false;
			}
			CallBackListener other = (CallBackListener) obj;
			if (other.event == null) {
				return false;
			}
			if (owner.getClass() != other.owner.getClass()) {
				return false;
			}

			if (event.getClass() != other.event.getClass()) {
				return false;
			}
			return distinctId == other.distinctId;
		}
	}

}
