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
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Setter;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.cohort.CohortMember;
import org.openmrs.module.cohort.CohortMemberAttribute;
import org.openmrs.module.cohort.CohortMemberAttributeType;
import org.openmrs.module.cohort.api.CohortMemberService;
import org.openmrs.module.cohort.api.dao.IGenericDao;
import org.openmrs.module.cohort.api.dao.PropValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Setter(AccessLevel.PACKAGE)
@Component(value = "cohort.cohortMemberService")
public class CohortMemberServiceImpl extends BaseOpenmrsService implements CohortMemberService {
	
	private final IGenericDao<CohortMember> cohortMemberDao;
	
	private final IGenericDao<CohortMemberAttributeType> cohortMemberAttributeTypeDao;
	
	private final IGenericDao<CohortMemberAttribute> cohortMemberAttributeDao;
	
	@Autowired
	public CohortMemberServiceImpl(IGenericDao<CohortMember> cohortMemberDao,
	    IGenericDao<CohortMemberAttributeType> cohortMemberAttributeTypeDao,
	    IGenericDao<CohortMemberAttribute> cohortMemberAttributeDao) {
		this.cohortMemberDao = cohortMemberDao;
		this.cohortMemberAttributeTypeDao = cohortMemberAttributeTypeDao;
		this.cohortMemberAttributeDao = cohortMemberAttributeDao;
		
		this.cohortMemberDao.setClazz(CohortMember.class);
		this.cohortMemberAttributeDao.setClazz(CohortMemberAttribute.class);
		this.cohortMemberAttributeTypeDao.setClazz(CohortMemberAttributeType.class);
	}
	
	@Override
	public CohortMember getByUuid(@NotNull String uuid) {
		return cohortMemberDao.get(uuid);
	}
	
	@Override
	public CohortMember getByName(String name) {
		return cohortMemberDao.findByUniqueProp(PropValue.builder().property("name").value(name).build());
	}
	
	@Override
	public Collection<CohortMember> findAll() {
		return cohortMemberDao.findAll();
	}
	
	@Override
	public CohortMember createOrUpdate(CohortMember cohortMember) {
		return cohortMemberDao.createOrUpdate(cohortMember);
	}
	
	@Override
	public CohortMember delete(CohortMember cohortMember, String retireReason) {
		if (cohortMember != null) {
			cohortMember.setVoided(true);
			cohortMember.setVoidReason(retireReason);
			cohortMember.setDateVoided(new Date());
			return cohortMemberDao.createOrUpdate(cohortMember);
		}
		return null;
	}
	
	@Override
	public void purge(CohortMember cohortMember) {
		cohortMemberDao.delete(cohortMember);
	}
	
	@Override
	public CohortMemberAttributeType getAttributeTypeByUuid(String uuid) {
		return cohortMemberAttributeTypeDao.get(uuid);
	}
	
	@Override
	public Collection<CohortMemberAttributeType> findAllAttributeTypes() {
		return cohortMemberAttributeTypeDao.findAll();
	}
	
	@Override
	public CohortMemberAttributeType createAttributeType(CohortMemberAttributeType cohortMemberAttributeType) {
		return cohortMemberAttributeTypeDao.createOrUpdate(cohortMemberAttributeType);
	}
	
	@Override
	public CohortMemberAttributeType voidAttributeType(CohortMemberAttributeType cohortMemberAttributeType,
	        String voidReason) {
		cohortMemberAttributeType.setRetired(true);
		cohortMemberAttributeType.setRetireReason(voidReason);
		return cohortMemberAttributeTypeDao.createOrUpdate(cohortMemberAttributeType);
	}
	
	@Override
	public void purgeAttributeType(CohortMemberAttributeType cohortMemberAttributeType) {
		cohortMemberAttributeTypeDao.delete(cohortMemberAttributeType);
	}
	
	@Override
	public CohortMemberAttribute getAttributeByUuid(@NotNull String uuid) {
		return cohortMemberAttributeDao.get(uuid);
	}
	
	@Override
	public Collection<CohortMemberAttribute> getAttributeByTypeUuid(@NotNull String uuid) {
		return cohortMemberAttributeDao.findBy(
		    PropValue.builder().property("uuid").associationPath(Optional.of("attributeType")).value(uuid).build());
	}
	
	@Override
	public CohortMemberAttribute createAttribute(CohortMemberAttribute cohortMemberAttribute) {
		return cohortMemberAttributeDao.createOrUpdate(cohortMemberAttribute);
	}
	
	@Override
	public CohortMemberAttribute deleteAttribute(CohortMemberAttribute attribute, String voidReason) {
		attribute.setVoided(true);
		attribute.setVoidReason(voidReason);
		return cohortMemberAttributeDao.createOrUpdate(attribute);
	}
	
	@Override
	public void purgeAttribute(CohortMemberAttribute cohortMemberAttribute) {
		cohortMemberAttributeDao.delete(cohortMemberAttribute);
	}
	
	@Override
	public Collection<CohortMember> findCohortMembersByCohortUuid(String cohortUuid) {
		return cohortMemberDao.findBy(
		    PropValue.builder().property("uuid").associationPath(Optional.of("cohort")).value(cohortUuid).build());
	}
	
	@Override
	public Collection<CohortMember> findCohortMembersByPatientUuid(String patientUuid) {
		return cohortMemberDao.findBy(
		    PropValue.builder().property("uuid").associationPath(Optional.of("patient")).value(patientUuid).build());
	}
}
