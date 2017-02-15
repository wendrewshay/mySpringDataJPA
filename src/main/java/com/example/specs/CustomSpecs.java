package com.example.specs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

public class CustomSpecs {

	public static <T> Specification<T> byAuto(final EntityManager entityManager, final T example) {
		//获得当前实体类对象的类型
		final Class<T> cls = (Class<T>) example.getClass();
		
		return new Specification<T>() {

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//新建Predicate列表存储构造的查询条件
				List<Predicate> predicates = new ArrayList<>();
				//获得实体类的EntityType，我们可以从EntityType获得实体类的属性
				EntityType<T> entity = entityManager.getMetamodel().entity(cls);
				//对实体类的所有属性做循环
				for(Attribute<T, ?> attr : entity.getDeclaredAttributes()) {
					Object attrValue = getValue(example, attr);
					if(attrValue != null) {
						//当前属性值为字符类型的时候
						if(attr.getJavaType() == String.class) {
							if(!StringUtils.isEmpty(attrValue)) {
								//构造当前属性like(前后%)属性值查询条件，并添加到条件列表中
								predicates.add(cb.like(
										root.get(attribute(entity, attr.getName(), String.class)),
										pattern((String)attrValue)));
							}
						}else{
							//其余情况下，构造属性和属性值equal查询条件，并添加到条件列表中
							predicates.add(cb.equal(
									root.get(attribute(entity, attr.getName(), attrValue.getClass())),
									attrValue));
						}
					}
				}
				//将条件列表转换成Predicate。
				return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
			
			/**
			 * 通过反射获得实体类对象对应属性的属性值
			 * @param example
			 * @param attr
			 * @return
			 */
			private <T> Object getValue(T example, Attribute<T, ?> attr) {
				return ReflectionUtils.getField((Field)attr.getJavaMember(), example);
			}
			
			/**
			 * 获得实体类的当前属性的SingularAttribute,SingularAttribute包含的是实体类的某个单独属性
			 * @param entity
			 * @param fieldName
			 * @param fieldClass
			 * @return
			 */
			private <T, E> SingularAttribute<T, E> attribute(EntityType<T> entity, String fieldName, Class<E> fieldClass) {
				return entity.getDeclaredSingularAttribute(fieldName, fieldClass);
			}
			
		};
	}
	
	/**
	 * 构造like的查询模式，即前后加%
	 * @param str
	 * @return
	 */
	static private String pattern(String str) {
		return "%" + str + "%";
	}
}
