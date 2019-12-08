package com.hmq.framework.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.hmq.framework.dao.IGenDao;
import com.hmq.framework.model.GenPO;
import com.hmq.framework.model.IDModel;
import com.hmq.framework.model.ITokenVO;
import com.hmq.framework.service.IGenService;
import com.hmq.framework.utis.TokenUtil;
import com.hmq.framework.utis.UUIDUtil;
import com.hmq.utis.framework.query.ExpressionUtil;
import com.hmq.utis.framework.query.JpaUtil;

public class GenService<PO extends IDModel<ID>, ID extends Serializable, Dao extends IGenDao<PO, ID>>
		implements IGenService<PO, ID> {

	@Autowired
	private Dao dao;

	protected Dao getDao() {
		return this.dao;
	}

	@Override
	public PO getById(ID id) {
		PO model = this.getDao().getOne(id);
		return model;
	}

	@Override
	public void deleteById(ID id) {
		this.getDao().deleteById(id);
	}

	@Override
	public PO saveOne(PO po) {
		handleData(po);
		return this.getDao().save(po);
	}

	@Override
	public List<PO> saveAll(List<PO> poList) {
		handleData(poList);
		return this.getDao().saveAll(poList);
	}

	private void handleData(PO po) {
		ITokenVO<ID> tokenVO = TokenUtil.getTokenVO();
		if(po instanceof GenPO){
			GenPO<ID> cpo= (GenPO<ID>) po; 
			if (cpo.getCreateTime() == null) {
				cpo.setCreateTime(new Date());
			}
			if (cpo.getCreaterId() == null) {
				cpo.setCreaterId(tokenVO.getUserid());
			}
			cpo.setUpdateTime(new Date());
			cpo.setModifierId(tokenVO.getUserid());
		}

		if (po.getId() == null) {
			po.setId((ID) UUIDUtil.newUUID());
		}
	}

	private void handleData(List<PO> poList) {
		for (PO po : poList) {
			handleData(po);
		}
	}

	@Override
	public long countBySpec(Specification<PO> spec) {
		return this.getDao().count(spec);
	}
	
	@Override
	public List<PO> findBySpec(Specification<PO> spec) {
		return this.findBySpec(spec, null, null, null, null);
	}

	@Override
	public List<PO> findBySpec(Specification<PO> spec, String orderBy, String order) {
		return this.findBySpec(spec, null, null, orderBy, order);
	}
	
	@Override
	public List<PO> findBySpec(Specification<PO> spec, Integer pageIndex, Integer pageSize, String orderBy,
			String order) {
		List<PO> modelList = null;
		if (pageIndex != null) {
			Pageable pageable = JpaUtil.buildPageable(pageIndex, pageSize, orderBy, order);
			modelList = this.getDao().findAll(spec, pageable).getContent();
		} else if (orderBy != null) {
			Sort pageSort = JpaUtil.buildSort(orderBy, order);
			modelList = this.getDao().findAll(spec, pageSort);
		} else {
			modelList = this.getDao().findAll(spec);
		}
		return modelList;
	}

	@Override
	public Page<PO> findBySpecWithPage(Specification<PO> spec, Integer pageIndex, Integer pageSize,
			String orderBy, String order) {
		Pageable pageable = JpaUtil.buildPageable(pageIndex, pageSize, orderBy, order);
		Page<PO> pageData = this.getDao().findAll(spec, pageable);
		return pageData;
	}

	@Override
	public long countByFilter(Map<String, Object> filter) {
		Specification<PO> spec = ExpressionUtil.genExpressionByFilter(filter);
		long count = this.getDao().count(spec);
		return count;
	}

	@Override
	public List<PO> findByFilter(Map<String, Object> filter) {
		return this.findByFilter(filter, null, null, null, null);
	}
	
	
	@Override
	public List<PO> findByFilter(Map<String, Object> filter, String orderBy, String order) {
		return this.findByFilter(filter, null, null, orderBy, order);
	}

	@Override
	public List<PO> findByFilter(Map<String, Object> filter, Integer pageIndex, Integer pageSize, String orderBy,
			String order) {
		Specification<PO> spec = ExpressionUtil.genExpressionByFilter(filter);
		List<PO> modelList = this.findBySpec(spec, pageIndex, pageSize, orderBy, order);
		return modelList;
	}

	@Override
	public Page<PO> findByFilterWithPage(Map<String, Object> filter, Integer pageIndex, Integer pageSize, String orderBy,
			String order) {
		Specification<PO> spec = ExpressionUtil.genExpressionByFilter(filter);
		Page<PO> pageData = this.findBySpecWithPage(spec, pageIndex, pageSize, orderBy, order);
		return pageData;
	}

}
