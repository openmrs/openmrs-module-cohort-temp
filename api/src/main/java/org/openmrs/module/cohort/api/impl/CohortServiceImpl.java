/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cohort.api.impl;

import javax.validation.constraints.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.cohort.*;
import org.openmrs.module.cohort.api.CohortMemberService;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.module.cohort.api.dao.GenericDao;
import org.openmrs.module.cohort.api.dao.search.PropValue;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Setter(AccessLevel.PACKAGE)
public class CohortServiceImpl extends BaseOpenmrsService implements CohortService {
	
	private final GenericDao<CohortM> cohortDao;
	
	private final GenericDao<CohortAttribute> cohortAttributeDao;
	
	private final GenericDao<CohortAttributeType> cohortAttributeTypeDao;
	
	public CohortServiceImpl(GenericDao<CohortM> cohortDao, GenericDao<CohortAttribute> cohortAttributeDao,
	    GenericDao<CohortAttributeType> cohortAttributeTypeDao) {
		this.cohortDao = cohortDao;
		this.cohortAttributeDao = cohortAttributeDao;
		this.cohortAttributeTypeDao = cohortAttributeTypeDao;
	}
	
	@Override
	public CohortM getCohortByUuid(@NotNull String uuid) {
		return cohortDao.get(uuid);
	}
	
	@Override
	public CohortM getCohort(@NotNull String name) {
		return cohortDao.findByUniqueProp(PropValue.builder().property("name").value(name).build());
	}
	
	@Override
	public CohortM getCohort(int id) {
		return cohortDao.get(id);
	}
	
	@Override
	public Collection<CohortM> findCohortByLocationUuid(@NotNull String locationUuid) {
		return cohortDao.findBy(
		    PropValue.builder().property("uuid").associationPath(Optional.of("location")).value(locationUuid).build());
	}
	
	@Override
	public Collection<CohortM> findCohortByPatientUuid(@NotNull String patientUuid) {
		return cohortDao.findBy(
		    PropValue.builder().property("uuid").associationPath(Optional.of("patient")).value(patientUuid).build());
	}
	
	@Override
	public Collection<CohortM> findAll() {
		return cohortDao.findAll();
	}
	
	@Override
	public CohortM saveCohort(@NotNull CohortM cohortM) {
		return cohortDao.createOrUpdate(cohortM);
	}
	
	@Override
	public void voidCohort(CohortM cohort, String reason) {
		if (cohort == null) {
			return;
		}
		cohort.setVoided(true);
		cohort.setVoidReason(reason);

		for (CohortMember cohortMember : cohort.getCohortMembers()) {
			Context.getService(CohortMemberService.class).voidCohortMember(cohortMember, "Cohort voided");
		}
		cohortDao.createOrUpdate(cohort);
	}
	
	@Override
	public void purgeCohort(@NotNull CohortM cohort) {
		cohortDao.delete(cohort);
	}
	
	@Override
	public CohortAttribute getAttributeByUuid(@NotNull String uuid) {
		return cohortAttributeDao.get(uuid);
	}
	
	@Override
	public CohortAttribute saveAttribute(@NotNull CohortAttribute attribute) {
		return cohortAttributeDao.createOrUpdate(attribute);
	}
	
	@Override
	public Collection<CohortAttribute> findAttributesByCohortUuid(String cohortUuid) {
		return cohortAttributeDao.findBy(
		    PropValue.builder().associationPath(Optional.of("cohort")).property("uuid").value(cohortUuid).build());
	}
	
	@Override
	public Collection<CohortAttribute> findAttributesByTypeUuid(String attributeTypeUuid) {
		return cohortAttributeDao.findBy(PropValue.builder().associationPath(Optional.of("attributeType")).property("uuid")
		        .value(attributeTypeUuid).build());
	}
	
	@Override
	public Collection<CohortAttribute> findAttributesByTypeName(String attributeTypeName) {
		return cohortAttributeDao.findBy(PropValue.builder().associationPath(Optional.of("attributeType")).property("name")
		        .value(attributeTypeName).build());
	}
	
	@Override
	public void voidCohortAttribute(@NotNull CohortAttribute attribute, String retiredReason) {
		if (attribute == null) {
			return;
		}
		
		cohortAttributeDao.createOrUpdate(attribute);
	}
	
	@Override
	public void purgeCohortAttribute(@NotNull CohortAttribute attribute) {
		cohortAttributeDao.delete(attribute);
	}
	
	@Override
	public CohortAttributeType getAttributeTypeByUuid(@NotNull String uuid) {
		return cohortAttributeTypeDao.get(uuid);
	}
	
	@Override
	public CohortAttributeType getAttributeTypeByName(String name) {
		return cohortAttributeTypeDao.findByUniqueProp(PropValue.builder().property("name").value(name).build());
	}
	
	@Override
	public Collection<CohortAttributeType> findAllAttributeTypes() {
		return cohortAttributeTypeDao.findAll();
	}
	
	@Override
	public CohortAttributeType saveAttributeType(@NotNull CohortAttributeType attributeType) {
		return cohortAttributeTypeDao.createOrUpdate(attributeType);
	}
	
	@Override
	public void voidAttributeType(@NotNull CohortAttributeType attributeType, String retiredReason) {
		if (attributeType == null) {
			return;
		}
		
		cohortAttributeTypeDao.createOrUpdate(attributeType);
	}
	
	@Override
	public void purgeAttributeType(@NotNull CohortAttributeType attributeType) {
		cohortAttributeTypeDao.delete(attributeType);
	}
	
	@Override
	public List<CohortM> findMatchingCohorts(String nameMatching, Map<String, String> attributes, CohortType cohortType,
	        boolean includeVoided) {
		return cohortDao.getSearchHandler().findCohorts(nameMatching, attributes, cohortType, includeVoided);
	}
}
