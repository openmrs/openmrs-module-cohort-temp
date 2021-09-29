package org.openmrs.module.cohort.api.impl;

import javax.validation.constraints.NotNull;

import java.util.Collection;

import lombok.AccessLevel;
import lombok.Setter;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.cohort.CohortType;
import org.openmrs.module.cohort.api.CohortTypeService;
import org.openmrs.module.cohort.api.dao.IGenericDao;
import org.openmrs.module.cohort.api.dao.PropValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Setter(AccessLevel.PACKAGE)
@Component(value = "cohort.cohortTypeService")
public class CohortTypeServiceImpl extends BaseOpenmrsService implements CohortTypeService {
	
	private final IGenericDao<CohortType> dao;
	
	@Autowired
	public CohortTypeServiceImpl(IGenericDao<CohortType> dao) {
		this.dao = dao;
		this.dao.setClazz(CohortType.class);
	}
	
	@Override
	public CohortType getByUuid(@NotNull String uuid) {
		return dao.get(uuid);
	}
	
	@Override
	public CohortType getByName(@NotNull String name) {
		return dao.findByUniqueProp(PropValue.builder().property("name").value(name).build());
	}
	
	@Override
	public Collection<CohortType> findAll() {
		return dao.findAll();
	}
	
	@Override
	public CohortType createOrUpdate(CohortType cohortType) {
		return dao.createOrUpdate(cohortType);
	}
	
	@Override
	public CohortType delete(@NotNull String uuid, String voidedReason) {
		CohortType cohortTypeToBeVoided = this.getByUuid(uuid);
		if (cohortTypeToBeVoided != null) {
			cohortTypeToBeVoided.setVoided(true);
			cohortTypeToBeVoided.setVoidReason(voidedReason);
			return createOrUpdate(cohortTypeToBeVoided);
		}
		return null;
	}
	
	@Override
	public void purge(CohortType cohortType) {
		dao.delete(cohortType);
	}
}
