package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.domain.Person;
import com.example.support.CustomRepository;

/**
 * 继承自定义的Repository
 * @author xiawq
 *
 */
public interface CustomPersonRepository extends CustomRepository<Person, Long>{

	List<Person> findByAddress(String address);
	Person findByNameAndAddress(String name, String address);
	
	@Query("select p from Person p where p.name=:name and p.address=:address")
	Person withNameAndAddressQuery(@Param("name") String name, @Param("address") String address);
	Person withNameAndAddressNamedQuery(String name, String address);
}
