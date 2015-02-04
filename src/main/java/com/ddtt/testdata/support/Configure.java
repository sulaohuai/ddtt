package com.ddtt.testdata.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configure extends Properties {
	private static final long serialVersionUID = 4546753667556392327L;
	private static Logger logger = Logger.getLogger(Configure.class);
	private File file;

	public static Configure parse(String pathInProject) {
		return parse(pathInProject, false);
	}

	public static Configure parse(String pathInProject, boolean createIfNotExits) {
		Configure instance = new Configure();
		try {
			logger.info(new File(Configure.class.getClassLoader().getResource("").toURI()).getAbsolutePath()
					+ File.separator + pathInProject + " is loaded from absolute path.");
			URL url = getURL(pathInProject);

			if (url == null)
				if (createIfNotExits) {
					String path = getAbslutePath(pathInProject);
					instance.file = new File(path);
					instance.file.createNewFile();
				} else {
					logger.error("RuntimeException", new RuntimeException(pathInProject + " - File Not Found"));
				}
			else {
				instance.file = new File(url.toURI());
			}

			if (instance.file != null)
				instance.load(new FileInputStream(instance.file));
		} catch (URISyntaxException e) {
			logger.error("URISyntaxException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}

		return instance;
	}


	public String getFullPath() {
		return this.file.getAbsolutePath();
	}

	private static URL getURL(String path) {
		return Configure.class.getClassLoader().getResource(path);
	}

	private static String getAbslutePath(String path) throws URISyntaxException {
		return new File(Configure.class.getClassLoader().getResource("").toURI()).getAbsolutePath() + File.separator
				+ path;
	}
}
