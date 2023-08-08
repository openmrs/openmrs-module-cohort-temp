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

import java.util.Collection;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.cohort.CohortMember;
import org.openmrs.module.cohort.CohortMemberAttribute;
import org.openmrs.module.cohort.CohortMemberAttributeType;

public interface CohortMemberService extends OpenmrsService {
	
	CohortMember getCohortMemberByUuid(@NotNull String uuid);
	
	CohortMember getCohortMemberByName(@NotNull String name);
	
	Collection<CohortMember> findAllCohortMembers();
	
	CohortMember saveCohortMember(@NotNull CohortMember cohortMember);
	
	void voidCohortMember(@NotNull CohortMember cohortMember, String voidReason);
	
	void purgeCohortMember(@NotNull CohortMember cohortMember);
	
	CohortMemberAttributeType getCohortMemberAttributeTypeByUuid(@NotNull String uuid);
	
	Collection<CohortMemberAttributeType> findAllCohortMemberAttributeTypes();
	
	CohortMemberAttribute saveCohortMemberAttribute(CohortMemberAttribute cohortMemberAttributeType);
	
	void voidCohortMemberAttribute(CohortMemberAttribute cohortMemberAttribute, String voidReason);
	
	void purgeCohortMemberAttribute(CohortMemberAttribute cohortMemberAttribute);
	
	CohortMemberAttribute getCohortMemberAttributeByUuid(@NotNull String uuid);
	
	Collection<CohortMemberAttribute> getCohortMemberAttributesByTypeUuid(@NotNull String attributeTypeUuid);
	
	CohortMemberAttributeType saveCohortMemberAttributeType(CohortMemberAttributeType cohortMemberAttributeType);
	
	void voidCohortMemberAttributeType(CohortMemberAttributeType cohortMemberAttributeType, String voidReason);
	
	void purgeCohortMemberAttributeType(CohortMemberAttributeType cohortMemberAttributeType);
	
	//Search methods
	Collection<CohortMember> findCohortMembersByCohortUuid(@NotNull String cohortUuid);
	
	Collection<CohortMember> findCohortMembersByPatientUuid(@NotNull String patientUuid);
	
	Collection<CohortMember> findCohortMembersByPatientName(@NotNull String patientName);
	
	Collection<CohortMember> findCohortMembersByCohortAndPatientName(@NotNull String cohortUuid,
	        @NotNull String patientName);
}
