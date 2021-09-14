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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.cohort.CohortAttribute;
import org.openmrs.module.cohort.CohortAttributeType;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortMember;
import org.openmrs.module.cohort.CohortType;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.module.cohort.api.db.CohortDAO;

/**
 * It is a default implementation of {@link CohortService}.
 */
public class CohortServiceImpl extends BaseOpenmrsService implements CohortService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private CohortDAO dao;
	
	public CohortDAO getDao() {
		return dao;
	}
	
	/**
	 * @param dao the dao to set
	 */
	public void setDao(CohortDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public CohortType getCohortTypeById(Integer cohort_type_id) {
		return dao.getCohortType(cohort_type_id);
	}
	
	@Override
	public CohortMember saveCohortMember(CohortMember cohort) {
		return dao.saveCPatient(cohort);
	}
	
	@Override
	public CohortAttributeType saveCohort(CohortAttributeType a) {
		return dao.saveCohortAttributes(a);
	}
	
	@Override
	public CohortType saveCohort(CohortType cohort) {
		return dao.saveCohortType(cohort);
	}
	
	@Override
	public void purgeCohortAttributes(CohortAttributeType attributes) {
		dao.purgeCohortAttributes(attributes);
	}
	
	@Override
	public void purgeCohortAtt(CohortAttribute att) {
		dao.purgeCohortAtt(att);
	}
	
	@Override
	public List<CohortType> getAllCohortTypes() {
		return dao.getAllCohortTypes();
	}
	
	@Override
	public List<CohortAttributeType> getAllCohortAttributeTypes() {
		return dao.getAllCohortAttributes();
	}
	
	@Override
	public CohortAttributeType getCohortAttributeTypeByName(String attribute_type_name) {
		return dao.findCohortAttributes(attribute_type_name);
	}
	
	@Override
	public CohortM saveCohort(CohortM cohort) {
		return dao.saveCohort(cohort);
	}
	
	@Override
	public void purgeCohort(CohortM cohort) {
		dao.purgeCohort(cohort);
	}
	
	@Override
	public List<CohortM> getAllCohorts() {
		return dao.findCohorts();
	}
	
	@Override
	public List<CohortM> findCohortsMatching(String nameMatching, Map<String, String> attributes, CohortType cohortType) {
		return dao.findCohorts(nameMatching, attributes, cohortType);
	}
	
	@Override
	public CohortAttribute saveCohortAttribute(CohortAttribute att) {
		return dao.saveCohortAttributes(att);
	}
	
	@Override
	public void purgeCohortType(CohortType type) {
		dao.purgeCohortType(type);
	}
	
	@Override
	public CohortM getCohortById(Integer id) {
		return dao.getCohortMById(id);
	}
	
	@Override
	public CohortM getCohortByUuid(String uuid) {
		return dao.getCohortUuid(uuid);
	}
	
	@Override
	public CohortAttribute getCohortAttributeByUuid(String uuid) {
		return dao.getCohortAttributeByUuid(uuid);
	}
	
	@Override
	public CohortMember getCohortMemberByUuid(String uuid) {
		return dao.getCohortMemUuid(uuid);
	}
	
	@Override
	public CohortType getCohortTypeByUuid(String uuid) {
		return dao.getCohortTypeByUuid(uuid);
	}
	
	@Override
	public CohortAttributeType getCohortAttributeTypeByUuid(String uuid) {
		return dao.getCohortAttributeTypeUuid(uuid);
	}
	
	@Override
	public CohortAttribute getCohortAttributeById(Integer id) {
		return dao.getCohortAttribute(id);
	}
	
	@Override
	public CohortAttributeType getCohortAttributeType(Integer id) {
		return dao.findCohortAttributeType(id);
	}
	
	private Collection<Provider> usersToProviders(Collection<User> users) {
		if (users == null) {
			return null;
		}
		ProviderService providerService = Context.getProviderService();
		Collection<Provider> ret = new HashSet<>();
		for (User u : users) {
			ret.addAll(providerService.getProvidersByPerson(u.getPerson()));
		}
		return ret;
	}
	
	@Override
	public Long getCount(String name) {
		return dao.getCount(name);
	}
	
	@Override
	public List<CohortMember> findCohortMemberByName(String name) {
		return dao.findCohortMember(name);
	}
	
	@Override
	public List<CohortMember> findCohortMembersByCohort(Integer cohortId) {
		return dao.findCohortMembersByCohortId(cohortId);
	}
	
	@Override
	public CohortM getCohort(Integer locationId, Integer programId, Integer typeId) {
		return dao.getCohort(locationId, programId, typeId);
	}
	
	@Override
	public List<CohortAttribute> findCohortAttributes(Integer cohortId, Integer attributeTypeId) {
		return dao.findCohortAttributes(cohortId, attributeTypeId);
	}
	
	@Override
	public CohortType getCohortTypeByName(String name) {
		return dao.getCohortTypeByName(name);
	}
	
	@Override
	public CohortM getCohortByName(String name) {
		return dao.getCohortByName(name);
	}
	
	public List<CohortMember> findCohortMembersByPatient(int patientId) {
		return dao.getCohortMembersByPatientId(patientId);
	}
	
	@Override
	public List<CohortAttribute> getCohortAttributesByAttributeType(Integer attributeTypeId) {
		return dao.getCohortAttributesByAttributeType(attributeTypeId);
	}
	
	@Override
	public List<CohortM> getCohortsByLocationId(int locationId) {
		return dao.getCohortsByLocationId(locationId);
	}
}
