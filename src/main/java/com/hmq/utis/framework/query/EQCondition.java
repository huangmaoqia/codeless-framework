package com.hmq.utis.framework.query;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.hmq.framework.utis.DateUtil;

public class EQCondition<T> implements Specification<T> {
	private static final long serialVersionUID = 1L;
	private String fieldName;
	private Object value;
	private ICondition<T> next;

	public EQCondition(String fieldName, Object value) {
		this.fieldName = fieldName;
		this.value = value;
	}

	public EQCondition(IGetter<T> iGetter, Object value) {
		this.fieldName = BeanUtils.convertToFieldName(iGetter);
		this.value = value;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		Path<?> expression = root.get(fieldName);
		Class<?> clazz = expression.getJavaType();
		if (clazz == Date.class) {
			if (value instanceof String) {
				this.value = DateUtil.toDate(value.toString());
			}
		}
		return builder.equal(expression, value);
	}

	public ICondition<T> getNext() {
		return next;
	}

	public void setNext(ICondition<T> next) {
		this.next = next;
	}

}
