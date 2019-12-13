package com.hmq.utis.framework.query;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

public interface ICondition<T> extends Specification<T> {

	
	public default ICondition<T> andEQ(IGetter<T> getter,Object value){
		EQCondition<T> otherCd=new EQCondition<T>(getter,value);
		return (root, query, builder) -> {
			Predicate otherPredicate = otherCd == null ? null : otherCd.toPredicate(root, query, builder);
			Predicate thisPredicate = this == null ? null : this.toPredicate(root, query, builder);
			return builder.and(thisPredicate,otherPredicate);
		};
	}

}
