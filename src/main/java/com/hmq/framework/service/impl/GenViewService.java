package com.hmq.framework.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import com.alibaba.fastjson.JSON;
import com.hmq.framework.dao.IGenDao;
import com.hmq.framework.model.GenPO;
import com.hmq.framework.model.GenVO;
import com.hmq.framework.model.IDModel;
import com.hmq.framework.service.IGenViewService;
import com.hmq.framework.utis.DataRelation;
import com.hmq.utis.framework.query.ExpressionUtil;

public class GenViewService<VO, PO extends IDModel<ID>, ID extends Serializable, Dao extends IGenDao<PO, ID>>
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
		relatedColumn(vo);
		relatedSon(vo);
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
	public List<VO> findVOByFilter(Map<String, Object> filter) {
		return this.findVOByFilter(filter, null, null, null, null);
	}

	@Override
	public List<VO> findVOByFilter(Map<String, Object> filter, String orderBy, String order) {
		return this.findVOByFilter(filter, null, null, orderBy, order);
	}

	@Override
	public List<VO> findVOByFilter(Map<String, Object> filter, Integer pageIndex, Integer pageSize, String orderBy,
			String order) {
		Specification<PO> spec = ExpressionUtil.genExpressionByFilter(filter);
		rebuildSpec(spec);
		List<PO> poList = this.findBySpec(spec);
		List<VO> voList = this.toVO(poList);
		relatedColumn(voList);
		return voList;
	}

	@Override
	public Page<VO> findVOByFilterWithPage(Map<String, Object> filter, Integer pageIndex, Integer pageSize,
			String orderBy, String order) {
		return null;
	}

	@Override
	public List<VO> findVOBySpec(Specification<PO> spec) {
		rebuildSpec(spec);
		List<PO> poList = this.findBySpec(spec);
		List<VO> voList = this.toVO(poList);
		relatedColumn(voList);
		return voList;
	}

	private void inferType(Object obj) {
		String clazzName = obj.getClass().getName();
		if(clazzName.indexOf("$")!=-1) {
			clazzName=clazzName.substring(0, clazzName.indexOf("$"));
		}
		if (obj instanceof GenVO) {
			voClass = (Class<VO>) obj.getClass();
			clazzName = clazzName.replace(".vo.", ".po.");
			try {
				poClass = (Class<PO>) voClass.getClassLoader()
						.loadClass(clazzName.substring(0, clazzName.length() - 2));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (obj instanceof GenPO){
			poClass = (Class<PO>) obj.getClass();
			clazzName = clazzName.replace(".po.", ".vo.");
			try {
				voClass = (Class<VO>) poClass.getClassLoader().loadClass(clazzName + "VO");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
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
			if (((List) obj).size() == 0)
				return;
			inferType(((List) obj).get(0));
		} else {
			inferType(obj);
		}
	}

	protected void relatedColumn(VO vo) {
		List<VO> voList = new ArrayList<>();
		voList.add(vo);
		relatedColumn(voList);
	}

	protected void relatedSon(VO vo) {
		List<VO> voList = new ArrayList<>();
		voList.add(vo);
		relatedSon(voList);
	}

	protected void relatedColumn(List<VO> voList) {
		for (DataRelation<VO, ?> columnDataRelation : columnDataRelations) {
			columnDataRelation.relate(voList);
		}
	}

	protected void relatedSon(List<VO> voList) {
		for (DataRelation<VO, ?> sonDataRelation : sonDataRelations) {
			sonDataRelation.relate(voList);
		}
	}

	private void rebuildSpec(Specification spec) {
		for (DataRelation<VO, ?> columnDataRelation : columnDataRelations) {
			columnDataRelation.rebuildSpec(spec);
		}
	}

	private List<DataRelation<VO, ?>> columnDataRelations = new ArrayList<>();

	protected void addColumnDataRelation(DataRelation<VO, ?> r) {
		columnDataRelations.add(r);
	}

	private List<DataRelation<VO, ?>> sonDataRelations = new ArrayList<>();

	protected void addSonDataRelation(DataRelation<VO, ?> r) {
		sonDataRelations.add(r);
	}

}
