package com.hmq.utis.framework.query;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public final class JpaUtil {
	
	private static final Integer PAGESIZE=10; 
	private static final String ORDER="DESC";

	public static Sort buildSort(String orderBy, String order) {
		if (order == null) {
			order = ORDER;
		}
		String[] orderByArr = orderBy.split(",");
		String[] orederArr = order.split(",");
		List<Order> orderList = new ArrayList<Order>();
		if (orderByArr.length != orederArr.length) {
			String[] _orderArr=new String[orderByArr.length];
			for (int i = 0; i < orderByArr.length; ++i) {
				if(i<orederArr.length) {
					_orderArr[i]=orederArr[i];
				}else {
					_orderArr[i]=orederArr[0];
				}
			}
			orederArr=_orderArr;
		} else {
			for (int i = 0; i < orderByArr.length; ++i) {
				Order o = new Order("asc".equalsIgnoreCase(orederArr[i]) ? Direction.ASC : Direction.DESC, orderByArr[i]);
				orderList.add(o);
			}
		}
		Sort pageSort = Sort.by(orderList);
		return pageSort;
	}

	public static Pageable buildPageable(Integer pageIndex, Integer pageSize, String orderBy, String order) {
		Pageable pageable = null;
		if (pageSize == null) {
			pageSize = PAGESIZE;
		}
		
		if (orderBy != null) {
			Sort pageSort = buildSort(orderBy, order);
			pageable = PageRequest.of(pageIndex-1, pageSize, pageSort);
		} else {
			pageable = PageRequest.of(pageIndex-1, pageSize);
		}
		return pageable;
	}
}
