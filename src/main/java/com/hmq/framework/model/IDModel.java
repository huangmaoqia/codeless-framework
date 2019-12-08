package com.hmq.framework.model;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class IDModel<ID extends Serializable> {
	@Id
	private ID id;

	
	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}
	
}
