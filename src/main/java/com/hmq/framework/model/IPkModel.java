package com.hmq.framework.model;

import java.io.Serializable;

public interface IPkModel<ID extends Serializable> {
	
	public ID getId();

	public void setId(ID id);
	
}
