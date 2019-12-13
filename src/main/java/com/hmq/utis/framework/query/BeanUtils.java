package com.hmq.utis.framework.query;

import java.lang.invoke.SerializedLambda;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class BeanUtils {

	private static final Map<Class<?>, WeakReference<SerializedLambda>> FUNC_CACHE = new ConcurrentHashMap<>();

	public static <T> String convertToFieldName(IGetter<T> fn) {
		SerializedLambda lambda = getSerializedLambda(fn);
		String getMethodName = lambda.getImplMethodName();
		if (getMethodName.startsWith("get")) {
			getMethodName = getMethodName.substring(3);
		} else if (getMethodName.startsWith("is")) {
			getMethodName = getMethodName.substring(2);
		}
		return getMethodName.substring(0, 1).toLowerCase() + getMethodName.substring(1);
	}
	
	public static <T,V> String convertToFieldName(ISetter<T,V> fn) {
		SerializedLambda lambda = getSerializedLambda(fn);
		String getMethodName = lambda.getImplMethodName();
		if (getMethodName.startsWith("set")) {
			getMethodName = getMethodName.substring(3);
		}
		return getMethodName.substring(0, 1).toLowerCase() + getMethodName.substring(1);
	}
	
	public static <T> Class<T> getTClass(IGetter<T> fn) {
		SerializedLambda lambda =BeanUtils.getSerializedLambda(fn);
		String instantiatedMethodType=lambda.getInstantiatedMethodType();
		//(Lcom/hmq/universe/model/po/User;)Ljava/lang/Object;
		String className=instantiatedMethodType.substring(2, instantiatedMethodType.indexOf(";"));
		className= className.replace("/", ".");
		
		Class<T> tClass=null;
		try {
			tClass=(Class<T>) BeanUtils.class.getClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tClass;
	}

	public static SerializedLambda getSerializedLambda(Object fn) {
		Class<?> clazz = fn.getClass();
		return Optional.ofNullable(FUNC_CACHE.get(clazz)).map(WeakReference::get).orElseGet(() -> {
			SerializedLambda lambda = null;
			try {
				Method method = fn.getClass().getDeclaredMethod("writeReplace");
				method.setAccessible(Boolean.TRUE);
				lambda = (SerializedLambda) method.invoke(fn);
				FUNC_CACHE.put(fn.getClass(), new WeakReference<>(lambda));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return lambda;
		});
	}
}
