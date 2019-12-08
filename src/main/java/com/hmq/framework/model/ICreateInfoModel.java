package com.hmq.framework.model;

import java.io.Serializable;
import java.util.Date;

public interface ICreateInfoModel<ID extends Serializable>{
	public Date getCreateTime();
	public Date getUpdateTime();
	public ID getModifierId();
	public ID getCreaterId();
	
	public void setCreateTime(Date createTime);
	public void setUpdateTime(Date updateTime);
	public void setModifierId(ID modifierId);
	public void setCreaterId(ID createrId);
}
