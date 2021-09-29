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

import javax.validation.constraints.NotNull;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortAttribute;
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
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@SuppressWarnings("unused")
@Resource(name = RestConstants.VERSION_1 + CohortMainRestController.COHORT_NAMESPACE
        + "/cohortattribute", supportedClass = CohortAttribute.class, supportedOpenmrsVersions = { "1.8 - 2.*" })
public class CohortAttributeResource extends DataDelegatingCrudResource<CohortAttribute> {
	
	private final CohortService cohortService;
	
	public CohortAttributeResource() {
		this.cohortService = Context.getRegisteredComponent("cohort.cohortService", CohortService.class);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		if (Context.isAuthenticated()) {
			if (rep instanceof DefaultRepresentation) {
				final DelegatingResourceDescription description = new DelegatingResourceDescription();
				description.addProperty("cohort", Representation.REF);
				description.addProperty("value");
				description.addProperty("cohortAttributeType");
				description.addProperty("uuid");
				description.addSelfLink();
				description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
				return description;
			} else if (rep instanceof FullRepresentation) {
				final DelegatingResourceDescription description = new DelegatingResourceDescription();
				description.addProperty("cohort", Representation.REF);
				description.addProperty("value");
				description.addProperty("cohortAttributeType");
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
		description.addRequiredProperty("cohort");
		description.addRequiredProperty("value");
		description.addRequiredProperty("cohortAttributeType");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
		return getCreatableProperties();
	}
	
	@Override
	public CohortAttribute save(CohortAttribute cohortAttribute) {
		return cohortService.createAttribute(cohortAttribute);
	}
	
	@Override
	protected void delete(CohortAttribute cohortAttribute, String reason, RequestContext request) throws ResponseException {
		cohortService.deleteAttribute(cohortAttribute, reason);
	}
	
	@Override
	public void purge(CohortAttribute cohortAttribute, RequestContext request) throws ResponseException {
		cohortService.purgeAttribute(cohortAttribute);
	}
	
	@Override
	public CohortAttribute newDelegate() {
		return new CohortAttribute();
	}
	
	@Override
	public CohortAttribute getByUniqueId(@NotNull String uuid) {
		return cohortService.getAttributeByUuid(uuid);
	}
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		String cohortUuid = context.getParameter("cohort");
		String attributeTypeUuid = context.getParameter("attributeType");
		
		if (StringUtils.isNotBlank(cohortUuid) && StringUtils.isNotBlank(attributeTypeUuid)) {
			throw new IllegalArgumentException(
			        "cohort and attributeType Parameters can't both be declared in the url, search by either cohort or attributeType, not both");
		}
		if ((cohortUuid != null)) {
			return new NeedsPaging<>(new ArrayList<>(cohortService.findAttributesByCohortUuid(cohortUuid)), context);
		} else if (attributeTypeUuid != null) {
			return new NeedsPaging<>(new ArrayList<>(cohortService.findAttributesByCohortUuid(attributeTypeUuid)), context);
		} else {
			return new NeedsPaging<>(new ArrayList<>(), context);
		}
	}
}
