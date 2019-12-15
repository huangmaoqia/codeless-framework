package com.hmq.utis.framework.query;

import org.springframework.data.jpa.domain.Specification;

public class AndExpression<T> extends Conditions<T> implements Specification<T> {
	private ELogicalOperator logicalOperator=ELogicalOperator.AND;
}
