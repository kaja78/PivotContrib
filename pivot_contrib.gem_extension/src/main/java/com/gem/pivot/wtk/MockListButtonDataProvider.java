package com.gem.pivot.wtk;

import com.gem.pivot.wtk.support.ListButtonDataProvider;

import java.util.Arrays;
import java.util.List;

/**
 * <strong>Created with IntelliJ IDEA</strong><br/>
 * User: Jiri Pejsa<br/>
 * Date: 17.6.15<br/>
 * Time: 13:33<br/>
 * <p>To change this template use File | Settings | File Templates.</p>
 */
public class MockListButtonDataProvider implements ListButtonDataProvider<User> {

	public static final List<User> db = Arrays.asList(new User("Jiri", "Pejsa"), new User("Tereza", "Novakova"), new User("Andrej", "Babiš"));

	@Override
	public List<User> getData() {
		return db;
	}
}
