package com.hmq.framework.utils.query;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ExpressionUtil {
	public static <M> Conditions<M> genExpressionByFilter(Map<String,Object> filter){
		Iterator<Entry<String, Object>> entry = filter.entrySet().iterator();
		Conditions<M> expression=new Conditions<M>();
		while (entry.hasNext()) {
			Entry<String, Object> item = entry.next();
			String key = (String) item.getKey();
			Object value = item.getValue();
			expression.addFilter(key, value);
		}
		return expression;
	}
}
