/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cohort.web.resource;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortMember;
import org.openmrs.module.cohort.api.CohortMemberService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.representation.CustomRepresentation;
import org.openmrs.module.webservices.rest.web.representation.NamedRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Qualifier;

@RunWith(PowerMockRunner.class)
public class CohortMemberResourceTest extends BaseCohortResourceTest<CohortMember, CohortMemberResource> {
	
	private static final String COHORT_MEMBER_UUID = "6hje567a-fca0-11e5-9e59-08002719a7";
	
	@Mock
	@Qualifier("cohort.cohortMemberService")
	private CohortMemberService cohortMemberService;
	
	CohortMember cohortMember;
	
	@Before
	public void setup() {
		
		cohortMember = new CohortMember();
		cohortMember.setUuid(COHORT_MEMBER_UUID);
		
		//Mocks
		this.prepareMocks();
		when(Context.getService(CohortMemberService.class)).thenReturn(cohortMemberService);
		
		this.setResource(new CohortMemberResource());
		this.setObject(cohortMember);
	}
	
	@Test
	public void shouldGetRegisteredService() {
		assertThat(cohortMemberService, notNullValue());
	}
	
	@Test
	public void shouldReturnDefaultRepresentation() {
		verifyDefaultRepresentation("patient", "startDate", "endDate", "uuid", "attributes", "cohort");
	}
	
	@Test
	public void shouldReturnFullRepresentation() {
		verifyFullRepresentation("patient", "startDate", "endDate", "uuid", "attributes", "cohort", "auditInfo");
	}
	
	@Test
	public void shouldReturnNullForRepresentationOtherThenDefaultOrFull() {
		CustomRepresentation customRepresentation = new CustomRepresentation("some-rep");
		assertThat(getResource().getRepresentationDescription(customRepresentation), is(nullValue()));
		
		NamedRepresentation namedRepresentation = new NamedRepresentation("some-named-rep");
		assertThat(getResource().getRepresentationDescription(namedRepresentation), is(nullValue()));
		
		RefRepresentation refRepresentation = new RefRepresentation();
		assertThat(getResource().getRepresentationDescription(refRepresentation), is(nullValue()));
	}
	
	@Test
	public void shouldGetResourceByUniqueUuid() {
		when(cohortMemberService.getCohortMemberByUuid(COHORT_MEMBER_UUID)).thenReturn(cohortMember);
		
		CohortMember result = getResource().getByUniqueId(COHORT_MEMBER_UUID);
		assertThat(result, notNullValue());
		assertThat(result.getUuid(), is(COHORT_MEMBER_UUID));
	}
	
	@Test
	public void shouldCreateNewResource() {
		CohortM cohort = mock(CohortM.class);
		cohortMember.setCohort(cohort);
		
		when(cohortMemberService.saveCohortMember(getObject())).thenReturn(getObject());
		when(cohort.getVoided()).thenReturn(false);
		
		CohortMember newlyCreatedObject = getResource().save(getObject());
		assertThat(newlyCreatedObject, notNullValue());
		assertThat(newlyCreatedObject.getUuid(), is(COHORT_MEMBER_UUID));
	}
	
	@Test(expected = ResourceDoesNotSupportOperationException.class)
	public void shouldGetAllResources() {
		when(cohortMemberService.findAllCohortMembers()).thenReturn(Collections.singletonList(getObject()));
		
		getResource().getAll(new RequestContext());
	}
	
	@Test
	public void shouldInstantiateNewDelegate() {
		assertThat(getResource().newDelegate(), notNullValue());
	}
	
	@Test
	public void verifyResourceVersion() {
		assertThat(getResource().getResourceVersion(), is("1.8"));
	}
	
	@Test
	public void verifyUri() {
		assertThat(getResource().getUri(getObject()), endsWith("/cohortm/cohortmember/" + COHORT_MEMBER_UUID));
	}
}
