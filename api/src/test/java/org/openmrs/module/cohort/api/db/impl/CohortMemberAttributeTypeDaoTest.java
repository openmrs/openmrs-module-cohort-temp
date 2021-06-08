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

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.cohort.CohortMemberAttributeType;
import org.openmrs.module.cohort.api.TestDataUtils;
import org.openmrs.module.cohort.api.TestSpringConfiguration;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@ContextConfiguration(classes = TestSpringConfiguration.class, inheritLocations = false)
public class CohortMemberAttributeTypeDaoTest extends BaseModuleContextSensitiveTest {

    private static final String COHORT_MEMBER_ATTRIBUTE_TYPE_NAME = "cohort member attributeType Name";

    private static final Integer COHORT_MEMBER_ATTRIBUTE_TYPE_ID = 103;

    private final String COHORT_MEMBER_ATTRIBUTE_TYPE_UUID = "9eb7fe43-2813-4ebc-80dc-2e5d30251bb7";

    private CohortMemberAttributeTypeDaoImpl dao;

    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Before
    public void setup() throws Exception{
        dao = new CohortMemberAttributeTypeDaoImpl();
        dao.setSessionFactory(sessionFactory);
        String COHORT_MEMBER_ATTRIBUTE_TYPE_INITIAL_TEST_DATA_XML = "org/openmrs/module/cohort/api/hibernate/db/CohortMemberAttributeTypeDaoTest_initialTestData.xml";
        executeDataSet(COHORT_MEMBER_ATTRIBUTE_TYPE_INITIAL_TEST_DATA_XML);
    }

    @Test
    public void shouldGetCohortMemberAttributeTypeByUuid() {
       CohortMemberAttributeType attributeType = dao.getCohortMemberAttributeTypeByUuid(COHORT_MEMBER_ATTRIBUTE_TYPE_UUID);

       assertThat(attributeType, notNullValue());
       assertThat(attributeType.getUuid(), equalTo(COHORT_MEMBER_ATTRIBUTE_TYPE_UUID));
    }

    @Test
    public void shouldGetAllCohortMemberAttributeTypes() {
        List<CohortMemberAttributeType> attributeTypes = dao.getCohortMemberAttributeTypes();

        assertThat(attributeTypes, notNullValue());
        assertThat(attributeTypes, hasSize(2));
        assertThat(attributeTypes, everyItem(hasProperty("name", notNullValue())));
    }

    @Test
    public void shouldCreateNewCohortMemberAttributeType() {
        CohortMemberAttributeType cohortMemberAttributeType = dao.createCohortMemberAttributeType(TestDataUtils.COHORT_MEMBER_ATTRIBUTE_TYPE());
        assertThat(cohortMemberAttributeType, notNullValue());
        assertThat(cohortMemberAttributeType.getCohortMemberAttributeTypeId(), notNullValue());
        assertThat(cohortMemberAttributeType.getCohortMemberAttributeTypeId(), equalTo(COHORT_MEMBER_ATTRIBUTE_TYPE_ID));
        assertThat(cohortMemberAttributeType.getName(), equalTo(COHORT_MEMBER_ATTRIBUTE_TYPE_NAME));
    }

    @Test
    public void shouldVoidCohortMemberAttributeType() {
        CohortMemberAttributeType attributeType = dao.deleteCohortMemberAttributeType(COHORT_MEMBER_ATTRIBUTE_TYPE_UUID);

        assertThat(attributeType, notNullValue());
        assertThat(attributeType.getVoided(), is(true));
        assertThat(attributeType.getVoidReason(), is("Voided via cohort rest call"));
    }

    @Test
    public void shouldPurgeCohortMemberAttributeType() {
        dao.purgeCohortMemberAttribute(dao.getCohortMemberAttributeTypeByUuid(COHORT_MEMBER_ATTRIBUTE_TYPE_UUID));

        assertThat(dao.getCohortMemberAttributeTypeByUuid(COHORT_MEMBER_ATTRIBUTE_TYPE_UUID), nullValue());
    }
}
