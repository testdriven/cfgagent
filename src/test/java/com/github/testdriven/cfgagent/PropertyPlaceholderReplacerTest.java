package com.github.testdriven.cfgagent;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class PropertyPlaceholderReplacerTest {
	@Test
	public void will_replace_placeholder() {
		// given
		PropertyPlaceholderReplacer replacer = new PropertyPlaceholderReplacer();
		Map<String, String> values = new HashMap<>();
		values.put("HOST", "localhost");

		// when
		String actual = replacer.replace("some.property=some://{HOST}", values);

		// then
		Assert.assertEquals("some.property=some://localhost", actual);
	}
}
