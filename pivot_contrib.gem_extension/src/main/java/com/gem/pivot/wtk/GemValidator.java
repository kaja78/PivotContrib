package com.gem.pivot.wtk;

import org.apache.pivot.wtk.validation.Validator;

import java.lang.reflect.Field;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 17.6.15<br/>
 * Time: 10:59<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class GemValidator implements Validator {

	public GemValidator(Object srcObject, Field field) {

	}

	@Override
	public boolean isValid(String text) {
		return false;
	}
}
