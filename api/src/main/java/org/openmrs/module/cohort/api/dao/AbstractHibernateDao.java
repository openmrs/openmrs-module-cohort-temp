/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cohort.api.dao;

import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.or;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.openmrs.Auditable;
import org.openmrs.OpenmrsObject;
import org.openmrs.module.cohort.api.dao.search.SearchQueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Slf4j
@SuppressWarnings("unchecked")
@Getter
@Setter
public abstract class AbstractHibernateDao<W extends OpenmrsObject & Auditable> {
	
	private Class<W> clazz;
	
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	@Autowired
	@Qualifier("cohort.search.cohortSearchHandler")
	private SearchQueryHandler searchHandler;
	
	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public W get(String uuid) {
		return (W) getCurrentSession().createCriteria(clazz).add(eq("uuid", uuid)).uniqueResult();
	}
	
	public Collection<W> findAll() {
		return getCurrentSession().createCriteria(clazz).list();
	}
	
	public W createOrUpdate(W entity) {
		getCurrentSession().saveOrUpdate(entity);
		return entity;
	}
	
	public void delete(W entity) {
		getCurrentSession().delete(entity);
	}
	
	public void delete(String uuid) {
		this.delete(this.get(uuid));
	}
	
	public Collection<W> findBy(PropValue propValue) {
		Criteria criteria = getCurrentSession().createCriteria(clazz, "_wc");
		return propValue.getAssociationPath().isPresent()
		        ? criteria.createCriteria("_wc." + propValue.getAssociationPath().get(), "_pv2021")
		                .add(eq("_pv2021." + propValue.getProperty(), propValue.getValue())).list()
		        : criteria.add(eq(propValue.getProperty(), propValue.getValue())).list();
	}
	
	public W findByUniqueProp(PropValue propValue) {
		Criteria criteria = getCurrentSession().createCriteria(clazz, "_cu");
		return (W) (propValue.getAssociationPath().isPresent()
		        ? criteria.createCriteria("_cu." + propValue.getAssociationPath().get(), "_cu2021")
		                .add(eq("_cu2021." + propValue.getProperty(), propValue.getValue())).uniqueResult()
		        : criteria.add(eq(propValue.getProperty(), propValue.getValue())).uniqueResult());
	}
	
	public Collection<W> findByOr(Criterion... predicates) {
		Criteria orByCriteria = getCurrentSession().createCriteria(clazz);
		return orByCriteria.add(or(predicates)).list();
	}
	
	public Collection<W> findByAnd(Criterion... predicates) {
		Criteria andByCriteria = getCurrentSession().createCriteria(clazz);
		return andByCriteria.add(and(predicates)).list();
	}
}
