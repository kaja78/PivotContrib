package pivot_contrib.util.validator;

import org.apache.pivot.wtk.MessageType;

public class ValidationProblem {
	private final String textKey;
	private final String message;
	private final MessageType messageType;
	
	public ValidationProblem(String textKey, String message,
			MessageType messageType) {
		this.textKey = textKey;
		this.message = message;
		this.messageType = messageType;
	}
	
	public String getTextKey() {
		return textKey;
	}
	public String getMessage() {
		return message;
	}
	public MessageType getMessageType() {
		return messageType;
	}
	
	
}
