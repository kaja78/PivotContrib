package com.gem.pivot.wtk.validators;

import org.apache.pivot.wtk.validation.Validator;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 17.6.15<br/>
 * Time: 11:20<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class EmptyValidator implements Validator {

	@Override
	public boolean isValid(String text) {
		return true;
	}
}
