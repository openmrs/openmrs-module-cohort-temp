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
import org.openmrs.module.cohort.CohortMemberAttribute;
import org.openmrs.module.cohort.api.TestDataUtils;
import org.openmrs.module.cohort.api.TestSpringConfiguration;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@ContextConfiguration(classes = TestSpringConfiguration.class, inheritLocations = false)
public class CohortMemberAttributeDaoTest extends BaseModuleContextSensitiveTest {

    //Order is salient
    private static final String[] COHORT_MEMBER_ATTRIBUTE_INITIAL_TEST_DATA_XML = new String[]{
            "org/openmrs/module/cohort/api/hibernate/db/CohortDaoTest_initialTestData.xml",
            "org/openmrs/module/cohort/api/hibernate/db/CohortMemberDaoTest_initialTestData.xml",
            "org/openmrs/module/cohort/api/hibernate/db/CohortMemberAttributeTypeDaoTest_initialTestData.xml",
            "org/openmrs/module/cohort/api/hibernate/db/CohortMemberAttributeDaoTest_initialTestData.xml"
    };

    private static final String COHORT_MEMBER_ATTRIBUTE_UUID = "ddadadd8-8034-4a28-9441-2eb2e7679e10";

    private static final String COHORT_MEMBER_ATTRIBUTE_TYPE_UUID = "9eb7fe43-2813-4ebc-80dc-2e5d30251bb7";

    private static final int COHORT_MEMBER_ATTRIBUTE_ID = 1;

    private static final int TEST_COHORT_MEMBER_ATTRIBUTE_ID = 100;

    private static final String COHORT_MEMBER_ATTRIBUTE_VALUE = "cohortMemberAttribute";

    private CohortMemberAttributeDaoImpl dao;

    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Before
    public void setup() throws Exception {
        dao = new CohortMemberAttributeDaoImpl();
        dao.setSessionFactory(sessionFactory);
        for (String dataset : COHORT_MEMBER_ATTRIBUTE_INITIAL_TEST_DATA_XML) {
            executeDataSet(dataset);
        }
    }

    @Test
    public void shouldGetCohortMemberAttributeByUuid() {
        CohortMemberAttribute cohortMemberAttribute = dao.getCohortMemberAttributeByUuid(COHORT_MEMBER_ATTRIBUTE_UUID);
        assertThat(cohortMemberAttribute, notNullValue());
        assertThat(cohortMemberAttribute.getCohortMemberAttributeId(), notNullValue());
        assertThat(cohortMemberAttribute.getCohortMemberAttributeId(), is(COHORT_MEMBER_ATTRIBUTE_ID));
        assertThat(cohortMemberAttribute.getUuid(), equalTo(COHORT_MEMBER_ATTRIBUTE_UUID));
    }

    @Test
    public void shouldGetCohortMemberAttributesByTypeUuid() {
        List<CohortMemberAttribute> memberAttributes = dao.getCohortMemberAttributesByTypeUuid(COHORT_MEMBER_ATTRIBUTE_TYPE_UUID);

        assertThat(memberAttributes, notNullValue());
        assertThat(memberAttributes, hasSize(1));
    }

    @Test
    public void shouldCreateNewCohortMemberAttribute() {
        CohortMemberAttribute cohortAttribute = dao.saveCohortMemberAttribute(TestDataUtils.COHORT_MEMBER_ATTRIBUTE());
        assertThat(cohortAttribute, notNullValue());
        assertThat(cohortAttribute.getCohortMemberAttributeId(), notNullValue());
        assertThat(cohortAttribute.getCohortMemberAttributeId(), equalTo(TEST_COHORT_MEMBER_ATTRIBUTE_ID));
        assertThat(cohortAttribute.getValue(), equalTo(COHORT_MEMBER_ATTRIBUTE_VALUE));
    }

    @Test
    public void shouldVoidCohortMemberAttribute() {
        CohortMemberAttribute attribute = dao.deleteCohortMemberAttribute(COHORT_MEMBER_ATTRIBUTE_UUID);

        assertThat(attribute, notNullValue());
        assertThat(attribute.getVoided(), is(true));
        assertThat(attribute.getVoidReason(), is("Voided via cohort rest call"));
    }

    @Test
    public void shouldPurgeCohortMemberAttribute() {
        dao.purgeCohortMemberAttribute(dao.getCohortMemberAttributeByUuid(COHORT_MEMBER_ATTRIBUTE_UUID));

        assertThat(dao.getCohortMemberAttributeByUuid(COHORT_MEMBER_ATTRIBUTE_UUID), nullValue());
    }
}
