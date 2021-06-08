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

import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortMemberAttributeType;
import org.openmrs.module.cohort.api.db.CohortMemberAttributeTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class CohortMemberAttributeTypeDaoImpl implements CohortMemberAttributeTypeDao {

    @Autowired
    @Setter(AccessLevel.PACKAGE)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    protected org.hibernate.Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public CohortMemberAttributeType getCohortMemberAttributeTypeByUuid(String uuid) {
        return (CohortMemberAttributeType) getCurrentSession().createCriteria(CohortMemberAttributeType.class).add(
                Restrictions.eq("uuid", uuid)).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CohortMemberAttributeType> getCohortMemberAttributeTypes() {
        return getCurrentSession().createCriteria(CohortMemberAttributeType.class).list();
    }

    @Override
    public CohortMemberAttributeType createCohortMemberAttributeType(CohortMemberAttributeType cohortMemberAttributeType) {
        getCurrentSession().saveOrUpdate(cohortMemberAttributeType);
        return cohortMemberAttributeType;
    }

    @Override
    public CohortMemberAttributeType deleteCohortMemberAttributeType(@NotNull String uuid) {
        CohortMemberAttributeType cohortMemberAttributeType = getCohortMemberAttributeTypeByUuid(uuid);
        cohortMemberAttributeType.setVoided(true);
        cohortMemberAttributeType.setVoidReason("Voided via cohort rest call");
        cohortMemberAttributeType.setVoidedBy(Context.getAuthenticatedUser());
        getCurrentSession().saveOrUpdate(cohortMemberAttributeType);
        return cohortMemberAttributeType;
    }

    @Override
    public void purgeCohortMemberAttribute(CohortMemberAttributeType cohortMemberAttributeType) {
        getCurrentSession().delete(cohortMemberAttributeType);
    }
}
