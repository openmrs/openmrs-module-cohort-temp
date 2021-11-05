/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cohort.api.dao.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortMember;
import org.openmrs.module.cohort.api.SpringTestConfiguration;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = SpringTestConfiguration.class, inheritLocations = false)
public class CohortSearchHandlerTest extends BaseModuleContextSensitiveTest {
	
	private static final String[] COHORT_INITIAL_TEST_DATA_XML = {
	        "org/openmrs/module/cohort/api/hibernate/db/CohortDaoTest_initialTestData.xml",
	        "org/openmrs/module/cohort/api/hibernate/db/CohortMemberDaoTest_initialTestData.xml" };
	
	private static final String PATIENT_UUID = "61b38324-e2fd-4feb-95b7-9e9a2a4400df";
	
	private static final String BAD_PATIENT_UUID = "xx78-bad-patient-uuid-9349hj34l";
	
	@Autowired
	private CohortSearchHandler searchHandler;
	
	@Before
	public void setup() throws Exception {
		for (String data : COHORT_INITIAL_TEST_DATA_XML) {
			executeDataSet(data);
		}
	}
	
	@Test
	public void findCohortsByPatientUuid_shouldReturnCollectionOfCohorts() {
		Collection<CohortM> cohorts = searchHandler.findCohortsByMemberships(PATIENT_UUID, null, null, false);
		
		assertThat(cohorts, notNullValue());
		assertThat(cohorts, hasSize(1));
		for (CohortM cohort : cohorts) {
			for (CohortMember cohortMember : cohort.getCohortMembers()) {
				assertThat(cohortMember.getPatient().getUuid(), is(PATIENT_UUID));
			}
		}
	}
	
	@Test
	public void findCohortsByBadPatientUuid_shouldReturnEmptyCollection() {
		Collection<CohortM> cohorts = searchHandler.findCohortsByMemberships(BAD_PATIENT_UUID, null, null, false);
		
		assertThat(cohorts, notNullValue());
		assertThat(cohorts.isEmpty(), is(true));
	}
	
}
