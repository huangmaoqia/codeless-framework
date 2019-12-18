package com.hmq.framework.service;

import java.io.Serializable;

import com.hmq.framework.model.IPkModel;

public interface ICommonService<VO, PO extends IPkModel<ID>, ID extends Serializable> extends IGenService<PO, ID>,IGenViewService<VO, PO , ID> {

}
