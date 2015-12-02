package com.github.testdriven.cfgagent;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple processor that resolves placeholders with default values against
 * environment variables.
 *
 * @author Matthias Hryniszak
 */
public class PropertyPlaceholderReplacer {
	private static final Pattern PLACEHOLDER_RX = Pattern.compile("\\{(.+?)(:(.+?))?\\}");

	public String replace(String value, Map<String, String> env) {
		Matcher m = PLACEHOLDER_RX.matcher(value);
		while (m.find()) {
			String key = m.group(1);
			if (m.group(1) != null) {
				String replacement = env.containsKey(key) ? env.get(key) : m.group(3);
				if (replacement == null) replacement = "";
				value = value.replace(m.group(0), replacement);
			}
		}
		return value;
	}
}
