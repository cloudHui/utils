package eventold;

// 自定义事件2
public class NotificationEvent implements Event {
	private final String message;

	public NotificationEvent(String message) {
		this.message = message;
	}

	@Override
	public String getType() {
		return "USER_NOTIFICATION";
	}

	public String getMessage() {
		return message;
	}
}