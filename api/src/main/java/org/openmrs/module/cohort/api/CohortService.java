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
	
	CohortM getByUuid(@NotNull String uuid);
	
	CohortM findByName(@NotNull String name);
	
	Collection<CohortM> findByLocationUuid(@NotNull String locationUuid);
	
	Collection<CohortM> findByPatientUuid(@NotNull String patientUuid);
	
	Collection<CohortM> findAll();
	
	CohortM createOrUpdate(@NotNull CohortM cohortType);
	
	CohortM delete(@NotNull CohortM cohort, String reason);
	
	void purge(@NotNull CohortM cohortType);
	
	CohortAttribute getAttributeByUuid(@NotNull String uuid);
	
	CohortAttribute createAttribute(@NotNull CohortAttribute attribute);
	
	Collection<CohortAttribute> findAttributesByCohortUuid(@NotNull String cohortUuid);
	
	Collection<CohortAttribute> findAttributesByTypeUuid(@NotNull String attributeTypeUuid);
	
	Collection<CohortAttribute> findAttributesByTypeName(@NotNull String attributeTypeName);
	
	CohortAttribute deleteAttribute(@NotNull CohortAttribute attribute, String retiredReason);
	
	void purgeAttribute(@NotNull CohortAttribute attribute);
	
	CohortAttributeType getAttributeTypeByUuid(@NotNull String uuid);
	
	CohortAttributeType getAttributeTypeByName(@NotNull String name);
	
	Collection<CohortAttributeType> findAllAttributeTypes();
	
	CohortAttributeType createAttributeType(@NotNull CohortAttributeType attributeType);
	
	CohortAttributeType deleteAttributeType(@NotNull CohortAttributeType attributeType, String retiredReason);
	
	void purgeAttributeType(@NotNull CohortAttributeType attributeType);
	
	//Search
	List<CohortM> findMatchingCohorts(String nameMatching, Map<String, String> attributes, CohortType cohortType,
	        boolean includeVoided);
	
	Collection<CohortM> findCohortsByPatientUuid(String patientUuid);
}
