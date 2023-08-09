package org.openmrs.module.cohort.api;

import javax.validation.constraints.NotNull;

import java.util.Collection;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.cohort.CohortType;
import org.springframework.transaction.annotation.Transactional;

public interface CohortTypeService extends OpenmrsService {
	
	@Transactional(readOnly = true)
	CohortType getCohortTypeByUuid(@NotNull String uuid);
	
	@Transactional(readOnly = true)
	CohortType getCohortTypeByUuid(@NotNull String uuid, boolean includeVoided);
	
	@Transactional(readOnly = true)
	CohortType getCohortTypeByName(@NotNull String name);
	
	@Transactional(readOnly = true)
	CohortType getCohortTypeByName(@NotNull String name, boolean includeVoided);
	
	@Transactional(readOnly = true)
	Collection<CohortType> findAllCohortTypes();
	
	@Transactional
	CohortType saveCohortType(@NotNull CohortType cohortType);
	
	@Transactional
	void voidCohortType(@NotNull CohortType cohortType, String voidedReason);
	
	@Transactional
	void purgeCohortType(@NotNull CohortType cohortType);
	
}
