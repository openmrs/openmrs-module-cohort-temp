package org.openmrs.module.cohort.api.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortMember;
import org.openmrs.module.cohort.CohortType;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class CohortServiceImplContextSensitiveTest extends BaseModuleContextSensitiveTest {
	
	@Test
	public void shouldBeRegisteredAsService() {
		assertThat(Context.getService(CohortService.class), notNullValue());
	}
	
	@Test
	public void saveCohort_shouldSaveCohort() {
		CohortM cohortM = new CohortM();
		cohortM.setCohortType(new CohortType());
		
		CohortM result = Context.getService(CohortService.class).saveCohort(cohortM);
		
		assertThat(result, notNullValue());
		assertThat(result.getId(), notNullValue());
	}
	
	@Test
	public void saveCohort_shouldUpdateCohort() {
		CohortM cohortM = new CohortM();
		cohortM.setName("Test Cohort");
		cohortM.setCohortType(new CohortType());
		
		CohortM savedCohort = Context.getService(CohortService.class).saveCohort(cohortM);
		savedCohort.setName("Updated Test Cohort");
		
		CohortM result = Context.getService(CohortService.class).saveCohort(savedCohort);
		
		assertThat(result, notNullValue());
		assertThat(result.getName(), equalTo("Updated Test Cohort"));
	}

	@Test
	public void saveCohort_shouldUpdateCohortWhileKeepingName() {
		CohortM cohortM = new CohortM();
		cohortM.setName("Test Cohort");
		cohortM.setCohortType(new CohortType());

		CohortM savedCohort = Context.getService(CohortService.class).saveCohort(cohortM);
		savedCohort.setDescription("Updated Test Description");

		CohortM result = Context.getService(CohortService.class).saveCohort(savedCohort);

		assertThat(result, notNullValue());
		assertThat(result.getName(), equalTo("Test Cohort"));
		assertThat(result.getDescription(), equalTo("Updated Test Description"));
	}
	
	@Test
	public void saveCohort_shouldSaveCohortMembers() {
		CohortM cohortM = new CohortM();
		cohortM.setCohortType(new CohortType());
		
		Patient patient = Context.getPatientService().getPatient(7);
		CohortMember cm = new CohortMember(patient);
		
		cohortM.addMemberships(cm);
		
		CohortM result = Context.getService(CohortService.class).saveCohort(cohortM);
		
		assertThat(result, notNullValue());
		assertThat(result.size(), equalTo(1));
		assertThat(result.getCohortMembers().iterator().next(), notNullValue());
		assertThat(result.getCohortMembers().iterator().next().getPatient(), equalTo(patient));
	}
	
	@Test
	public void saveCohort_shouldSaveCohortMembersForExistingCohort() {
		CohortM cohortM = new CohortM();
		cohortM.setCohortType(new CohortType());
		
		CohortM savedCohort = Context.getService(CohortService.class).saveCohort(cohortM);
		
		Patient patient = Context.getPatientService().getPatient(7);
		CohortMember cm = new CohortMember(patient);
		savedCohort.addMemberships(cm);
		
		Context.getService(CohortService.class).saveCohort(savedCohort);
		Context.getRegisteredComponent("sessionFactory", SessionFactory.class).getCurrentSession().flush();
		
		CohortM result = Context.getService(CohortService.class).getCohort(savedCohort.getCohortId());
		
		assertThat(result, notNullValue());
		assertThat(result.size(), equalTo(1));
		assertThat(result.getCohortMembers().iterator().next(), notNullValue());
		assertThat(result.getCohortMembers().iterator().next().getPatient(), equalTo(patient));
	}
	
	@Test
	public void saveCohort_shouldRemoveMembersForExistingCohort() {
		CohortM cohortM = new CohortM();
		cohortM.setCohortType(new CohortType());
		
		Patient patient = Context.getPatientService().getPatient(7);
		CohortMember cm = new CohortMember(patient);
		
		cohortM.addMemberships(cm);
		
		CohortM existingCohort = Context.getService(CohortService.class).saveCohort(cohortM);
		
		existingCohort.removeMemberships(existingCohort.getActiveCohortMembers().iterator().next());
		
		CohortM result = Context.getService(CohortService.class).saveCohort(existingCohort);
		
		assertThat(result, notNullValue());
		assertThat(result.size(), equalTo(0));
	}
	
}
