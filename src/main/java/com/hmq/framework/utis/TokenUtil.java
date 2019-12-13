package com.hmq.framework.utis;

import java.io.Serializable;

import com.hmq.framework.model.token.ITokenVO;
import com.hmq.framework.model.token.TokenVO;

public class TokenUtil {
	
	public  static <ID extends Serializable> ITokenVO<ID> getTokenVO(){
		return (ITokenVO<ID>) new TokenVO();
	}
}
