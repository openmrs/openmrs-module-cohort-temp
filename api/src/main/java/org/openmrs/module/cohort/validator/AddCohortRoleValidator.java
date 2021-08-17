package org.openmrs.module.cohort.validator;

import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortRole;
import org.openmrs.module.cohort.api.CohortService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@Qualifier("addCohortRoleValidator")
public class AddCohortRoleValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(CohortRole.class);
	}
	
	@Override
	public void validate(Object command, Errors errors) {
		CohortService cohortService = Context.getService(CohortService.class);
		
		CohortRole currentRole = (CohortRole) command;
		CohortRole role = cohortService.getCohortRoleByName(currentRole.getName());
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
		
		if (role != null) {
			errors.rejectValue("name", "An entry with this name already exists");
		}
	}
}
