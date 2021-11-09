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

import org.openmrs.module.cohort.CohortAttributeType;
import org.openmrs.module.cohort.api.CohortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@Qualifier("cohort.cohortAttributeTypeValidator")
public class CohortAttributeTypeValidator implements Validator {
	
	private final CohortService cohortService;
	
	@Autowired
	public CohortAttributeTypeValidator(@Qualifier("cohort.cohortService") CohortService cohortService) {
		this.cohortService = cohortService;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(CohortAttributeType.class);
	}
	
	@Override
	public void validate(Object command, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "format", "required");
		
		CohortAttributeType cohortAttributeType = (CohortAttributeType) command;
		CohortAttributeType attributeType = cohortService.getAttributeTypeByName(cohortAttributeType.getName());
		
		if (attributeType != null) {
			errors.rejectValue("name", " A cohort attribute type with the same name already exists");
		}
	}
}
