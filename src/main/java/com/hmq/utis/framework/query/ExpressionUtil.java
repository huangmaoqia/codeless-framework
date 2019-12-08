package com.hmq.utis.framework.query;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ExpressionUtil {
	public static <M> Expression<M> genExpressionByFilter(Map<String,Object> filter){
		Iterator<Entry<String, Object>> entry = filter.entrySet().iterator();
		Expression<M> expression=new Expression<M>();
		while (entry.hasNext()) {
			Entry<String, Object> item = entry.next();
			String key = (String) item.getKey();
			Object value = item.getValue();
			expression.addCd(key, value);
		}
		return expression;
	}
}
