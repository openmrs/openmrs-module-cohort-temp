/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cohort.api;

import javax.validation.constraints.NotNull;

import java.util.List;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.cohort.CohortMemberAttribute;
import org.openmrs.module.cohort.CohortMemberAttributeType;

public interface CohortMemberService extends OpenmrsService {
	
	CohortMemberAttributeType getCohortMemberAttributeTypeByUuid(@NotNull String uuid);
	
	List<CohortMemberAttributeType> getAllCohortMemberAttributeTypes();
	
	CohortMemberAttributeType saveCohortMemberAttributeType(CohortMemberAttributeType cohortMemberAttributeType);
	
	CohortMemberAttributeType deleteCohortMemberAttributeType(CohortMemberAttributeType cohortMemberAttributeType,
	        String voidReason);
	
	void purgeCohortMemberAttributeType(CohortMemberAttributeType cohortMemberAttributeType);
	
	CohortMemberAttribute getCohortMemberAttributeByUuid(@NotNull String uuid);
	
	List<CohortMemberAttribute> getCohortMemberAttributeByTypeUuid(@NotNull String attributeTypeUuid);
	
	CohortMemberAttribute saveCohortMemberAttribute(CohortMemberAttribute cohortMemberAttribute);
	
	void purgeCohortMemberAttribute(CohortMemberAttribute cohortMemberAttribute);
}
