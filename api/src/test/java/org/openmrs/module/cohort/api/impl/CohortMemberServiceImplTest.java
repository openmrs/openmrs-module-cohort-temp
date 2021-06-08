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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openmrs.module.cohort.CohortMemberAttribute;
import org.openmrs.module.cohort.CohortMemberAttributeType;
import org.openmrs.module.cohort.api.db.CohortMemberAttributeDao;
import org.openmrs.module.cohort.api.db.CohortMemberAttributeTypeDao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CohortMemberServiceImplTest {

    private final String COHORT_MEMBER_ATTRIBUTE_TYPE_UUID = "6e0d1303-9a41-40ce-b951-3d8e2aadbf99";

    private final String COHORT_MEMBER_ATTRIBUTE_UUID = "32816782-d578-401c-8475-8ccbb26ce001";

    @Mock
    private CohortMemberAttributeDao attributeDao;

    @Mock
    private CohortMemberAttributeTypeDao attributeTypeDao;

    private CohortMemberServiceImpl cohortMemberService;

    @Before
    public void setup() {
        cohortMemberService = new CohortMemberServiceImpl();
        cohortMemberService.setAttributeDao(attributeDao);
        cohortMemberService.setAttributeTypeDao(attributeTypeDao);

    }
    @Test
    public void getCohortMemberAttributeType_shouldReturnMatchingCohortMemberAttributeType() {
        CohortMemberAttributeType cohortMemberAttributeType = mock(CohortMemberAttributeType.class);
        when(attributeTypeDao.getCohortMemberAttributeTypeByUuid(COHORT_MEMBER_ATTRIBUTE_TYPE_UUID)).thenReturn(cohortMemberAttributeType);
        when(cohortMemberAttributeType.getUuid()).thenReturn(COHORT_MEMBER_ATTRIBUTE_TYPE_UUID);

        CohortMemberAttributeType result = cohortMemberService.getCohortMemberAttributeTypeByUuid(COHORT_MEMBER_ATTRIBUTE_TYPE_UUID);
        assertThat(result, notNullValue());
        assertThat(result.getUuid(), is(COHORT_MEMBER_ATTRIBUTE_TYPE_UUID));
    }

    @Test
    public void getAllCohortMemberAttributeTypes() {}

    @Test
    public void saveCohortMemberAttributeType() {
        CohortMemberAttributeType cohortMemberAttributeType = mock(CohortMemberAttributeType.class);
        when(attributeTypeDao.createCohortMemberAttributeType(cohortMemberAttributeType)).thenReturn(cohortMemberAttributeType);
        when(cohortMemberAttributeType.getCohortMemberAttributeTypeId()).thenReturn(1);

        CohortMemberAttributeType result = cohortMemberService.saveCohortMemberAttributeType(cohortMemberAttributeType);
        assertThat(result, notNullValue());
        assertThat(result.getCohortMemberAttributeTypeId(), is(1));
    }

    @Test
    public void purgeCohortMemberAttributeType() {}

    @Test
    public void getCohortMemberAttributeByUuid() {
        CohortMemberAttribute cohortMemberAttribute = mock(CohortMemberAttribute.class);
        when(attributeDao.getCohortMemberAttributeByUuid(COHORT_MEMBER_ATTRIBUTE_UUID)).thenReturn(cohortMemberAttribute);
        when(cohortMemberAttribute.getUuid()).thenReturn(COHORT_MEMBER_ATTRIBUTE_UUID);

        CohortMemberAttribute result = cohortMemberService.getCohortMemberAttributeByUuid(COHORT_MEMBER_ATTRIBUTE_UUID);
        assertThat(result, notNullValue());
        assertThat(result.getUuid(), is(COHORT_MEMBER_ATTRIBUTE_UUID));
    }

    @Test
    public void saveCohortMemberAttribute() {
        CohortMemberAttribute cohortMemberAttribute = mock(CohortMemberAttribute.class);
        when(attributeDao.saveCohortMemberAttribute(cohortMemberAttribute)).thenReturn(cohortMemberAttribute);
        when(cohortMemberAttribute.getCohortMemberAttributeId()).thenReturn(1);

        CohortMemberAttribute result = cohortMemberService.saveCohortMemberAttribute(cohortMemberAttribute);
        assertThat(result, notNullValue());
        assertThat(result.getCohortMemberAttributeId(), is(1));
    }

    @Test
    public void purgeCohortMemberAttribute() {}
}
