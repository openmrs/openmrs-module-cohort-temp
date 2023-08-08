package org.openmrs.module.cohort.api;

import javax.validation.constraints.NotNull;

import java.util.Collection;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.cohort.CohortType;

public interface CohortTypeService extends OpenmrsService {
	
	CohortType getCohortTypeByUuid(@NotNull String uuid);
	
	CohortType getCohortTypeByUuid(@NotNull String uuid, boolean includeVoided);
	
	CohortType getCohortTypeByName(@NotNull String name);
	
	CohortType getCohortTypeByName(@NotNull String name, boolean includeVoided);
	
	Collection<CohortType> findAllCohortTypes();
	
	CohortType saveCohortType(@NotNull CohortType cohortType);
	
	void voidCohortType(@NotNull CohortType cohortType, String voidedReason);
	
	void purgeCohortType(@NotNull CohortType cohortType);
	
}
