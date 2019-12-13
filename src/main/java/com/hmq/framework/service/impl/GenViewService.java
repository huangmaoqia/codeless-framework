package com.hmq.framework.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.alibaba.fastjson.JSON;
import com.hmq.framework.dao.IGenDao;
import com.hmq.framework.model.GenPO;
import com.hmq.framework.model.GenVO;
import com.hmq.framework.model.IPkModel;
import com.hmq.framework.model.page.PageModel;
import com.hmq.framework.service.IGenViewService;
import com.hmq.framework.utis.DataRelation;
import com.hmq.framework.utis.DataRelationAction;
import com.hmq.utis.framework.query.ExpressionUtil;
import com.hmq.utis.framework.query.JpaUtil;

public class GenViewService<VO, PO extends IPkModel<ID>, ID extends Serializable, Dao extends IGenDao<PO, ID>>
		extends GenService<PO, ID, Dao> implements IGenViewService<VO, PO, ID> {

	private Class<VO> voClass = null;
	private Class<PO> poClass = null;

	protected VO toVO(PO po) {
		inferVOPOClass(po);
		VO vo = JSON.parseObject(JSON.toJSONString(po), voClass);
		return vo;
	}

	protected List<VO> toVO(List<PO> poList) {
		inferVOPOClass(poList);
		return JSON.parseArray(JSON.toJSONString(poList), voClass);
	}

	protected PO toPO(VO vo) {
		inferVOPOClass(vo);
		return JSON.parseObject(JSON.toJSONString(vo), poClass);
	}

	protected List<PO> toPO(List<VO> voList) {
		inferVOPOClass(voList);
		return JSON.parseArray(JSON.toJSONString(voList), poClass);
	}

	@Override
	public VO getVOById(ID id) {
		PO po = this.getById(id);
		VO vo = this.toVO(po);

		List<VO> voList = new ArrayList<>();
		voList.add(vo);
		List<DataRelationAction<VO, ?>> relationActionList = genRelationActionList(columnDataRelations);
		this.relate(voList, relationActionList);

		relationActionList = genRelationActionList(sonDataRelations);
		this.relate(voList, relationActionList);

		return vo;
	}

	@Override
	public ID saveOneVO(VO vo) {
		PO po = this.toPO(vo);
		return this.saveOne(po).getId();
	}

	@Override
	public int saveAllVO(List<VO> voList) {
		if (voList == null || voList.size() == 0) {
			return 0;
		}
		List<PO> poList = this.toPO(voList);
		return this.saveAll(poList).size();
	}

	@Override
	public List<VO> findVOByFilter(Map<String, Object> filter, List<DataRelation<VO, ?>> relations) {
		return this.findVOByFilter(filter, null, null, null, null, null);
	}

	@Override
	public List<VO> findVOByFilter(Map<String, Object> filter, String orderBy, String order,
			List<DataRelation<VO, ?>> relations) {
		return this.findVOByFilter(filter, null, null, orderBy, order, null);
	}

	@Override
	public List<VO> findVOByFilter(Map<String, Object> filter, Integer pageIndex, Integer pageSize, String orderBy,
			String order, List<DataRelation<VO, ?>> relations) {
		Specification<VO> spec = ExpressionUtil.genExpressionByFilter(filter);
		return this.findVOBySpec(spec, pageIndex, pageSize, orderBy, order, null);
	}

	private void inferType(Object obj) {
		String clazzName = obj.getClass().getName();
		if (clazzName.indexOf("$") != -1) {
			clazzName = clazzName.substring(0, clazzName.indexOf("$"));
		}
		if (obj instanceof GenVO) {
			voClass = (Class<VO>) obj.getClass();
			clazzName = clazzName.replace(".vo.", ".po.");
			try {
				poClass = (Class<PO>) voClass.getClassLoader()
						.loadClass(clazzName.substring(0, clazzName.length() - 2));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else if (obj instanceof GenPO) {
			poClass = (Class<PO>) obj.getClass();
			clazzName = clazzName.replace(".po.", ".vo.");
			try {
				voClass = (Class<VO>) poClass.getClassLoader().loadClass(clazzName + "VO");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void inferVOPOClass(Object obj) {
		if (voClass != null)
			return;
		if (obj == null)
			return;
		if (obj instanceof List) {
			if (((List<?>) obj).size() == 0)
				return;
			inferType(((List<?>) obj).get(0));
		} else {
			inferType(obj);
		}
	}

	private void relate(List<VO> voList, List<DataRelationAction<VO, ?>> relationActionList) {
		for (DataRelationAction<VO, ?> relationAction : relationActionList) {
			relationAction.relate(voList);
		}
	}

	private Specification<PO> rebuildSpec(Specification<VO> spec, List<DataRelationAction<VO, ?>> relationActionList) {
		Specification<PO> poSpec = null;
		for (DataRelationAction<VO, ?> relationAction : relationActionList) {
			poSpec = relationAction.rebuildSpec(spec);
		}
		return poSpec;
	}

	private List<DataRelationAction<VO, ?>> genRelationActionList(List<DataRelation<VO, ?>> relations) {
		List<DataRelationAction<VO, ?>> relationActionList = new ArrayList<>();
		if (relations == null) {
			relations = columnDataRelations;
		}
		for (DataRelation<VO, ?> relation : relations) {
			relationActionList.add(new DataRelationAction(relation));
		}
		return relationActionList;
	}
	
	private List<DataRelation<VO, ?>> columnDataRelations = new ArrayList<>();

	protected void addColumnDataRelation(DataRelation<VO, ?> r) {
		columnDataRelations.add(r);
	}

	private List<DataRelation<VO, ?>> sonDataRelations = new ArrayList<>();

	protected void addSonDataRelation(DataRelation<VO, ?> r) {
		sonDataRelations.add(r);
	}

	@Override
	public List<VO> findVOBySpec(Specification<VO> spec, List<DataRelation<VO, ?>> relations) {
		return this.findVOBySpec(spec, null, null, null, null, null);
	}

	@Override
	public List<VO> findVOBySpec(Specification<VO> spec, String orderBy, String order,
			List<DataRelation<VO, ?>> relations) {
		return this.findVOBySpec(spec, null, null, orderBy, order, null);
	}

	@Override
	public List<VO> findVOBySpec(Specification<VO> spec, Integer pageIndex, Integer pageSize, String orderBy,
			String order, List<DataRelation<VO, ?>> relations) {

		List<DataRelationAction<VO, ?>> relationActionList = genRelationActionList(relations);

		Specification<PO> poSpec = rebuildSpec(spec, relationActionList);

		List<PO> poList = this.findBySpec(poSpec, pageIndex, pageSize, orderBy, order);
		List<VO> voList = this.toVO(poList);

		this.relate(voList, relationActionList);
		return voList;
	}


	@Override
	public long countVOByFilter(Map<String, Object> filter, List<DataRelation<VO, ?>> relations) {
		Specification<VO> spec = ExpressionUtil.genExpressionByFilter(filter);
		long count = this.countVOBySpec(spec, relations);
		return count;
	}


	@Override
	public long countVOBySpec(Specification<VO> spec, List<DataRelation<VO, ?>> relations) {
		List<DataRelationAction<VO, ?>> relationActionList = genRelationActionList(relations);
		Specification<PO> poSpec = rebuildSpec(spec, relationActionList);
		long count = this.getDao().count(poSpec);
		return count;
	}

	@Override
	public PageModel<VO> findVOBySpecWithPage(Specification<VO> spec, Integer pageIndex, Integer pageSize,
			String orderBy, String order, List<DataRelation<VO, ?>> relations) {
		
		List<DataRelationAction<VO, ?>> relationActionList = genRelationActionList(relations);

		Specification<PO> poSpec = rebuildSpec(spec, relationActionList);
		
		Pageable pageable = JpaUtil.buildPageable(pageIndex, pageSize, orderBy, order);
		Page<PO> pageData = this.getDao().findAll(poSpec, pageable);
		List<VO> voList = this.toVO(pageData.getContent());
		
		this.relate(voList, relationActionList);

		return new PageModel<VO>(pageData.getNumber()+1,pageData.getSize(),(long)pageData.getTotalElements(),voList);
	}
	
	@Override
	public PageModel<VO> findVOByFilterWithPage(Map<String, Object> filter, Integer pageIndex, Integer pageSize,
			String orderBy, String order, List<DataRelation<VO, ?>> relations) {
		Specification<VO> spec = ExpressionUtil.genExpressionByFilter(filter);
		return this.findVOBySpecWithPage(spec, pageIndex, pageSize, orderBy, order, relations);
	}

}
