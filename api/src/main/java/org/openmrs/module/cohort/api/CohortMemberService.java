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
	
	CohortMember getByUuid(@NotNull String uuid);
	
	CohortMember getByName(@NotNull String name);
	
	Collection<CohortMember> findAll();
	
	CohortMember createOrUpdate(@NotNull CohortMember cohortMember);
	
	CohortMember delete(@NotNull CohortMember cohortMember, String retireReason);
	
	void purge(@NotNull CohortMember cohortMember);
	
	CohortMemberAttributeType getAttributeTypeByUuid(@NotNull String uuid);
	
	Collection<CohortMemberAttributeType> findAllAttributeTypes();
	
	CohortMemberAttributeType createAttributeType(CohortMemberAttributeType cohortMemberAttributeType);
	
	CohortMemberAttributeType voidAttributeType(CohortMemberAttributeType cohortMemberAttributeType, String voidReason);
	
	void purgeAttributeType(CohortMemberAttributeType cohortMemberAttributeType);
	
	CohortMemberAttribute getAttributeByUuid(@NotNull String uuid);
	
	Collection<CohortMemberAttribute> getAttributeByTypeUuid(@NotNull String attributeTypeUuid);
	
	CohortMemberAttribute createAttribute(CohortMemberAttribute cohortMemberAttribute);
	
	CohortMemberAttribute deleteAttribute(CohortMemberAttribute attribute, String voidReason);
	
	void purgeAttribute(CohortMemberAttribute cohortMemberAttribute);
	
	//Search methods
	Collection<CohortMember> findCohortMembersByCohortUuid(@NotNull String cohortUuid);
	
	Collection<CohortMember> findCohortMembersByPatientUuid(@NotNull String patientUuid);
	
	Collection<CohortMember> findCohortMembersByPatientName(@NotNull String patientName);
	
	Collection<CohortMember> findCohortMembersByCohortAndPatient(@NotNull String cohortUuid, @NotNull String patientName);
}
