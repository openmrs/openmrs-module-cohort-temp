package org.openmrs.module.cohort.validator;

import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortProgram;
import org.openmrs.module.cohort.api.CohortService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@Qualifier("addCohortProgramValidator")
public class AddCohortProgramValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(CohortProgram.class);
	}
	
	@Override
	public void validate(Object command, Errors errors) {
		CohortService cohortService = Context.getService(CohortService.class);
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required");
		
		CohortProgram program = (CohortProgram) command;
		
		// TODO change it to find by name and then reject
		for (CohortProgram programs : cohortService.getAllCohortPrograms()) {
			if (program.getName().equals(programs.getName())) {
				errors.rejectValue("name", "An entry with this name already exists");
			}
		}
	}
}
