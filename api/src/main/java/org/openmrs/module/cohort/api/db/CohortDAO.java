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

import java.util.List;
import java.util.Map;

import org.openmrs.module.cohort.CohortAttribute;
import org.openmrs.module.cohort.CohortAttributeType;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortMember;
import org.openmrs.module.cohort.CohortType;
import org.openmrs.module.cohort.api.CohortService;

/**
 * Database methods for {@link CohortService}.
 */
public interface CohortDAO {
	
	CohortAttribute getCohortAttributeById(Integer id);
	
	CohortAttribute getCohortAttributeByUuid(String uuid);
	
	CohortAttribute saveCohortAttributes(CohortAttribute att);
	
	List<CohortAttribute> findCohortAttributes(Integer cohortId, Integer attributeTypeId);
	
	CohortAttributeType getCohortAttributeTypeById(Integer id);
	
	CohortAttributeType getCohortAttributeTypeByUuid(String uuid);
	
	CohortAttributeType getCohortAttributeTypeUuid(String uuid);
	
	CohortAttributeType getCohortAttributes(Integer cohort_attribute_type_id);
	
	CohortAttributeType saveCohortAttributes(CohortAttributeType attributes);
	
	CohortM getCohortByName(String name);
	
	CohortM getCohortMById(Integer id);
	
	CohortM getCohortMByUuid(String uuid);
	
	CohortM getCohortUuid(String uuid);
	
	CohortM saveCohort(CohortM cohort);
	
	CohortM getCohort(Integer locationId, Integer ProgramId, Integer TypeId);
	
	CohortMember getCohortMemUuid(String uuid);
	
	CohortMember saveCPatient(CohortMember cohort);
	
	CohortType getCohortType(Integer id);
	
	CohortType getCohortTypeById(Integer id);
	
	CohortType getCohortTypeByUuid(String uuid);
	
	CohortType getCohortTypeByName(String name);
	
	CohortType saveCohortType(CohortType cohorttype);
	
	CohortAttribute getCohortAttribute(Integer id);
	
	List<CohortAttribute> getCohortAttributesByAttributeType(Integer attributeTypeId);
	
	List<CohortAttribute> findCohortAttribute(String name);
	
	CohortAttributeType findCohortAttributeType(Integer id);
	
	List<CohortAttributeType> getAllCohortAttributes();
	
	CohortAttributeType findCohortAttributes(String attribute_type_name);
	
	List<CohortM> findCohorts();
	
	List<CohortM> findCohorts(String nameMatching, Map<String, String> attributes, CohortType type);
	
	CohortM getCohort(Integer id);
	
	List<CohortM> getCohortsByLocationId(Integer id);
	
	List<CohortMember> findCohortMember(String name);
	
	List<CohortMember> findCohortMembersByCohortId(Integer cohortId);
	
	List<CohortMember> getCohortMembersByCohortId(Integer id);
	
	CohortMember getCohortMember(Integer id);
	
	List<CohortMember> getCohortMembersByCohortRoleId(Integer id);
	
	List<CohortMember> getCohortMembersByPatientId(int patientId);
	
	List<CohortType> getAllCohortTypes();
	
	Long getCount(String name);
	
	void purgeCohort(CohortM cohort);
	
	void purgeCohortAtt(CohortAttribute att);
	
	void purgeCohortAttributes(CohortAttributeType attributes);
	
	void purgeCohortType(CohortType cohort);
}
