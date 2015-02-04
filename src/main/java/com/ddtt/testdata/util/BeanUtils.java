package com.ddtt.testdata.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.util.StringUtils;

public class BeanUtils {

	/**
	 * Enhance Apache BeanUtils to support
	 * "copy value only when the target field is blank"
	 * 
	 * @param dest
	 * @param orig
	 * @param override
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void copyProperties(Object dest, Object orig, boolean override) throws IllegalAccessException,
			InvocationTargetException {

		if (override) {
			org.apache.commons.beanutils.BeanUtils.copyProperties(dest, orig);
			return;
		}

		// Validate existence of the specified beans
		if (dest == null) {
			throw new IllegalArgumentException("No destination bean specified");
		}
		if (orig == null) {
			throw new IllegalArgumentException("No origin bean specified");
		}

		BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
		PropertyUtilsBean propertyUtilsBean = beanUtilsBean.getPropertyUtils();
		// Copy the properties, converting as necessary
		if (orig instanceof DynaBean) {
			DynaProperty origDescriptors[] = ((DynaBean) orig).getDynaClass().getDynaProperties();
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				Object destValue = ((DynaBean) dest).get(name);
				if (propertyUtilsBean.isWriteable(dest, name) && StringUtils.isEmpty(destValue)) {
					Object value = ((DynaBean) orig).get(name);
					beanUtilsBean.copyProperty(dest, name, value);
				}
			}
		} else if (orig instanceof Map && dest instanceof Map) {
			Iterator names = ((Map) orig).keySet().iterator();
			while (names.hasNext()) {
				String name = (String) names.next();
				Object destValue = ((Map) dest).get(name);
				if (/*propertyUtilsBean.isWriteable(dest, name) && */StringUtils.isEmpty(destValue)) {
					Object value = ((Map) orig).get(name);
					((Map)dest).put(name, value);
				}
			}
		} else if (orig instanceof Map) {
			Iterator names = ((Map) orig).keySet().iterator();
			while (names.hasNext()) {
				String name = (String) names.next();
				Object destValue = ((Map) dest).get(name);
				if (propertyUtilsBean.isWriteable(dest, name) && StringUtils.isEmpty(destValue)) {
					Object value = ((Map) orig).get(name);
					beanUtilsBean.copyProperty(dest, name, value);
				}
			}
		} else /* if (orig is a standard JavaBean) */{
			PropertyDescriptor origDescriptors[] = propertyUtilsBean.getPropertyDescriptors(orig);
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				if ("class".equals(name)) {
					continue; // No point in trying to set an object's class
				}
				try {
					Object destValue = propertyUtilsBean.getSimpleProperty(dest, name);
					if (propertyUtilsBean.isReadable(orig, name) && propertyUtilsBean.isWriteable(dest, name)
							 && StringUtils.isEmpty(destValue)) {
						Object value = propertyUtilsBean.getSimpleProperty(orig, name);
						beanUtilsBean.copyProperty(dest, name, value);
					}
				} catch (NoSuchMethodException e) {
					; // Should not happen
				}
			}
		}

	}
}
