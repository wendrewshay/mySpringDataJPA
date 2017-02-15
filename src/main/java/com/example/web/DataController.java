package com.example.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dao.CustomPersonRepository;
import com.example.dao.PersonRepository;
import com.example.domain.Person;

@RestController
public class DataController {

	//Spring Data JPA 已自动注册bean，所以可以自动注入
	@Autowired
	PersonRepository personRepository;
	@Autowired
	CustomPersonRepository customPersonRepository;
	
	@RequestMapping("/save")
	public Person save(String name, String address, Integer age) {
		Person p = personRepository.save(new Person(null, name, age, address));
		return p;
	}
	
	@RequestMapping("/q1")
	public List<Person> q1(String address) {
		List<Person> people = personRepository.findByAddress(address);
		return people;
	}
	
	@RequestMapping("/q2")
	public Person q2(String name, String address) {
		Person people = personRepository.findByNameAndAddress(name, address);
		return people;
	}
	
	@RequestMapping("/q3")
	public Person q3(String name, String address) {
		Person people = personRepository.withNameAndAddressQuery(name, address);
		return people;
	}
	
	@RequestMapping("/q4")
	public List<Person> q4(String name, String address) {
		List<Person> people = personRepository.withNameAndAddressNamedQuery(name, address);
		return people;
	}
	
	/**
	 * 测试排序
	 * @return
	 */
	@RequestMapping("/sort")
	public List<Person> sort() {
		List<Person> people = personRepository.findAll(new Sort(Direction.ASC, "age"));
		return people;
	}
	
	/**
	 * 测试分页
	 * @return
	 */
	@RequestMapping("/page")
	public Page<Person> page() {
		Page<Person> pagePeople = personRepository.findAll(new PageRequest(0, 2));
		return pagePeople;
	}
	
	/**
	 * 测试自定义的Repository，支持分页功能
	 * @param person
	 * @return
	 */
	@RequestMapping("/auto")
	public Page<Person> auto(Person person) {
		Page<Person> pagePeople = customPersonRepository.findByAuto(person, new PageRequest(0, 2));
		return pagePeople;
	}
}
