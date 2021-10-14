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
import org.openmrs.Retireable;
import org.openmrs.Voidable;
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
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		includeRetiredObjects(criteria, false);
		return (W) criteria.add(eq("uuid", uuid)).uniqueResult();
	}
	
	public Collection<W> findAll() {
		return this.findAll(false);
	}
	
	public Collection<W> findAll(boolean includeRetired) {
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		includeRetiredObjects(criteria, includeRetired);
		return criteria.list();
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
	
	/**
	 * By default, retired/voided objects are excluded for searches
	 * 
	 * @param propValue Property and value
	 * @return A Collection of W objects
	 */
	public Collection<W> findBy(PropValue propValue) {
		return this.findBy(propValue, false);
	}
	
	public Collection<W> findBy(PropValue propValue, boolean includeRetired) {
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		includeRetiredObjects(criteria, includeRetired);
		return propValue.getAssociationPath().isPresent()
		        ? criteria.createCriteria(propValue.getAssociationPath().get(), "_pv2021")
		                .add(eq("_pv2021." + propValue.getProperty(), propValue.getValue())).list()
		        : criteria.add(eq(propValue.getProperty(), propValue.getValue())).list();
	}
	
	public W findByUniqueProp(PropValue propValue) {
		return this.findByUniqueProp(propValue, false);
	}
	
	public W findByUniqueProp(PropValue propValue, boolean includeRetired) {
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		includeRetiredObjects(criteria, includeRetired);
		return (W) (propValue.getAssociationPath().isPresent()
		        ? criteria.createCriteria(propValue.getAssociationPath().get(), "_cu2021")
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
	
	protected boolean isVoidable() {
		return Voidable.class.isAssignableFrom(clazz);
	}
	
	protected boolean isRetirable() {
		return Retireable.class.isAssignableFrom(clazz);
	}
	
	protected void handleVoidable(Criteria criteria) {
		criteria.add(eq("voided", false));
	}
	
	protected void handleRetirable(Criteria criteria) {
		criteria.add(eq("retired", false));
	}
	
	protected void includeRetiredObjects(Criteria criteria, boolean includeRetired) {
		if (!includeRetired) {
			if (isVoidable()) {
				handleVoidable(criteria);
			} else if (isRetirable()) {
				handleRetirable(criteria);
			}
		}
	}
}
