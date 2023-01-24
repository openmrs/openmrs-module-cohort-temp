package org.openmrs.module.cohort.api.impl;

import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortType;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.module.cohort.validators.CohortMValidator;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CohortValidatorTest extends BaseModuleContextSensitiveTest {

	@Autowired
	CohortMValidator validator;

	@Test
	public void saveCohort_shouldFailValidationOnExistingName() {
		CohortM cohort = new CohortM();
		cohort.setCohortType(new CohortType());
		cohort.setName("Test Cohort");
		Context.getService(CohortService.class).saveCohort(cohort);

		CohortM invalidCohort = new CohortM();
		invalidCohort.setCohortType(new CohortType());
		invalidCohort.setName("Test Cohort");
		Errors errors = new BeanPropertyBindingResult(invalidCohort, "invalidCohort");
		validator.validate(invalidCohort, errors);

		assertThat(errors.getErrorCount(), equalTo(1));
	}

	@Test
	public void saveCohort_shouldPassValidationOnUpdatingWhileKeepingName() {
		CohortM cohort = new CohortM();
		cohort.setName("Test Cohort");
		cohort.setCohortType(new CohortType());
		Context.getService(CohortService.class).saveCohort(cohort);
		cohort.setDescription("Updated Cohort description");
		Errors errors = new BeanPropertyBindingResult(cohort, "invalid cohort");
		validator.validate(cohort, errors);

		assertThat(errors.getErrorCount(), equalTo(0));
	}
}
