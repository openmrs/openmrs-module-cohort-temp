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
import org.springframework.transaction.annotation.Transactional;

public interface CohortMemberService extends OpenmrsService {
	
	@Transactional(readOnly = true)
	CohortMember getCohortMemberByUuid(@NotNull String uuid);
	
	@Transactional(readOnly = true)
	CohortMember getCohortMemberByName(@NotNull String name);
	
	@Transactional(readOnly = true)
	Collection<CohortMember> findAllCohortMembers();
	
	@Transactional
	CohortMember saveCohortMember(@NotNull CohortMember cohortMember);
	
	@Transactional
	void voidCohortMember(@NotNull CohortMember cohortMember, String voidReason);
	
	@Transactional
	void purgeCohortMember(@NotNull CohortMember cohortMember);
	
	@Transactional(readOnly = true)
	CohortMemberAttributeType getCohortMemberAttributeTypeByUuid(@NotNull String uuid);
	
	@Transactional(readOnly = true)
	Collection<CohortMemberAttributeType> findAllCohortMemberAttributeTypes();
	
	@Transactional
	CohortMemberAttribute saveCohortMemberAttribute(CohortMemberAttribute cohortMemberAttributeType);
	
	@Transactional
	void voidCohortMemberAttribute(CohortMemberAttribute cohortMemberAttribute, String voidReason);
	
	@Transactional
	void purgeCohortMemberAttribute(CohortMemberAttribute cohortMemberAttribute);
	
	@Transactional(readOnly = true)
	CohortMemberAttribute getCohortMemberAttributeByUuid(@NotNull String uuid);
	
	@Transactional(readOnly = true)
	Collection<CohortMemberAttribute> getCohortMemberAttributesByTypeUuid(@NotNull String attributeTypeUuid);
	
	@Transactional
	CohortMemberAttributeType saveCohortMemberAttributeType(CohortMemberAttributeType cohortMemberAttributeType);
	
	@Transactional
	void voidCohortMemberAttributeType(CohortMemberAttributeType cohortMemberAttributeType, String voidReason);
	
	@Transactional
	void purgeCohortMemberAttributeType(CohortMemberAttributeType cohortMemberAttributeType);
	
	//Search methods
	
	@Transactional(readOnly = true)
	Collection<CohortMember> findCohortMembersByCohortUuid(@NotNull String cohortUuid);
	
	@Transactional(readOnly = true)
	Collection<CohortMember> findCohortMembersByPatientUuid(@NotNull String patientUuid);
	
	@Transactional(readOnly = true)
	Collection<CohortMember> findCohortMembersByPatientName(@NotNull String patientName);
	
	@Transactional(readOnly = true)
	Collection<CohortMember> findCohortMembersByCohortAndPatientName(@NotNull String cohortUuid,
	        @NotNull String patientName);
}
