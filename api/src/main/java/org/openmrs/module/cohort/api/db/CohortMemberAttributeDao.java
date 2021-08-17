/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cohort.api.db;

import javax.validation.constraints.NotNull;

import java.util.List;

import org.openmrs.annotation.Authorized;
import org.openmrs.module.cohort.CohortMemberAttribute;
import org.openmrs.util.PrivilegeConstants;

public interface CohortMemberAttributeDao {
	
	@Authorized(PrivilegeConstants.GET_PATIENT_COHORTS)
	CohortMemberAttribute getCohortMemberAttributeByUuid(@NotNull String uuid);
	
	@Authorized(PrivilegeConstants.GET_PATIENT_COHORTS)
	List<CohortMemberAttribute> getCohortMemberAttributesByTypeUuid(@NotNull String attributeTypeUuid);
	
	@Authorized(PrivilegeConstants.ADD_COHORTS)
	CohortMemberAttribute saveCohortMemberAttribute(@NotNull CohortMemberAttribute cohortMemberAttribute);
	
	@Authorized(PrivilegeConstants.DELETE_COHORTS)
	CohortMemberAttribute deleteCohortMemberAttribute(@NotNull String uuid);
	
	@Authorized(PrivilegeConstants.PURGE_COHORTS)
	void purgeCohortMemberAttribute(CohortMemberAttribute cohortMemberAttribute);
}
