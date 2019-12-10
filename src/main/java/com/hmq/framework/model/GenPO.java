package com.hmq.framework.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@MappedSuperclass
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class GenPO<ID extends Serializable> implements IPkModel<ID>,ICreateInfoModel<ID> {
	@Id
	private ID id;
	@Override
	public ID getId() {
		return id;
	}
	@Override
	public void setId(ID id) {
		this.id = id;
	}
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	Date createTime;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	Date updateTime;
	ID createrId;
	ID modifierId;
	
	@Override
	public Date getCreateTime() {
		return this.createTime;
	}

	@Override
	public Date getUpdateTime() {
		return this.updateTime;
	}

	@Override
	public ID getModifierId() {
		return this.modifierId;
	}

	@Override
	public ID getCreaterId() {
		return this.createrId;
	}

	@Override
	public void setCreateTime(Date createTime) {
		this.createTime=createTime;
	}

	@Override
	public void setUpdateTime(Date updateTime) {
		this.updateTime=updateTime;
	}

	@Override
	public void setModifierId(ID modifierId) {
		this.modifierId=modifierId;
	}

	@Override
	public void setCreaterId(ID createrId) {
		this.createrId=createrId;
	}


}
