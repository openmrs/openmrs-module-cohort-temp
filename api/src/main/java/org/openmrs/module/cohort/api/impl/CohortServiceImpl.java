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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.cohort.CohortAttribute;
import org.openmrs.module.cohort.CohortAttributeType;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortType;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.module.cohort.api.dao.IGenericDao;
import org.openmrs.module.cohort.api.dao.PropValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Setter(AccessLevel.PACKAGE)
@Component(value = "cohort.cohortService")
public class CohortServiceImpl extends BaseOpenmrsService implements CohortService {
	
	private final IGenericDao<CohortM> cohortDao;
	
	private final IGenericDao<CohortAttribute> cohortAttributeDao;
	
	private final IGenericDao<CohortAttributeType> cohortAttributeTypeDao;
	
	@Autowired
	public CohortServiceImpl(IGenericDao<CohortM> cohortDao, IGenericDao<CohortAttribute> cohortAttributeDao,
	    IGenericDao<CohortAttributeType> cohortAttributeTypeDao) {
		this.cohortDao = cohortDao;
		this.cohortDao.setClazz(CohortM.class);
		this.cohortAttributeDao = cohortAttributeDao;
		this.cohortAttributeDao.setClazz(CohortAttribute.class);
		this.cohortAttributeTypeDao = cohortAttributeTypeDao;
		this.cohortAttributeTypeDao.setClazz(CohortAttributeType.class);
	}
	
	@Override
	public CohortM getByUuid(@NotNull String uuid) {
		return cohortDao.get(uuid);
	}
	
	@Override
	public CohortM findByName(@NotNull String name) {
		return cohortDao.findByUniqueProp(PropValue.builder().property("name").value(name).build());
	}
	
	@Override
	public Collection<CohortM> findByLocationUuid(@NotNull String locationUuid) {
		return cohortDao.findBy(
		    PropValue.builder().property("uuid").associationPath(Optional.of("location")).value(locationUuid).build());
	}
	
	@Override
	public Collection<CohortM> findByPatientUuid(@NotNull String patientUuid) {
		return cohortDao.findBy(
		    PropValue.builder().property("uuid").associationPath(Optional.of("patient")).value(patientUuid).build());
	}
	
	@Override
	public Collection<CohortM> findAll() {
		return cohortDao.findAll();
	}
	
	@Override
	public CohortM createOrUpdate(@NotNull CohortM cohortM) {
		return cohortDao.createOrUpdate(cohortM);
	}
	
	@Override
	public CohortM delete(@NotNull CohortM cohort, String reason) {
		cohort.setVoided(true);
		cohort.setVoidReason(reason);
		cohort.setDateVoided(new Date());
		return cohortDao.createOrUpdate(cohort);
	}
	
	@Override
	public void purge(@NotNull CohortM cohort) {
		cohortDao.delete(cohort);
	}
	
	@Override
	public CohortAttribute getAttributeByUuid(@NotNull String uuid) {
		return cohortAttributeDao.get(uuid);
	}
	
	@Override
	public CohortAttribute createAttribute(@NotNull CohortAttribute attribute) {
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
	public CohortAttribute deleteAttribute(@NotNull CohortAttribute attribute, String retiredReason) {
		attribute.setVoided(true);
		attribute.setDateVoided(new Date());
		attribute.setVoidReason(retiredReason);
		return cohortAttributeDao.createOrUpdate(attribute);
	}
	
	@Override
	public void purgeAttribute(@NotNull CohortAttribute attribute) {
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
	public CohortAttributeType createAttributeType(@NotNull CohortAttributeType attributeType) {
		return cohortAttributeTypeDao.createOrUpdate(attributeType);
	}
	
	@Override
	public CohortAttributeType deleteAttributeType(@NotNull CohortAttributeType attributeType, String retiredReason) {
		attributeType.setVoided(true);
		attributeType.setDateVoided(new Date());
		attributeType.setVoidReason(retiredReason);
		return cohortAttributeTypeDao.createOrUpdate(attributeType);
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
