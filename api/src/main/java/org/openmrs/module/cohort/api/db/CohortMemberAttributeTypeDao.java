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

import org.openmrs.annotation.Authorized;
import org.openmrs.module.cohort.CohortMemberAttributeType;
import org.openmrs.util.PrivilegeConstants;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface CohortMemberAttributeTypeDao {

    @Authorized(PrivilegeConstants.GET_PATIENT_COHORTS)
    CohortMemberAttributeType getCohortMemberAttributeTypeByUuid(@NotNull String uuid);

    @Authorized(PrivilegeConstants.GET_PATIENT_COHORTS)
    List<CohortMemberAttributeType> getCohortMemberAttributeTypes();

    @Authorized(PrivilegeConstants.ADD_COHORTS)
    CohortMemberAttributeType createCohortMemberAttributeType(@NotNull CohortMemberAttributeType cohortMemberAttributeType);

    @Authorized(PrivilegeConstants.DELETE_COHORTS)
    CohortMemberAttributeType deleteCohortMemberAttributeType (@NotNull String uuid);

    @Authorized(PrivilegeConstants.PURGE_COHORTS)
    void purgeCohortMemberAttribute (CohortMemberAttributeType cohortMemberAttributeType);
}
