package com.hmq.framework.utis;

import java.lang.invoke.SerializedLambda;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.hmq.framework.model.IPkModel;
import com.hmq.framework.utils.query.Condition;
import com.hmq.framework.utils.query.Conditions;
import com.hmq.framework.utils.query.IGetter;
import com.hmq.framework.utils.query.ISetter;
import com.hmq.framework.utils.query.ServiceLink;

public class DataRelationAction<S, T extends IPkModel<?>> {

	private List<T> targetList = null;
	
	private ServiceLink link=null;

	private DataRelation<S, T> dataRelation = null;

	public DataRelationAction(DataRelation<S, T> dataRelation,ServiceLink link) {
		this.dataRelation = dataRelation;
		this.link=link;
	}

	@SuppressWarnings("unchecked")
	public void relate(List<S> sourceList) {
		if (sourceList == null || sourceList.size() == 0) {
			return;
		}
		if (targetList == null) {
			Conditions<T> exp = new Conditions<>();
			for (ForwardRelation<S, T> forwardRelation : dataRelation.forwardRelations) {
				IGetter<S> sg = forwardRelation.getSGetter();
				IGetter<T> tg = forwardRelation.getTGetter();
				Set<Object> valueSet = new HashSet<>();
				for (S source : sourceList) {
					Object value = sg.apply(source);
					valueSet.add(value);
				}
				exp.addCdIn(tg, valueSet);
			}

			if (dataRelation.genService != null) {
				targetList = dataRelation.genService.findBySpec(exp);
			} else if (dataRelation.genViewService != null) {
				targetList = dataRelation.genViewService.findVOBySpec(exp, null,link);
			}
		}
		if (targetList != null && targetList.size() > 0) {
			Map<String, List<T>> key2TMap = new HashMap<>();

			for (T t : targetList) {
				String key = "";
				for (ForwardRelation<S, T> forwardRelation : dataRelation.forwardRelations) {
					IGetter<T> tg = forwardRelation.getTGetter();
					key += String.valueOf(tg.apply(t).hashCode());
					if (key2TMap.get(key) == null) {
						key2TMap.put(key, new ArrayList<T>());
					}
					key2TMap.get(key).add(t);
				}
			}
			for (S source : sourceList) {
				String key = "";
				for (ForwardRelation<S, T> forwardRelation : dataRelation.forwardRelations) {
					IGetter<S> sg = forwardRelation.getSGetter();
					key += String.valueOf(sg.apply(source).hashCode());
				}
				List<T> ktList = key2TMap.get(key);
				if (ktList != null && ktList.size() > 0) {
					for (BackwardRelation<S, T> backwardRelation : dataRelation.backwardRelations) {
						ISetter<S, ?> ss = backwardRelation.getSSetter();
						IGetter<T> tg = backwardRelation.getTGetter();

						Object tValue = null;
						if (tg == null) {
							SerializedLambda lambda=BeanUtils.getSerializedLambda(ss);
							String ttt=lambda.getInstantiatedMethodType();
							if(ttt.indexOf("List;)")!=-1) {
								tValue = ktList;
							}else {
								tValue = ktList.get(0);
							}
						} else {
							tValue = tg.apply(ktList.get(0));
						}
						((ISetter<S, Object>) ss).apply(source, tValue);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <PO> Specification<PO> rebuildSpec(Specification<S> spec) {
		if (spec instanceof Conditions) {

			List<Specification<S>> expressionList = ((Conditions<S>) spec).getExpressionList();

			Conditions<T> exp = new Conditions<T>();
			for (BackwardRelation<S, T> backwardRelation : dataRelation.backwardRelations) {
				ISetter<S, ?> ss = backwardRelation.getSSetter();
				IGetter<T> tg = backwardRelation.getTGetter();
				String columnName = BeanUtils.convertToFieldName(ss);
				for (int i = expressionList.size() - 1; i >= 0; i--) {
					if (expressionList.get(i) instanceof Condition) {
						Condition<S> condition = (Condition<S>) expressionList.get(i);
						String fieldName = condition.getFieldName();
						if (columnName.equals(fieldName)) {
							expressionList.remove(i);
							exp.addCd(new Condition<T>(BeanUtils.convertToFieldName(tg), condition.getOp(),
									condition.getValue()));
							break;
						}
					}
				}
			}
			if (exp.getExpressionList().size() > 0) {
				if (dataRelation.genService != null) {
					targetList = dataRelation.genService.findBySpec(exp);
				} else if (dataRelation.genViewService != null) {
					targetList = dataRelation.genViewService.findVOBySpec(exp, null,link);
				}
				for (ForwardRelation<S, T> forwardRelation : dataRelation.forwardRelations) {
					IGetter<T> tg = forwardRelation.getTGetter();
					IGetter<S> sg = forwardRelation.getSGetter();
					Set<Object> valueSet = new HashSet<>();
					for (T r : targetList) {
						valueSet.add(tg.apply(r));
					}
					((Conditions<S>) spec).addCdIn(sg, valueSet);
				}
			}
		}
		return (Specification<PO>) spec;
	}
}
