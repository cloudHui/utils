package event;

import java.util.Objects;

public class Condition {
	private int conditionType;
	private double conditionValue;

	public int getConditionType() {
		return this.conditionType;
	}

	public void setConditionType(int typeValue) {
		this.conditionType = typeValue;
	}

	public double getConditionValue() {
		return this.conditionValue;
	}

	public void setConditionValue(double numValue) {
		this.conditionValue = numValue;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Condition that = (Condition) o;
		return conditionType == that.conditionType &&
				Double.compare(that.conditionValue, conditionValue) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(conditionType, conditionValue);
	}

	/**
	 * 检测是否小于
	 */
	public boolean checkLess(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Condition that = (Condition) o;
		return conditionType == that.conditionType && Double.compare(conditionValue, that.conditionValue) < 0;
	}

	/**
	 * 检测是否大于
	 */
	public boolean checkMore(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Condition that = (Condition) o;
		return conditionType == that.conditionType && Double.compare(conditionValue, that.conditionValue) > 0;
	}

	@Override
	public String toString() {
		return "{" +
				"conType=" + conditionType +
				", conValue=" + conditionValue +
				'}';
	}
}
