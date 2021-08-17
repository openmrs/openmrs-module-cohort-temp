/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cohort.api.db.impl;

import javax.validation.constraints.NotNull;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortMemberAttributeType;
import org.openmrs.module.cohort.api.db.CohortMemberAttributeTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Setter(AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
public class CohortMemberAttributeTypeDaoImpl implements CohortMemberAttributeTypeDao {
	
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	protected org.hibernate.Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public CohortMemberAttributeType getCohortMemberAttributeTypeByUuid(String uuid) {
		return (CohortMemberAttributeType) getSession().createCriteria(CohortMemberAttributeType.class)
		        .add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CohortMemberAttributeType> getCohortMemberAttributeTypes() {
		return getSession().createCriteria(CohortMemberAttributeType.class).list();
	}
	
	@Override
	public CohortMemberAttributeType createCohortMemberAttributeType(CohortMemberAttributeType cohortMemberAttributeType) {
		getSession().saveOrUpdate(cohortMemberAttributeType);
		return cohortMemberAttributeType;
	}
	
	@Override
	public CohortMemberAttributeType deleteCohortMemberAttributeType(
	        @NotNull CohortMemberAttributeType cohortMemberAttributeType, String voidReason) {
		cohortMemberAttributeType.setRetired(true);
		cohortMemberAttributeType.setRetireReason(voidReason);
		cohortMemberAttributeType.setRetiredBy(Context.getAuthenticatedUser());
		getSession().saveOrUpdate(cohortMemberAttributeType);
		return cohortMemberAttributeType;
	}
	
	@Override
	public void purgeCohortMemberAttribute(CohortMemberAttributeType cohortMemberAttributeType) {
		getSession().delete(cohortMemberAttributeType);
	}
}
