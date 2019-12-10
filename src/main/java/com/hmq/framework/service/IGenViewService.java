package com.hmq.framework.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.hmq.framework.model.IPkModel;
import com.hmq.framework.model.PageModel;
import com.hmq.framework.utis.DataRelation;

public interface IGenViewService<VO, PO extends IPkModel<ID>, ID extends Serializable> extends IGenService<PO, ID> {

	public VO getVOById(ID id);

	public ID saveOneVO(VO vo);
	
	public int saveAllVO(List<VO> voList);
	
	public long countVOByFilter(Map<String, Object> filter, List<DataRelation<VO, ?>> relations);

	public List<VO> findVOByFilter(Map<String, Object> filter,List<DataRelation<VO, ?>> relations);

	public List<VO> findVOByFilter(Map<String, Object> filter, String orderBy, String order,List<DataRelation<VO, ?>> relations);

	public List<VO> findVOByFilter(Map<String, Object> filter, Integer pageIndex, Integer pageSize, String orderBy,
			String order,List<DataRelation<VO, ?>> relations);

	public PageModel<VO> findVOByFilterWithPage(Map<String, Object> filter, Integer pageIndex, Integer pageSize,
			String orderBy, String order,List<DataRelation<VO, ?>> relations);
	
	public long countVOBySpec(Specification<VO> spec, List<DataRelation<VO, ?>> relations);

	public List<VO> findVOBySpec(Specification<VO> spec,List<DataRelation<VO, ?>> relations);
	
	public List<VO> findVOBySpec(Specification<VO> spec, String orderBy, String order,List<DataRelation<VO, ?>> relations);

	public List<VO> findVOBySpec(Specification<VO> spec, Integer pageIndex, Integer pageSize, String orderBy,
			String order,List<DataRelation<VO, ?>> relations);

	public PageModel<VO> findVOBySpecWithPage(Specification<VO> spec, Integer pageIndex, Integer pageSize, String orderBy,
			String order,List<DataRelation<VO, ?>> relations);
}
