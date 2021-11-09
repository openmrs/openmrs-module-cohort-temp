package org.openmrs.module.cohort.api;

import javax.validation.constraints.NotNull;

import java.util.Collection;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.cohort.CohortType;

public interface CohortTypeService extends OpenmrsService {
	
	CohortType getByUuid(@NotNull String uuid);
	
	CohortType getByName(@NotNull String name);
	
	Collection<CohortType> findAll();
	
	CohortType saveCohortType(@NotNull CohortType cohortType);
	
	void voidCohortType(@NotNull CohortType cohortType, String voidedReason);
	
	void purgeCohortType(@NotNull CohortType cohortType);
	
}
