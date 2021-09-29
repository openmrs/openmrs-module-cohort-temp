package org.openmrs.module.cohort.api;

import javax.validation.constraints.NotNull;

import java.util.Collection;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.cohort.CohortType;

public interface CohortTypeService extends OpenmrsService {
	
	CohortType getByUuid(@NotNull String uuid);
	
	CohortType getByName(@NotNull String name);
	
	Collection<CohortType> findAll();
	
	CohortType createOrUpdate(@NotNull CohortType cohortType);
	
	CohortType delete(@NotNull String uuid, String voidedReason);
	
	void purge(@NotNull CohortType cohortType);
	
}
