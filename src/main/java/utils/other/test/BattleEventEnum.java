package utils.other.test;

/**
 * 战斗事件类型定义
 * @author zhong
 */
public enum BattleEventEnum {

	/** 受到技能伤害 */
	ON_BE_HURT(true),

	/** 添加Buff前 */
	ON_ADD_BUFF_BEFORE(false),

	/** 添加Buff后 */
	ON_ADD_BUFF_AFTER(false),

	/** 移除Buff后 */
	ON_REMOVE_BUFF_AFTER(false),

	/** 发动攻击时，也即施放主动技能时 */
	ON_START_ATTACKING(true),

	/** 受到致命伤害 */
	ON_BE_KILLED_HURT(true),

	/** 命中 */
	ON_HIT(true),

	/** 触发闪避效果时 */
	ON_MISS(true),

	/** 触发暴击效果时 */
	ON_CRIT(true),

	/** 触发致命效果时 */
	ON_FATAL(true),

	;

	private final boolean checkSkill;

	BattleEventEnum(boolean checkSkill) {
		this.checkSkill = checkSkill;
	}

	public boolean isCheckSkill() {
		return checkSkill;
	}
}
