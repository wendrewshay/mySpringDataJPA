package com.example.support;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 定义接口，继承JpaRepository可以使用其提供的方法，继承JpaSpecificationExecutor可以让我们具备使用Specification的能力
 * @author xiawq
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface CustomRepository <T, ID extends Serializable> 
	extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>{

	Page<T> findByAuto(T example, Pageable pageable);
}
