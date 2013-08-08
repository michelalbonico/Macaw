package org.kevoree.library.openchord;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

public class ReadProperties {
	
	public Properties readChordProperties(String file) {
		Properties props = new Properties();
		InputStream in = getClass().getResourceAsStream(file);
		try {
			props.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return props;
	}

}
