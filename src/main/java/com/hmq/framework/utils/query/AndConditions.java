package com.hmq.framework.utils.query;

import org.springframework.data.jpa.domain.Specification;

public class AndConditions<T> extends Conditions<T> implements Specification<T> {
	private static final long serialVersionUID = 1L;

	public AndConditions(){
		super(ELogicalOperator.AND);
	}
}
