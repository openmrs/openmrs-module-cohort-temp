/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cohort.validators;

import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.api.CohortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@Qualifier("cohort.cohortMValidator")
public class CohortMValidator implements Validator {
	
	private final CohortService cohortService;
	
	@Autowired
	public CohortMValidator(CohortService cohortService) {
		this.cohortService = cohortService;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(CohortM.class);
	}
	
	@Override
	public void validate(Object command, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Cohort Name Required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "Cohort Description Required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "Cohort Start Date Required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endDate", "Cohort End Date Required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "definitionHandlerClassname",
		    "Cohort definitionHandlerClassname is Required");
		
		CohortM cohort = (CohortM) command;
		
		//EndDate should less than startDate
		if (cohort.getStartDate().compareTo(cohort.getEndDate()) > 0) {
			errors.rejectValue("startDate", "Start date should be less than End date");
		}
		
		//Cohort should have a unique name
		CohortM cohortByName = cohortService.findByName(cohort.getName());
		if (cohortByName != null) {
			errors.rejectValue("name", "A cohort with this name already exists");
		}
	}
}
