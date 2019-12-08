package com.hmq.framework.utis;

import java.util.Arrays;

public class ClassUtil {
	public static boolean isImplementsInterface(Class<?> clazz,Class<?> interfaceClazz) {
		Class<?>[] interfaces=clazz.getInterfaces();
		return Arrays.asList(interfaces).contains(interfaceClazz);
	}
	
//	public static Class<?> loadClassByName(String className){
//		SpringBeanUtil
//		this.getClass().getClassLoader()
//		.loadClass(clazzName.substring(0, clazzName.length() - 2));
//	}
}
