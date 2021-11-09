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
	
	CohortM getCohort(@NotNull String name);
	
	CohortM getCohort(@NotNull int id);
	
	CohortM getCohortByUuid(@NotNull String uuid);
	
	Collection<CohortM> findCohortByLocationUuid(@NotNull String locationUuid);
	
	Collection<CohortM> findCohortByPatientUuid(@NotNull String patientUuid);
	
	Collection<CohortM> findAll();
	
	CohortM saveCohort(@NotNull CohortM cohortType);
	
	void voidCohort(@NotNull CohortM cohort, String reason);
	
	void purgeCohort(@NotNull CohortM cohortType);
	
	CohortAttribute getAttributeByUuid(@NotNull String uuid);
	
	CohortAttribute saveAttribute(@NotNull CohortAttribute attribute);
	
	Collection<CohortAttribute> findAttributesByCohortUuid(@NotNull String cohortUuid);
	
	Collection<CohortAttribute> findAttributesByTypeUuid(@NotNull String attributeTypeUuid);
	
	Collection<CohortAttribute> findAttributesByTypeName(@NotNull String attributeTypeName);
	
	void voidCohortAttribute(@NotNull CohortAttribute attribute, String retiredReason);
	
	void purgeCohortAttribute(@NotNull CohortAttribute attribute);
	
	CohortAttributeType getAttributeTypeByUuid(@NotNull String uuid);
	
	CohortAttributeType getAttributeTypeByName(@NotNull String name);
	
	Collection<CohortAttributeType> findAllAttributeTypes();
	
	CohortAttributeType saveAttributeType(@NotNull CohortAttributeType attributeType);
	
	void voidAttributeType(@NotNull CohortAttributeType attributeType, String retiredReason);
	
	void purgeAttributeType(@NotNull CohortAttributeType attributeType);
	
	//Search
	List<CohortM> findMatchingCohorts(String nameMatching, Map<String, String> attributes, CohortType cohortType,
	        boolean includeVoided);
}
