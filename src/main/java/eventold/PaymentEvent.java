package eventold;

// 自定义事件1
public class PaymentEvent implements Event {
	private final String userId;
	private final double amount;

	public PaymentEvent(String userId, double amount) {
		this.userId = userId;
		this.amount = amount;
	}

	@Override
	public String getType() {
		return "PAYMENT_PROCESSED";
	}

	public String getUserId() {
		return userId;
	}

	public double getAmount() {
		return amount;
	}
}