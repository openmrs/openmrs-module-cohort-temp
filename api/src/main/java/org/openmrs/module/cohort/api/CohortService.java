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

import java.util.List;
import java.util.Map;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.cohort.CohortAttribute;
import org.openmrs.module.cohort.CohortAttributeType;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortMember;
import org.openmrs.module.cohort.CohortType;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured
 * in moduleApplicationContext.xml. It can be accessed only via Context:<br>
 * <code>
 * Context.getService(cohortService.class).someMethod();
 * </code>
 *
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface CohortService extends OpenmrsService {
	
	CohortM getCohortById(Integer id);
	
	CohortM getCohortByUuid(String uuid);
	
	CohortM getCohortByName(String name);
	
	List<CohortM> getAllCohorts();
	
	List<CohortM> findCohortsMatching(String nameMatching, Map<String, String> attributes, CohortType cohortType);
	
	CohortM saveCohort(CohortM cohort);
	
	void purgeCohort(CohortM cohort);
	
	CohortM getCohort(Integer locationId, Integer programId, Integer typeId);
	
	CohortMember getCohortMemberByUuid(String uuid);
	
	List<CohortMember> findCohortMemberByName(String name);
	
	List<CohortMember> findCohortMembersByCohort(Integer cohortId);
	
	CohortMember saveCohortMember(CohortMember cohortmember);
	
	List<CohortMember> findCohortMembersByPatient(int patientId);
	
	CohortAttributeType getCohortAttributeType(Integer id);
	
	List<CohortAttributeType> getAllCohortAttributeTypes();
	
	CohortAttributeType getCohortAttributeTypeByName(String attribute_type_name);
	
	CohortAttributeType getCohortAttributeTypeByUuid(String uuid);
	
	CohortAttributeType saveCohort(CohortAttributeType a);
	
	void purgeCohortAttributes(CohortAttributeType attributes);
	
	CohortAttribute getCohortAttributeByUuid(String uuid);
	
	CohortAttribute getCohortAttributeById(Integer id);
	
	CohortAttribute saveCohortAttribute(CohortAttribute att);
	
	void purgeCohortAtt(CohortAttribute att);
	
	List<CohortAttribute> findCohortAttributes(Integer cohortId, Integer attributeTypeId);
	
	CohortType getCohortTypeById(Integer id);
	
	CohortType getCohortTypeByUuid(String uuid);
	
	CohortType getCohortTypeByName(String name);
	
	List<CohortType> getAllCohortTypes();
	
	CohortType saveCohort(CohortType cohort);
	
	void purgeCohortType(CohortType type);
	
	Long getCount(String name);
	
	List<CohortAttribute> getCohortAttributesByAttributeType(Integer attributeId);
	
	List<CohortM> getCohortsByLocationId(int locationId);
}
