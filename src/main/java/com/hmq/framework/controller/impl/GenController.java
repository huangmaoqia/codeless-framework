package com.hmq.framework.controller.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hmq.framework.model.IPkModel;
import com.hmq.framework.model.PageModel;
import com.hmq.framework.service.IGenService;

public class GenController<PO extends IPkModel<ID>, ID extends Serializable, Service extends IGenService<PO, ID>> {
	@Autowired
	private Service service;

	protected Service getService() {
		return this.service;
	}

	@GetMapping("/get/{id}")
	public PO getById(@PathVariable ID id) {
		PO model = this.getService().getById(id);
		return model;
	}

	@PostMapping("/delete/{id}")
	public void deleteById(ID id) {
		this.getService().deleteById(id);
	}

	@PostMapping("/saveOne")
	public ID saveOne(@RequestBody PO po) {
		return this.getService().saveOne(po).getId();
	}

	@PostMapping("/saveAll")
	public int saveAll(@RequestBody List<PO> poList) {
		return this.getService().saveAll(poList).size();
	}

	@GetMapping("/search")
	public List<PO> serach(HttpServletRequest request, Integer pageIndex, Integer pageSize, String orderBy,
			String order) {
		Map<String, Object> filter = getParams(request);
		List<PO> poList = this.getService().findByFilter(filter, pageIndex, pageSize, orderBy, order);
		return poList;
	}

	@GetMapping("/count")
	public long count(HttpServletRequest request) {
		Map<String, Object> filter = getParams(request);
		return this.getService().countByFilter(filter);
	}

	@GetMapping("/serachWithPage")
	public PageModel<PO> serachWithPage(HttpServletRequest request, Integer pageIndex, Integer pageSize, String orderBy,
			String order) {
		Map<String, Object> filter = getParams(request);
		PageModel<PO> poPage = this.getService().findByFilterWithPage(filter, pageIndex, pageSize, orderBy, order);
		return poPage;
	}

	private Set<String> pageKeySet = new HashSet<String>();
	{
		pageKeySet.add("pageIndex");
		pageKeySet.add("pageSize");
		pageKeySet.add("orderBy");
		pageKeySet.add("order");
	}

	public Map<String, Object> getParams(HttpServletRequest request) {
		Map<String, String[]> properties = request.getParameterMap();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Iterator<Entry<String, String[]>> entries = properties.entrySet().iterator();
		String key = "";
		String value = "";
		while (entries.hasNext()) {
			Entry<String, String[]> entry = entries.next();
			key = (String) entry.getKey();
			if (pageKeySet.contains(key)) {
				continue;
			}
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(key, value);
		}
		return returnMap;
	}

}
