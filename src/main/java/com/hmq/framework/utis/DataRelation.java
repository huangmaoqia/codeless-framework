package com.hmq.framework.utis;

import java.util.ArrayList;
import java.util.List;

import com.hmq.framework.model.IPkModel;
import com.hmq.framework.service.IGenService;
import com.hmq.framework.service.IGenViewService;
import com.hmq.framework.utils.query.IGetter;
import com.hmq.framework.utils.query.ISetter;

public class DataRelation<S, T extends IPkModel<?>> {

	public DataRelation(IGenService<T, ?> service) {
		this.genService = service;
	}

	public DataRelation(IGenViewService<T, ?, ?> service) {
		this.genViewService = service;
	}

	public IGenService<T, ?> genService = null;

	public IGenViewService<T, ?, ?> genViewService = null;

	List<ForwardRelation<S, T>> forwardRelations = new ArrayList<>();

	List<BackwardRelation<S, T>> backwardRelations = new ArrayList<>();

	public DataRelation<S, T> addForwardRelation(IGetter<S> sGetter, IGetter<T> tGetter) {
		forwardRelations.add(new ForwardRelation<S, T>(sGetter, tGetter));
		return this;
	}

	public <V> DataRelation<S, T> addBackwardRelation(ISetter<S, V> sSetter, IGetter<T> tGetter) {
		backwardRelations.add(new BackwardRelation<S, T>(sSetter, tGetter));
		return this;
	}

}
