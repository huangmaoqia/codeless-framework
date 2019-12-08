package com.hmq.framework.utis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.hmq.framework.model.GenPO;
import com.hmq.framework.model.GenVO;
import com.hmq.framework.service.IGenService;
import com.hmq.framework.service.IGenViewService;
import com.hmq.framework.service.IService;
import com.hmq.utis.framework.query.BeanUtils;
import com.hmq.utis.framework.query.Condition;
import com.hmq.utis.framework.query.Expression;
import com.hmq.utis.framework.query.IGetter;
import com.hmq.utis.framework.query.ISetter;

public class DataRelation<S, T> {

	private DataRelation() {
	}

	public static <S, T> DataRelation<S, T> newInstance() {
		return new DataRelation<S, T>();
	}

	private List<T> targetList = null;

	private IService targetService = null;

	private Map<IGetter<S>, IGetter<T>> forwardRelation = new HashMap<>();

	private Map<ISetter<S, ?>, IGetter<T>> backwardRelation = new HashMap<>();

	public DataRelation<S, T> setTargetService(IService targetService) {
		this.targetService = targetService;
		return this;
	}

	public DataRelation<S, T> addForwardRelation(IGetter<S> sGetter, IGetter<T> tGetter) {
		forwardRelation.put(sGetter, tGetter);
		return this;
	}

	public <V> DataRelation<S, T> addBackwardRelation(ISetter<S, V> sSetter, IGetter<T> tGetter) {
		backwardRelation.put(sSetter, tGetter);
		return this;
	}

	public void relate(List<S> sourceList) {
		if(sourceList==null || sourceList.size()==0){
			return;
		}
//		if (targetList == null) 
		{
			Set<IGetter<S>> sgSet = forwardRelation.keySet();
			Expression<T> exp = new Expression<>();
			IGetter<T> oneTg = null;
			for (IGetter<S> sg : sgSet) {
				List<Object> valueList = new ArrayList<>();
				for (S source : sourceList) {
					Object value = sg.apply(source);
					valueList.add(value);
				}
				exp.addCdIn(forwardRelation.get(sg), valueList);
				oneTg = forwardRelation.get(sg);
			}
			Class<T> tClass = BeanUtils.getTClass(oneTg);
			if (GenVO.class.isAssignableFrom(tClass)) {
				targetList = ((IGenViewService) targetService).findVOBySpec(exp);
			} else if (GenPO.class.isAssignableFrom(tClass)) {
				targetList = ((IGenService) targetService).findBySpec(exp);
			}
		}
		if (targetList != null && targetList.size() > 0) {
			Set<IGetter<S>> sgSet = forwardRelation.keySet();
			Map<String, List<T>> key2TMap = new HashMap<>();
			for (T t : targetList) {
				String key = "";
				for (IGetter<S> sg : sgSet) {
					IGetter<T> tGetter = (IGetter<T>) forwardRelation.get(sg);
					key += String.valueOf(tGetter.apply(t).hashCode());
				}
				if (key2TMap.get(key) == null) {
					key2TMap.put(key, new ArrayList<T>());
				}
				key2TMap.get(key).add(t);
			}

			Set<ISetter<S, ?>> ssSet = backwardRelation.keySet();
			for (S source : sourceList) {
				String key = "";
				for (IGetter<S> sg : sgSet) {
					key += String.valueOf(sg.apply(source).hashCode());
				}
				List<T> ktList = key2TMap.get(key);
				if (ktList != null && ktList.size() > 0) {
					for (ISetter ss : ssSet) {
						IGetter<T> tg = backwardRelation.get(ss);
						Object tValue = null;
						if (tg == null) {
							tValue = ktList;
						} else {
							tValue = tg.apply(ktList.get(0));
						}
						ss.apply(source, tValue);
					}
				}
			}
		}
	}

	public void rebuildSpec(Specification<S> spec) {
		if (spec instanceof Expression) {

			List<Specification<S>> expressionList = ((Expression<S>) spec).getExpressionList();

			Set<ISetter<S, ?>> ssSet = backwardRelation.keySet();
			Expression<T> exp = new Expression<T>();
			for (ISetter ss : ssSet) {
				IGetter<T> tg = backwardRelation.get(ss);
				String columnName = BeanUtils.convertToFieldName(ss);
				for (int i = expressionList.size() - 1; i >= 0; i--) {
					if (expressionList.get(i) instanceof Condition) {
						Condition<S> condition = (Condition<S>) expressionList.get(i);
						String fieldName = condition.getFieldName();
						if (columnName.equals(fieldName)) {
							expressionList.remove(i);
							exp.addExp(new Condition<T>(BeanUtils.convertToFieldName(tg), condition.getOp(),
									condition.getValue()));
							break;
						}
					}
				}
			}
			if (exp.getExpressionList().size() > 0) {
				targetList = ((IGenViewService) targetService).findBySpec(exp);
				Set<IGetter<S>> sgSet = forwardRelation.keySet();
				for (IGetter<S> sg : sgSet) {
					IGetter<T> tg = forwardRelation.get(sg);
					Set<Object> valueSet = new HashSet<>();
					for (T r : targetList) {
						valueSet.add(tg.apply(r));
					}
					((Expression<S>) spec).addCdIn(sg, valueSet);
				}
			}
		}
	}

	public Map<IGetter<S>, IGetter<T>> getForwardRelation() {
		return forwardRelation;
	}

	public Map<ISetter<S, ?>, IGetter<T>> getBackwardRelation() {
		return backwardRelation;
	}

	public IService getTargetService() {
		return targetService;
	}

}
