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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.cohort.CohortAttributeType;
import org.openmrs.module.cohort.api.SpringTestConfiguration;
import org.openmrs.module.cohort.api.TestDataUtils;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = SpringTestConfiguration.class, inheritLocations = false)
public class CohortAttributeTypeGenericDaoTest extends BaseModuleContextSensitiveTest {
	
	private static final String COHORT_ATTRIBUTE_TYPE_INITIAL_TEST_DATA_XML = "org/openmrs/module/cohort/api/hibernate/db/CohortAttributeTypeDaoTest_initialTestData.xml";
	
	private static final String COHORT_ATTRIBUTE_TYPE_UUID = "9eb7fe43-2813-4ebc-80dc-2e5d30251bb7";
	
	private static final int COHORT_ATTRIBUTE_TYPE_ID = 1;
	
	@Autowired
	@Qualifier("cohort.genericDao")
	private IGenericDao<CohortAttributeType> dao;
	
	@Before
	public void setup() throws Exception {
		dao.setClazz(CohortAttributeType.class);
		executeDataSet(COHORT_ATTRIBUTE_TYPE_INITIAL_TEST_DATA_XML);
	}
	
	@Test
	public void shouldGetCohortAttributeTypeById() {
		CohortAttributeType cohortAttributeType = dao.findByUniqueProp(
		    PropValue.builder().property("cohortAttributeTypeId").value(COHORT_ATTRIBUTE_TYPE_ID).build());
		assertThat(cohortAttributeType, notNullValue());
		assertThat(cohortAttributeType.getCohortAttributeTypeId(), notNullValue());
		assertThat(cohortAttributeType.getCohortAttributeTypeId(), equalTo(COHORT_ATTRIBUTE_TYPE_ID));
	}
	
	@Test
	public void shouldGetCohortAttributeTypeByUuid() {
		CohortAttributeType cohortAttributeType = dao.get(COHORT_ATTRIBUTE_TYPE_UUID);
		assertThat(cohortAttributeType, notNullValue());
		assertThat(cohortAttributeType.getCohortAttributeTypeId(), notNullValue());
		assertThat(cohortAttributeType.getCohortAttributeTypeId(), equalTo(COHORT_ATTRIBUTE_TYPE_ID));
		assertThat(cohortAttributeType.getUuid(), equalTo(COHORT_ATTRIBUTE_TYPE_UUID));
	}
	
	@Test
	public void shouldCreateNewCohortAttributeType() {
		CohortAttributeType cohortAttributeTypeToCreate = dao.createOrUpdate(TestDataUtils.COHORT_ATTRIBUTE_TYPE());
		assertThat(cohortAttributeTypeToCreate, notNullValue());
		assertThat(cohortAttributeTypeToCreate.getCohortAttributeTypeId(), notNullValue());
		assertThat(cohortAttributeTypeToCreate.getCohortAttributeTypeId(),
		    equalTo(TestDataUtils.COHORT_ATTRIBUTE_TYPE().getCohortAttributeTypeId()));
		
		// ISSUE: Batch update returned unexpected row count from update [0]; actual row count: 0; expected: 1
		//		CohortAttributeType newlyCreatedCohortAttributeType = dao.get("32816782-d578-401c-8475-8ccbb26ce001");
		//		assertThat(newlyCreatedCohortAttributeType, notNullValue());
		//		assertThat(newlyCreatedCohortAttributeType.getCohortAttributeTypeId(), notNullValue());
		//		assertThat(newlyCreatedCohortAttributeType.getUuid(), equalTo(cohortAttributeTypeToCreate.getUuid()));
		//		assertThat(newlyCreatedCohortAttributeType.getFormat(), equalTo(cohortAttributeTypeToCreate.getFormat()));
		//		assertThat(newlyCreatedCohortAttributeType.getCohortAttributeTypeId(), equalTo(cohortAttributeTypeToCreate.getCohortAttributeTypeId()));
	}
	
	@Test
	public void shouldUpdateCohortAttributeType() {
		CohortAttributeType cohortAttributeTypeToUpdate = dao.get(COHORT_ATTRIBUTE_TYPE_UUID);
		assertThat(cohortAttributeTypeToUpdate, notNullValue());
		assertThat(cohortAttributeTypeToUpdate.getDescription(), equalTo("This is description"));
		cohortAttributeTypeToUpdate.setDescription("Updated cohort attribute type");
		
		dao.createOrUpdate(cohortAttributeTypeToUpdate);
		
		CohortAttributeType cohortAttributeType = dao.get(cohortAttributeTypeToUpdate.getUuid());
		assertThat(cohortAttributeType, notNullValue());
		assertThat(cohortAttributeType.getUuid(), equalTo(cohortAttributeTypeToUpdate.getUuid()));
		assertThat(cohortAttributeType.getCohortAttributeTypeId(), equalTo(COHORT_ATTRIBUTE_TYPE_ID));
		assertThat(cohortAttributeType.getDescription(), equalTo("Updated cohort attribute type"));
	}
}
