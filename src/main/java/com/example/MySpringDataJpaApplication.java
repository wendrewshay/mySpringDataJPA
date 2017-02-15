package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.support.CustomRepositoryFactoryBean;

@SpringBootApplication
//配置类上配置@EnableJpaRepositories，并指定repositoryFactoryBeanClass，让我们自定义的Repository起效
//如果我们不需要自定义Repository实现，则无需添加@EnableJpaRepositories注解，因为@SpringBootApplication
//包含的@EnableAutoConfiguration注解已经开启了对Spring Data JPA的支持
@EnableJpaRepositories(repositoryFactoryBeanClass=CustomRepositoryFactoryBean.class)
public class MySpringDataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySpringDataJpaApplication.class, args);
	}
}
