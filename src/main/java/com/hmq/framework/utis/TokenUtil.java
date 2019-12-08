package com.hmq.framework.utis;

import java.io.Serializable;

import com.hmq.framework.model.ITokenVO;
import com.hmq.framework.model.TokenVO;

public class TokenUtil {
	
	public  static <ID extends Serializable> ITokenVO<ID> getTokenVO(){
		return (ITokenVO<ID>) new TokenVO();
	}
}
