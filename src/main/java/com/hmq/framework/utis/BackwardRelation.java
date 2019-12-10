package com.hmq.framework.utis;

import com.hmq.utis.framework.query.IGetter;
import com.hmq.utis.framework.query.ISetter;

public class BackwardRelation<S, T> {
	private ISetter<S, ?> sSetter=null;
	private IGetter<T> tGetter=null;
	
	public BackwardRelation(ISetter<S, ?> sSetter, IGetter<T> tGetter) {
		super();
		this.sSetter = sSetter;
		this.tGetter = tGetter;
	}
	
	
	public ISetter<S, ?> getSSetter() {
		return sSetter;
	}
	public IGetter<T> getTGetter() {
		return tGetter;
	}
}
