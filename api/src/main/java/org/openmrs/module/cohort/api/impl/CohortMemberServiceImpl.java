/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cohort.api.impl;

import lombok.AccessLevel;
import lombok.Setter;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.cohort.CohortMemberAttribute;
import org.openmrs.module.cohort.CohortMemberAttributeType;
import org.openmrs.module.cohort.api.CohortMemberService;
import org.openmrs.module.cohort.api.db.CohortMemberAttributeDao;
import org.openmrs.module.cohort.api.db.CohortMemberAttributeTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("cohortMemberService")
@Setter(AccessLevel.PACKAGE)
@Transactional
public class CohortMemberServiceImpl extends BaseOpenmrsService implements CohortMemberService {

    @Autowired
    private CohortMemberAttributeTypeDao attributeTypeDao;

    @Autowired
    private CohortMemberAttributeDao attributeDao;

    @Override
    public CohortMemberAttributeType getCohortMemberAttributeTypeByUuid(String uuid) {
        return attributeTypeDao.getCohortMemberAttributeTypeByUuid(uuid);
    }

    @Override
    public List<CohortMemberAttributeType> getAllCohortMemberAttributeTypes() {
        return attributeTypeDao.getCohortMemberAttributeTypes();
    }

    @Override
    @Transactional
    public CohortMemberAttributeType saveCohortMemberAttributeType(CohortMemberAttributeType cohortMemberAttributeType) {
        return attributeTypeDao.createCohortMemberAttributeType(cohortMemberAttributeType);
    }

    @Override
    public CohortMemberAttributeType deleteCohortMemberAttributeType(CohortMemberAttributeType cohortMemberAttributeType, String voidReason) {
        return attributeTypeDao.deleteCohortMemberAttributeType(cohortMemberAttributeType, voidReason);
    }

    @Override
    public void purgeCohortMemberAttributeType(CohortMemberAttributeType cohortMemberAttributeType) {
        attributeTypeDao.purgeCohortMemberAttribute(cohortMemberAttributeType);
    }

    @Override
    public CohortMemberAttribute getCohortMemberAttributeByUuid(String uuid) {
        return attributeDao.getCohortMemberAttributeByUuid(uuid);
    }

    @Override
    public List<CohortMemberAttribute> getCohortMemberAttributeByTypeUuid(String attributeTypeUuid) {
        return attributeDao.getCohortMemberAttributesByTypeUuid(attributeTypeUuid);
    }

    @Override
    public CohortMemberAttribute saveCohortMemberAttribute(CohortMemberAttribute cohortMemberAttribute) {
        return attributeDao.saveCohortMemberAttribute(cohortMemberAttribute);
    }

    @Override
    public void purgeCohortMemberAttribute(CohortMemberAttribute cohortMemberAttribute) {
        attributeDao.purgeCohortMemberAttribute(cohortMemberAttribute);
    }
}
