package com.hmq.framework.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import com.hmq.framework.model.IDModel;

public interface IGenViewService<VO, PO extends IDModel<ID>, ID extends Serializable> extends IGenService<PO, ID> {

	public VO getVOById(ID id);

	public ID saveOneVO(VO vo);
	
	public int saveAllVO(List<VO> voList);

	public List<VO> findVOByFilter(Map<String, Object> filter);

	public List<VO> findVOByFilter(Map<String, Object> filter, String orderBy, String order);

	public List<VO> findVOByFilter(Map<String, Object> filter, Integer pageIndex, Integer pageSize, String orderBy,
			String order);

	public Page<VO> findVOByFilterWithPage(Map<String, Object> filter, Integer pageIndex, Integer pageSize,
			String orderBy, String order);

	public List<VO> findVOBySpec(Specification<PO> spec);
}
