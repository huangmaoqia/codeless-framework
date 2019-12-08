package com.hmq.framework.controller.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hmq.framework.model.IDModel;
import com.hmq.framework.service.IGenViewService;

public class GenViewController<VO extends IDModel<ID>, PO extends IDModel<ID>, ID extends Serializable, Service extends IGenViewService<VO, PO, ID>>
		extends GenController<PO,ID,Service> {

	@GetMapping("/getVO/{id}")
	public VO getVOById(@PathVariable ID id) {
		VO vo = this.getService().getVOById(id);
		return vo;
	}

	@PostMapping("/saveOneVO")
	public ID saveOneVO(@RequestBody VO vo) {
		return this.getService().saveOneVO(vo);
	}

	@PostMapping("/saveAllVO")
	public int saveAllVO(@RequestBody List<VO> voList) {
		return this.getService().saveAllVO(voList);
	}

	@GetMapping("/searchVO")
	public List<VO> searchVO(HttpServletRequest request, Integer pageIndex, Integer pageSize, String orderBy,
			String order) {
		Map<String, Object> filter = getParams(request);
		List<VO> voList = this.getService().findVOByFilter(filter, pageIndex, pageSize, orderBy, order);
		return voList;
	}

	@GetMapping("/serachVOWithPage")
	public Page<VO> serachVOWithPage(HttpServletRequest request, Integer pageIndex, Integer pageSize, String orderBy,
			String order) {
		Map<String, Object> filter = getParams(request);
		Page<VO> voPage = this.getService().findVOByFilterWithPage(filter, pageIndex, pageSize, orderBy, order);
		return voPage;
	}

}
