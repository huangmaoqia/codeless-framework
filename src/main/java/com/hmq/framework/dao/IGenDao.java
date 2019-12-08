package com.hmq.framework.dao;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.hmq.framework.model.IDModel;

@NoRepositoryBean
public interface IGenDao<PO extends IDModel<ID>, ID extends Serializable>
		extends JpaRepository<PO, ID>, JpaSpecificationExecutor<PO> {
}
