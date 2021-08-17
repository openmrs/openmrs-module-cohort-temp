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

import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortMemberAttribute;
import org.openmrs.module.cohort.api.db.CohortMemberAttributeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CohortMemberAttributeDaoImpl implements CohortMemberAttributeDao {
	
	@Autowired
	@Setter(AccessLevel.PACKAGE)
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	protected org.hibernate.Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public CohortMemberAttribute getCohortMemberAttributeByUuid(String uuid) {
		return (CohortMemberAttribute) getCurrentSession().createCriteria(CohortMemberAttribute.class)
		        .add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CohortMemberAttribute> getCohortMemberAttributesByTypeUuid(String attributeTypeUuid) {
		return getCurrentSession().createCriteria(CohortMemberAttribute.class).createAlias("attributeType", "cm")
		        .add(Restrictions.eq("cm.uuid", attributeTypeUuid)).list();
	}
	
	@Override
	public CohortMemberAttribute saveCohortMemberAttribute(CohortMemberAttribute cohortMemberAttribute) {
		getCurrentSession().saveOrUpdate(cohortMemberAttribute);
		return cohortMemberAttribute;
	}
	
	@Override
	public CohortMemberAttribute deleteCohortMemberAttribute(String uuid) {
		CohortMemberAttribute cohortMemberAttribute = getCohortMemberAttributeByUuid(uuid);
		cohortMemberAttribute.setVoided(true);
		cohortMemberAttribute.setDateVoided(new Date());
		cohortMemberAttribute.setVoidReason("Voided via cohort rest call");
		cohortMemberAttribute.setVoidedBy(Context.getAuthenticatedUser());
		getCurrentSession().saveOrUpdate(cohortMemberAttribute);
		return cohortMemberAttribute;
	}
	
	@Override
	public void purgeCohortMemberAttribute(CohortMemberAttribute cohortMemberAttribute) {
		getCurrentSession().delete(cohortMemberAttribute);
	}
}
