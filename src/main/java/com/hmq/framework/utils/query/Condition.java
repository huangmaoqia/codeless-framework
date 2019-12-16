package com.hmq.framework.utils.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.hmq.framework.utis.DateUtil;

public class Condition<T> implements Specification<T> {
	private static final long serialVersionUID = 1L;
	private String fieldName;
	private EComparisonOperator op;
	private Object value;

	public enum EComparisonOperator {
		EQ, NE, LIKE, GT, LT, GTE, LTE, IN
	}

	public Condition(String fieldName, EComparisonOperator op, Object value) {
		this.fieldName = fieldName;
		this.op = op;
		this.value = value;
	}

	public Condition(String fieldName, Object value) {
		this.fieldName = fieldName;
		this.value = value;

		int index = fieldName.lastIndexOf("-");
		if (index != -1) {
			String opStr = fieldName.substring(index + 1);
			op = EComparisonOperator.valueOf(opStr);
			this.fieldName = fieldName.substring(0, index);
		} else {
			op = EComparisonOperator.EQ;
		}
	}

//	public Condition(IGetter<T> iGetter, EComparisonOperator op, Object value) {
//		this.fieldName = BeanUtils.convertToFieldName(iGetter);
//		this.op = op;
//		this.value = value;
//	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		Path expression = root.get(fieldName);
		
		if (op == EComparisonOperator.IN) {
			if (value instanceof String) {
				String[] strArr = ((String) value).split(",");
				value = Arrays.asList(strArr);
			}
		}

		
		Class<?> clazz = expression.getJavaType();
		if (clazz == Date.class) {
			if (value instanceof String) {
				this.value = DateUtil.toDate(value.toString());
			} else if (value instanceof Collection) {
				Set<Date> set = new HashSet<Date>();
				Collection<?> collection = (Collection<?>) value;
				for (Object obj : collection) {
					if(obj instanceof String){
						set.add(DateUtil.toDate((String)obj));
					}else if(obj instanceof Date){
						set.add((Date)obj);
					}
				}
				value = set;
			}
		}
		switch (op) {
		case EQ:
			return builder.equal(expression, value);
		case NE:
			return builder.notEqual(expression, value);
		case LIKE:
			return builder.like(expression, "%" + value + "%");
		case LT:
			return builder.lessThan(expression, (Comparable) value);
		case GT:
			return builder.greaterThan(expression, (Comparable) value);
		case LTE:
			return builder.lessThanOrEqualTo(expression, (Comparable) value);
		case GTE:
			return builder.greaterThanOrEqualTo(expression, (Comparable) value);
		case IN:
			In in = builder.in(expression);
			Collection values = (Collection) value;
			if (values!=null && values.size() > 0) {
				for (Object obj : values) {
					in.value(obj);
				}
				return in;
			} else {
				return builder.disjunction();
			}
		default:
			return null;
		}
	}

	public String getFieldName() {
		return fieldName;
	}

	public EComparisonOperator getOp() {
		return op;
	}

	public Object getValue() {
		return value;
	}
	
	

}
