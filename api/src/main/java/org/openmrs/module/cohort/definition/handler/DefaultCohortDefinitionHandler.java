/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cohort.definition.handler;

import lombok.extern.slf4j.Slf4j;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortMember;
import org.openmrs.module.cohort.definition.ManualCohortDefinitionHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultCohortDefinitionHandler implements ManualCohortDefinitionHandler {
	
	@Override
	public void update(CohortM cohort) {
		//Update cohort members
		//Do nothing for now
	}
	
	@Override
	public void addMember(CohortM cohort, CohortMember cohortMember) {
		cohortMember.setCohort(cohort);
		cohort.getCohortMembers().add(cohortMember);
	}
	
	@Override
	public void removeMember(CohortM cohort, CohortMember cohortMember) {
		cohortMember.setCohort(cohort);
		cohort.getCohortMembers().remove(cohortMember);
	}
}
