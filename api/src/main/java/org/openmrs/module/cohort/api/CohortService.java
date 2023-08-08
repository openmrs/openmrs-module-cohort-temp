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
import java.util.List;
import java.util.Map;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.cohort.CohortAttribute;
import org.openmrs.module.cohort.CohortAttributeType;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortType;

public interface CohortService extends OpenmrsService {
	
	CohortM getCohortM(@NotNull String name);
	
	CohortM getCohortM(@NotNull int id);
	
	CohortM getCohortMByUuid(@NotNull String uuid);
	
	Collection<CohortM> findCohortMByLocationUuid(@NotNull String locationUuid);
	
	Collection<CohortM> findCohortMByPatientUuid(@NotNull String patientUuid);
	
	Collection<CohortM> findAll();
	
	CohortM saveCohortM(@NotNull CohortM cohortType);
	
	void voidCohortM(@NotNull CohortM cohort, String reason);
	
	void purgeCohortM(@NotNull CohortM cohortType);
	
	CohortAttribute getAttributeByUuid(@NotNull String uuid);
	
	CohortAttribute saveAttribute(@NotNull CohortAttribute attribute);
	
	Collection<CohortAttribute> findCohortAttributesByCohortUuid(@NotNull String cohortUuid);
	
	Collection<CohortAttribute> findCohortAttributesByTypeUuid(@NotNull String attributeTypeUuid);
	
	Collection<CohortAttribute> findCohortAttributesByTypeName(@NotNull String attributeTypeName);
	
	void voidCohortAttribute(@NotNull CohortAttribute attribute, String retiredReason);
	
	void purgeCohortAttribute(@NotNull CohortAttribute attribute);
	
	CohortAttributeType getCohortAttributeTypeByUuid(@NotNull String uuid);
	
	CohortAttributeType getCohortAttributeTypeByName(@NotNull String name);
	
	Collection<CohortAttributeType> findAllCohortAttributeTypes();
	
	CohortAttributeType saveCohortAttributeType(@NotNull CohortAttributeType attributeType);
	
	void voidCohortAttributeType(@NotNull CohortAttributeType attributeType, String retiredReason);
	
	void purgeCohortAttributeType(@NotNull CohortAttributeType attributeType);
	
	//Search
	List<CohortM> findMatchingCohortMs(String nameMatching, Map<String, String> attributes, CohortType cohortType,
	        boolean includeVoided);
}
