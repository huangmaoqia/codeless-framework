package com.hmq.framework.model.page;

import java.util.List;

import org.springframework.data.domain.Page;

public class PageModel<M> {
	private Integer pageIndex=null;
	private Integer pageSize=null;
	private Long count=null;
	private List<M> content=null;
	
	public PageModel(Page<M> page){
		this.pageIndex=page.getNumber()+1;
		this.pageSize=page.getSize();
		this.count=(long) page.getTotalElements();
		this.content=page.getContent();
	}

	public PageModel(Integer pageIndex, Integer pageSize, Long count, List<M> mList) {
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.count = count;
		this.content = mList;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Long getCount() {
		return count;
	}


	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public List<M> getContent() {
		return content;
	}

	public void setContent(List<M> content) {
		this.content = content;
	}

}
