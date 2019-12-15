package com.hmq.utis.framework.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class Conditions<T> implements Specification<T> {
	
	public enum ELogicalOperator {
		OR,AND
	}
	private static final long serialVersionUID = 1L;
	private List<Specification<T>> speList=new ArrayList<Specification<T>>();
	protected ELogicalOperator logicalOperator=ELogicalOperator.AND;
	
	public Conditions(){
		this.logicalOperator=ELogicalOperator.AND;
	}
	
	public Conditions(ELogicalOperator logicalOperator){
		this.logicalOperator=logicalOperator;
	}

	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (!speList.isEmpty()) {
			for (int i=speList.size()-1;i>=0;i--) {
				Specification<T> c =speList.get(i);
				Predicate p=c.toPredicate(root, query, builder);
				if(p!=null){
					predicates.add(p);
				}
			}
		}
		if(!predicates.isEmpty()){
			switch (logicalOperator) {
			case OR:
				return builder.or(predicates.toArray(new Predicate[predicates.size()]));
			case AND:
				return builder.and(predicates.toArray(new Predicate[predicates.size()]));
			default:
				return null;
			}
		}
		return builder.conjunction();
	}
	
	public Conditions<T> addExp(Specification<T> spe){
		speList.add(spe);
		return this;
	}
	
	public Conditions<T> addCdEq(String fieldName,Object value){
		speList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.EQ, value));
		return this;
	}
	public Conditions<T> addCdLike(String fieldName,Object value){
		speList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.LIKE, value));
		return this;
	}
	public Conditions<T> addCdGte(String fieldName,Object value){
		speList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.GTE, value));
		return this;
	}
	public Conditions<T> addCdGt(String fieldName,Object value){
		speList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.GT, value));
		return this;
	}
	
	public Conditions<T> addCdLte(String fieldName,Object value){
		speList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.LTE, value));
		return this;
	}
	public Conditions<T> addCdLt(String fieldName,Object value){
		speList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.LT, value));
		return this;
	}
	
	public Conditions<T> addCdIn(String fieldName,Object value){
		speList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.IN, value));
		return this;
	}
	public Conditions<T> addCdNe(String fieldName,Object value){
		speList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.NE, value));
		return this;
	}
	public Conditions<T> addCd(String fieldName,Object value){
		speList.add(new Condition<T>(fieldName,value));
		return this;
	}
	
	public Conditions<T> addCdEq(IGetter<T> getter,Object value){
		speList.add(new Condition<T>(getter, Condition.EComparisonOperator.EQ, value));
		return this;
	}
	public Conditions<T> addCdLike(IGetter<T> getter,Object value){
		speList.add(new Condition<T>(getter, Condition.EComparisonOperator.LIKE, value));
		return this;
	}
	public Conditions<T> addCdGte(IGetter<T> getter,Object value){
		speList.add(new Condition<T>(getter, Condition.EComparisonOperator.GTE, value));
		return this;
	}
	public Conditions<T> addCdGt(IGetter<T> getter,Object value){
		speList.add(new Condition<T>(getter, Condition.EComparisonOperator.GT, value));
		return this;
	}
	
	public Conditions<T> addCdLEt(IGetter<T> getter,Object value){
		speList.add(new Condition<T>(getter, Condition.EComparisonOperator.LTE, value));
		return this;
	}
	public Conditions<T> addCdLt(IGetter<T> getter,Object value){
		speList.add(new Condition<T>(getter, Condition.EComparisonOperator.LT, value));
		return this;
	}
	
	public Conditions<T> addCdIn(IGetter<T> getter,Object value){
		speList.add(new Condition<T>(getter, Condition.EComparisonOperator.IN, value));
		return this;
	}
	public Conditions<T> addCdNe(IGetter<T> getter,Object value){
		speList.add(new Condition<T>(getter, Condition.EComparisonOperator.NE, value));
		return this;
	}

	public List<Specification<T>> getExpressionList() {
		return speList;
	}
}
