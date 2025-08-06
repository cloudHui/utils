package event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CommonInConditionEvent extends DistinctEvent {
	protected String eventType;
	/**
	 * 当前注册事件的条件
	 */
	private List<CommonInConditionObject> registerEventConditions = new ArrayList<>();
	private int accumulativeTotal;

	public CommonInConditionEvent() {
	}

	public CommonInConditionEvent(int accumulativeTotal) {
		this.accumulativeTotal = accumulativeTotal;
	}

	public List<CommonInConditionObject> getConditionData() {
		return registerEventConditions;
	}

	public void setConditionData(List<CommonInConditionObject> conditionData) {
		this.registerEventConditions = conditionData;
	}

	public int getAccumulativeTotal() {
		return accumulativeTotal;
	}

	public void setAccumulativeTotal(int accumulativeTotal) {
		this.accumulativeTotal = accumulativeTotal;
	}

	public <T extends DistinctEvent> boolean dealParameterCheckBeforeSendEvent(T sendEvent, T registerEvent) {
		if (!registerEvent.getClass().isAssignableFrom(sendEvent.getClass())) {
			return false;
		}
		if (registerEventConditions == null || registerEventConditions.size() < 1) {
			return true;
		}
		return dealCommonInConditionEvent(((CommonInConditionEvent) sendEvent).getConditionData());
	}

	/**
	 * 判断 真实发生的事件是否满足 注册的事件的条件
	 *
	 * @param comeTrues 真实发生的事件条件
	 * @return 是否满足注册事件的条件
	 */
	private boolean dealCommonInConditionEvent(List<CommonInConditionObject> comeTrues) {
		// 如果注册的事件没有条件 就是满足条件的
		if (registerEventConditions == null || registerEventConditions.size() <= 0) {
			return true;
		}
		// 如果注册的事件有条件 但是发生的事件没有条件 不满足条件直接返回
		if (comeTrues == null || comeTrues.size() <= 0) {
			return false;
		}

		// 注册事件关心的条件集合 去重
		// 只要满足 注册事件相同的条件的其中之一就行 所以 注册条件每个条件是或的关系
		Set<Integer> registerEventConditionSet = new HashSet<>();
		int conditionType;
		for (CommonInConditionObject registerCondition : registerEventConditions) {
			conditionType = registerCondition.getConditionType();
			registerEventConditionSet.add(conditionType);
		}

		// 注册事件条件 遍历 真实事件条件是否满足
		// 现在只要满足 注册事件相同的条件的其中之一就行 所以 注册相同条件每个条件是或的关系
		for (CommonInConditionObject register : registerEventConditions) {
			conditionType = register.getConditionType();
			if (registerEventConditionSet.contains(conditionType) && comeTrueFitRegister(comeTrues, register)) {
				registerEventConditionSet.remove(conditionType);
			}
		}
		return registerEventConditionSet.isEmpty();
	}

	/**
	 * 检测真实发生的事件里是否有满足注册条件的事件
	 *
	 * @param comeTrues 真实发生的事件
	 * @param registers 注册事件的条件
	 * @return 是否满足条件
	 */
	private boolean comeTrueFitRegister(List<CommonInConditionObject> comeTrues, CommonInConditionObject registers) {
		int conditionType = registers.getConditionType();
		boolean less = ConditionTypes.lessTypeSet.contains(conditionType);
		boolean more = ConditionTypes.moreTypeSet.contains(conditionType);
		boolean same = ConditionTypes.sameTypeSet.contains(conditionType);

		for (CommonInConditionObject comeTrue : comeTrues) {
			if (less && comeTrue.checkLess(registers)) {
				return true;
			}
			if (more && comeTrue.checkMore(registers)) {
				return true;
			}
			if (same && comeTrue.equals(registers)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean isConditionMeet(DistinctEvent other) {
		return dealParameterCheckBeforeSendEvent(other, this);
	}

	/**
	 * 注册监听事件
	 */
	public void initWhenRoleLoginRegisterEvent(int[][] condition, int eventId, String eventType) throws Exception {
		if (condition == null || condition.length < 1) {
			return;
		}

		int conditionType;
		CommonInConditionObject object;
		for (int[] ints : condition) {
			if (ints.length < 1 || ints.length > 2) {
				logger.info("condition of the {} parameter setting error that eventId:{}", eventType, eventId);
				continue;
			}
			conditionType = ints[0];
			// 条件表里的 小于 大于等于 条件都没找到 注册事件
			if (!ConditionTypes.lessTypeSet.contains(conditionType) && !ConditionTypes.moreTypeSet.contains(conditionType) && !ConditionTypes.sameTypeSet.contains(conditionType)) {
				throw new Exception(eventType + "Some conditions for code configuration are empty. eventId" + eventId + " conditionType: " + conditionType);
			}
			object = new CommonInConditionObject();
			object.setConditionType(conditionType);
			object.setConditionValue(ints[1]);
			this.registerEventConditions.add(object);
		}
		this.eventType = eventType;
	}

	@Override
	public String toString() {
		return "Event{" +
				"register=" + registerEventConditions.toString() +
				", acc=" + accumulativeTotal +
				'}';
	}
}
