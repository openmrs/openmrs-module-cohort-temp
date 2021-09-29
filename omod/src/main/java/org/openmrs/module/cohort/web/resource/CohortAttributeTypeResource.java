/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cohort.web.resource;

import java.util.ArrayList;
import java.util.Collection;

import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortAttributeType;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@SuppressWarnings("unused")
@Resource(name = RestConstants.VERSION_1 + CohortMainRestController.COHORT_NAMESPACE
        + "/cohortattributetype", supportedClass = CohortAttributeType.class, supportedOpenmrsVersions = { "1.8 - 2.*" })
public class CohortAttributeTypeResource extends DataDelegatingCrudResource<CohortAttributeType> {
	
	private final CohortService cohortService;
	
	public CohortAttributeTypeResource() {
		this.cohortService = Context.getRegisteredComponent("cohort.cohortService", CohortService.class);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		if (Context.isAuthenticated()) {
			if (rep instanceof DefaultRepresentation) {
				final DelegatingResourceDescription description = new DelegatingResourceDescription();
				description.addProperty("name");
				description.addProperty("description");
				description.addProperty("format");
				description.addProperty("uuid");
				description.addSelfLink();
				description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
				return description;
			} else if (rep instanceof FullRepresentation) {
				final DelegatingResourceDescription description = new DelegatingResourceDescription();
				description.addProperty("name");
				description.addProperty("description");
				description.addProperty("format");
				description.addProperty("uuid");
				description.addProperty("auditInfo");
				description.addSelfLink();
				return description;
			}
		}
		return null;
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addRequiredProperty("name");
		description.addProperty("description");
		description.addRequiredProperty("format");
		return description;
	}
	
	@Override
	public CohortAttributeType save(CohortAttributeType cohortAttributeType) {
		return cohortService.createAttributeType(cohortAttributeType);
	}
	
	@Override
	protected void delete(CohortAttributeType cohortAttributeType, String reason, RequestContext context)
	        throws ResponseException {
		cohortService.deleteAttributeType(cohortAttributeType, reason);
	}
	
	@Override
	public void purge(CohortAttributeType cohortAttributeType, RequestContext context) throws ResponseException {
		cohortService.purgeAttributeType(cohortAttributeType);
	}
	
	@Override
	public CohortAttributeType newDelegate() {
		return new CohortAttributeType();
	}
	
	@Override
	public CohortAttributeType getByUniqueId(String uuid) {
		return cohortService.getAttributeTypeByUuid(uuid);
	}
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		Collection<CohortAttributeType> allCohortAttributeTypes = cohortService.findAllAttributeTypes();
		return new NeedsPaging<>(new ArrayList<>(allCohortAttributeTypes), context);
	}
}
