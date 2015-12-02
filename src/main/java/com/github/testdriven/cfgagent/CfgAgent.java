package com.github.testdriven.cfgagent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * A Java agent that reads system property properties from a templated system.properties
 * file and resolves their actual values against environment variables.
 *
 * @author Matthias Hryniszak
 */
public class CfgAgent {
	public static void premain(String args, Instrumentation inst) throws FileNotFoundException, IOException {
		Properties props = new Properties();

		String filename = args;
		if (filename == null || filename.isEmpty()) {
			filename = "system.properties";
		}
		File systemPropertiesFile = new File(filename);
		if (systemPropertiesFile.exists()) {
			props.load(new FileInputStream(systemPropertiesFile));
		}

		Map<String, String> environment = System.getenv();
		PropertyPlaceholderReplacer replacer = new PropertyPlaceholderReplacer();

		for (Entry<Object, Object> variable : props.entrySet()) {
			String value = variable.getValue().toString();
			value = replacer.replace(value, environment);
			if (System.getProperty("cfgagent.debug") != null) {
				System.out.println(variable.getKey() + " = " + value);
			}
			System.setProperty(variable.getKey().toString(), value);
		}
	}
}
