package com.gem.pivot.wtk;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 16.6.15<br/>
 * Time: 12:33<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class ValidationError {

	public String field;
	public String error;

	public ValidationError(String field, String error) {
		this.field = field;
		this.error = error;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ValidationError{");
		sb.append("field='").append(field).append('\'');
		sb.append(", error='").append(error).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
