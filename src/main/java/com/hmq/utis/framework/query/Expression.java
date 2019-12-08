package com.hmq.utis.framework.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class Expression<T> implements Specification<T> {
	
	public enum ELogicalOperator {
		OR,AND
	}
	private static final long serialVersionUID = 1L;
	private List<Specification<T>> expressionList=new ArrayList<Specification<T>>();
	private ELogicalOperator logicalOperator=ELogicalOperator.AND;
	
	public Expression(){
		this.logicalOperator=ELogicalOperator.AND;
	}
	
	public Expression(ELogicalOperator logicalOperator){
		this.logicalOperator=logicalOperator;
	}

	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (!expressionList.isEmpty()) {
			for (int i=expressionList.size()-1;i>=0;i--) {
				Specification<T> c =expressionList.get(i);
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
	
	public void addExp(Specification<T> expression){
		expressionList.add(expression);
	}
	
	public void addCdEq(String fieldName,Object value){
		expressionList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.EQ, value));
	}
	public void addCdLike(String fieldName,Object value){
		expressionList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.LIKE, value));
	}
	public void addCdGreaterThanOrEqualTo(String fieldName,Object value){
		expressionList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.GTE, value));
	}
	public void addCdGreaterThan(String fieldName,Object value){
		expressionList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.GT, value));
	}
	
	public void addCdLessThanOrEqualTo(String fieldName,Object value){
		expressionList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.LTE, value));
	}
	public void addCdLessThan(String fieldName,Object value){
		expressionList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.LT, value));
	}
	
	public void addCdIn(String fieldName,Object value){
		expressionList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.IN, value));
	}
	public void addCdNotEq(String fieldName,Object value){
		expressionList.add(new Condition<T>(fieldName, Condition.EComparisonOperator.NE, value));
	}
	public void addCd(String fieldName,Object value){
		expressionList.add(new Condition<T>(fieldName,value));
	}
	
	/*
	 * 仿mybatis-plus的Lambda查询实现
	 * 好处是编译阶段就能检查参数名是否写正确,编码过程可以利用ide的代码提示,不需要关注参数名大小写
	 * 如果是直接写sql,则数据库字段变动,需要手动改全所有使用的地方
	 */
	public void addCdEq(IGetter<T> getter,Object value){
		expressionList.add(new Condition<T>(getter, Condition.EComparisonOperator.EQ, value));
	}
	public void addCdLike(IGetter<T> getter,Object value){
		expressionList.add(new Condition<T>(getter, Condition.EComparisonOperator.LIKE, value));
	}
	public void addCdGreaterThanOrEqualTo(IGetter<T> getter,Object value){
		expressionList.add(new Condition<T>(getter, Condition.EComparisonOperator.GTE, value));
	}
	public void addCdGreaterThan(IGetter<T> getter,Object value){
		expressionList.add(new Condition<T>(getter, Condition.EComparisonOperator.GT, value));
	}
	
	public void addCdLessThanOrEqualTo(IGetter<T> getter,Object value){
		expressionList.add(new Condition<T>(getter, Condition.EComparisonOperator.LTE, value));
	}
	public void addCdLessThan(IGetter<T> getter,Object value){
		expressionList.add(new Condition<T>(getter, Condition.EComparisonOperator.LT, value));
	}
	
	public void addCdIn(IGetter<T> getter,Object value){
		expressionList.add(new Condition<T>(getter, Condition.EComparisonOperator.IN, value));
	}
	public void addCdNotEq(IGetter<T> getter,Object value){
		expressionList.add(new Condition<T>(getter, Condition.EComparisonOperator.NE, value));
	}

	public List<Specification<T>> getExpressionList() {
		return expressionList;
	}
}
