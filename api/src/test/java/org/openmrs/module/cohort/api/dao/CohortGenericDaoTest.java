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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;

import java.util.Collection;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortAttribute;
import org.openmrs.module.cohort.CohortAttributeType;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.module.cohort.api.dao.search.PropValue;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CohortGenericDaoTest extends BaseModuleContextSensitiveTest {
	
	private static final String[] COHORT_INITIAL_TEST_DATA_XML = {
	        "org/openmrs/module/cohort/api/hibernate/db/CohortDaoTest_initialTestData.xml",
	        "org/openmrs/module/cohort/api/hibernate/db/CohortMemberDaoTest_initialTestData.xml" };
	
	private static final String COHORT_UUID = "7f9a2479-c14a-4bfc-bcaa-632860258519";
	
	private static final String LOCATION_UUID = "65ab9667-7432-49af-8be8-65a4b58fc78k";
	
	private static final String COHORT_NAME = "COVID-19 patients";
	
	private static final int COHORT_ID = 12;
	
	private static final String COHORT_NAME1 = "cohort name";
	
	private static final String COHORT_DESCRIPTION = "Cohort description";
	
	@Autowired
	@Qualifier("cohortDao")
	private GenericDao<CohortM> dao;

	@Autowired
	@Qualifier("cohort.cohortService")
	private CohortService service;
	
	@Before
	public void setup() throws Exception {
		for (String data : COHORT_INITIAL_TEST_DATA_XML) {
			executeDataSet(data);
		}
	}
	
	@Test
	public void shouldGetCohortByName() {
		CohortM cohort = dao.findByUniqueProp(
		    PropValue.builder().property("name").value(COHORT_NAME).associationPath(Optional.empty()).build());
		assertThat(cohort, notNullValue());
		assertThat(cohort.getName(), notNullValue());
		assertThat(cohort.getName(), equalTo(COHORT_NAME));
	}
	
	@Test
	public void shouldGetCohortMById() {
		CohortM cohort = dao.findByUniqueProp(
		    PropValue.builder().property("cohortId").value(COHORT_ID).associationPath(Optional.empty()).build());
		assertThat(cohort, notNullValue());
		assertThat(cohort.getCohortId(), notNullValue());
		assertThat(cohort.getCohortId(), equalTo(COHORT_ID));
		assertThat(cohort.size(), equalTo(1));
	}
	
	@Test
	public void shouldGetCohortMByUuid() {
		CohortM cohort = dao.get(COHORT_UUID);
		assertThat(cohort, notNullValue());
		assertThat(cohort.getCohortId(), notNullValue());
		assertThat(cohort.getCohortId(), equalTo(COHORT_ID));
		assertThat(cohort.getUuid(), notNullValue());
		assertThat(cohort.getUuid(), equalTo(COHORT_UUID));
		assertThat(cohort.size(), equalTo(1));
	}
	
	@Test
	public void shouldGetCohortUuid() {
		CohortM cohort = dao.get(COHORT_UUID);
		assertThat(cohort, notNullValue());
		assertThat(cohort.getUuid(), notNullValue());
		assertThat(cohort.getUuid(), equalTo(COHORT_UUID));
	}
	
	@Test
	public void shouldCreateNewCohort() {
		CohortM cohortM = new CohortM();
		cohortM.setUuid("24c266ec-ef38-4af5-bf67-608d64a7a4cv");
		cohortM.setCohortId(1);
		cohortM.setName(COHORT_NAME1);
		cohortM.setGroupCohort(false);
		cohortM.setDescription(COHORT_DESCRIPTION);

		CohortAttributeType attributeType = new CohortAttributeType();
		attributeType.setName("testAttributeType");
		attributeType.setMinOccurs(0);
		attributeType.setDatatypeClassname("org.openmrs.customdatatype.datatype.FreeTextDatatype");
		service.saveAttributeType(attributeType);

		CohortAttribute attribute1 = new CohortAttribute();
		attribute1.setAttributeType(attributeType);
		attribute1.setValue("Test group");
		cohortM.addAttribute(attribute1);
		CohortAttribute attribute2 = new CohortAttribute();
		attribute2.setAttributeType(attributeType);
		attribute2.setValue("Control group");
		cohortM.addAttribute(attribute2);
		
		dao.createOrUpdate(cohortM);
		CohortM createdCohort = service.getCohort(1);
		assertThat(createdCohort, notNullValue());
		assertThat(createdCohort.getCohortId(), equalTo(cohortM.getCohortId()));
		assertThat(createdCohort.getName(), is(cohortM.getName()));
		assertThat(createdCohort.getAttributes().size(), is(2));
	}
	
	@Test
	public void findByLocationUuid_shouldReturnNonVoidedCollectionOfCohortsMatchingTheLocation() {
		Collection<CohortM> cohorts = dao.findBy(
		    PropValue.builder().property("uuid").associationPath(Optional.of("location")).value(LOCATION_UUID).build());
		
		assertThat(cohorts, notNullValue());
		assertThat(cohorts, hasSize(1));
		for (CohortM cohort : cohorts) {
			assertThat(cohort.getLocation(), notNullValue());
			assertThat(cohort.getLocation().getUuid(), equalTo(LOCATION_UUID));
		}
	}
	
	@Test
	public void findByLocationUuid_shouldReturnCohortsMatchingGivenLocationIncludingVoidedCohorts() {
		Collection<CohortM> cohorts = dao.findBy(
		    PropValue.builder().property("uuid").associationPath(Optional.of("location")).value(LOCATION_UUID).build(),
		    true);
		
		assertThat(cohorts, notNullValue());
		assertThat(cohorts, hasSize(2));
		
		for (CohortM cohort : cohorts) {
			assertThat(cohort.getLocation(), notNullValue());
			assertThat(cohort.getLocation().getUuid(), equalTo(LOCATION_UUID));
			assertThat(cohort.getLocation().getName(), is("Cohort-21 Location"));
		}
	}
	
	@Test
	public void getByUuid_shouldReturnNullForVoidedOrRetiredCohort() {
		CohortM cohortToVoid = dao.get(COHORT_UUID);
		cohortToVoid.setVoided(true);
		cohortToVoid.setVoidReason("Voided by cohort test");
		dao.createOrUpdate(cohortToVoid);
		
		assertThat(dao.get(COHORT_UUID), nullValue());
	}
	
	@Test
	public void shouldVoidCohortM() {
		CohortM cohortToVoid = dao.get(COHORT_UUID);
		assertThat(false, equalTo(cohortToVoid.getVoided()));
		Context.getService(CohortService.class).voidCohortM(cohortToVoid, "delete cohort");
		assertThat(true, equalTo(cohortToVoid.getVoided()));
		CohortM voidedCohortM = dao.get(COHORT_UUID);
		assertThat(voidedCohortM, nullValue());
	}
}
