package com.hmq.framework.utils.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.hmq.framework.utis.BeanUtils;

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
			for (Specification<T> c:speList) {
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
	
	public Conditions<T> addCds(Conditions<T> cds){
		speList.add(cds);
		return this;
	}
	
	public Conditions<T> addCd(Condition<T> cd){
		speList.add(cd);
		return this;
	}
	
	public Conditions<T> addFilter(String fieldName,Object value){
		speList.add(new Condition<T>(fieldName,value));
		return this;
	}
	
	public Conditions<T> addCdEq(String fieldName,Object value){
		speList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.EQ, value));
		return this;
	}
	public Conditions<T> addCdLike(String fieldName,String value){
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
	
	public Conditions<T> addCdIn(String fieldName,Collection<?> value){
		speList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.IN, value));
		return this;
	}
	public Conditions<T> addCdIn(String fieldName,Object[] value){
		speList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.IN, value));
		return this;
	}
	public Conditions<T> addCdNe(String fieldName,Object value){
		speList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.NE, value));
		return this;
	}
	
	
	public Conditions<T> addCdEq(IGetter<T> getter,Object value){
		String fieldName = BeanUtils.convertToFieldName(getter);
		return addCdEq(fieldName, value);
	}
	public Conditions<T> addCdLike(IGetter<T> getter,String value){
		String fieldName = BeanUtils.convertToFieldName(getter);
		return this.addCdLike(fieldName, value);
	}
	public Conditions<T> addCdGte(IGetter<T> getter,Object value){
		String fieldName = BeanUtils.convertToFieldName(getter);
		return this.addCdGte(fieldName, value);
	}
	public Conditions<T> addCdGt(IGetter<T> getter,Object value){
		String fieldName = BeanUtils.convertToFieldName(getter);
		return this.addCdGt(fieldName, value);
	}
	
	public Conditions<T> addCdLte(IGetter<T> getter,Object value){
		String fieldName = BeanUtils.convertToFieldName(getter);
		return this.addCdLte(fieldName, value);
	}
	public Conditions<T> addCdLt(IGetter<T> getter,Object value){
		String fieldName = BeanUtils.convertToFieldName(getter);
		return this.addCdLt(fieldName, value);
	}
	
	public Conditions<T> addCdIn(IGetter<T> getter,Collection<?> value){
		String fieldName = BeanUtils.convertToFieldName(getter);
		return this.addCdIn(fieldName, value);
	}
	public Conditions<T> addCdIn(IGetter<T> getter,Object[] value){
		Set<Object> set = new HashSet<>(Arrays.asList(value));
		String fieldName = BeanUtils.convertToFieldName(getter);
		return this.addCdIn(fieldName, set);
	}
	public Conditions<T> addCdNe(IGetter<T> getter,Object value){
		String fieldName = BeanUtils.convertToFieldName(getter);
		return this.addCdNe(fieldName, value);
	}

	public List<Specification<T>> getExpressionList() {
		return speList;
	}
}
