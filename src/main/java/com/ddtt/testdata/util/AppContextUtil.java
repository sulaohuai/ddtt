package com.ddtt.testdata.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppContextUtil {
	private static ApplicationContext ctx = null;

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

	public static Object getBean(String id) {
		return getApplicationContext().getBean(id);
	}

	public static <T> T getBean(String id, Class<T> clazz) {
		return getApplicationContext().getBean(id, clazz);
	}

	static {
		ctx = new ClassPathXmlApplicationContext("classpath:bean-ddtt.xml");
	}
}
