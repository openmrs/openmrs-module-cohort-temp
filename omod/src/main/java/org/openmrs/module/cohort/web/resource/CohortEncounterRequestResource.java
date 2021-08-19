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

import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortEncounter;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.module.cohort.rest.v1_0.resource.CohortRest;
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

@Resource(name = RestConstants.VERSION_1 + CohortRest.COHORT_NAMESPACE
        + "/cohortencounter", supportedClass = CohortEncounter.class, supportedOpenmrsVersions = { "1.8 - 2.*" })
public class CohortEncounterRequestResource extends DataDelegatingCrudResource<CohortEncounter> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		
		DelegatingResourceDescription description = null;
		
		if (Context.isAuthenticated()) {
			description = new DelegatingResourceDescription();
			if (rep instanceof DefaultRepresentation) {
				description.addProperty("cohort");
				description.addProperty("encounterType", Representation.REF);
				description.addProperty("location", Representation.REF);
				description.addProperty("form", Representation.REF);
				description.addProperty("visit");
				description.addProperty("encounterDatetime");
				description.addProperty("encounterProviders");
				description.addProperty("obs");
				description.addProperty("uuid");
				description.addSelfLink();
			} else if (rep instanceof FullRepresentation) {
				description.addProperty("cohort");
				description.addProperty("encounterType");
				description.addProperty("location");
				description.addProperty("form");
				description.addProperty("visit");
				description.addProperty("encounterDatetime");
				description.addProperty("encounterProviders");
				description.addProperty("obs");
				description.addProperty("uuid");
				description.addProperty("auditInfo");
				description.addSelfLink();
			}
		}
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addRequiredProperty("cohort");
		description.addRequiredProperty("encounterType");
		description.addRequiredProperty("location");
		description.addRequiredProperty("form");
		description.addRequiredProperty("visit");
		description.addRequiredProperty("encounterDatetime");
		description.addRequiredProperty("encounterProviders");
		description.addRequiredProperty("obs");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
		return getCreatableProperties();
	}
	
	@Override
	public CohortEncounter newDelegate() {
		return new CohortEncounter();
	}
	
	@Override
	public CohortEncounter save(CohortEncounter cohortEncounter) {
		return Context.getService(CohortService.class).saveCohortEncounter(cohortEncounter);
	}
	
	@Override
	protected void delete(CohortEncounter cohortEncounter, String reason, RequestContext context) throws ResponseException {
		cohortEncounter.setVoided(true);
		cohortEncounter.setVoidReason(reason);
		Context.getService(CohortService.class).saveCohortEncounter(cohortEncounter);
	}
	
	@Override
	public CohortEncounter getByUniqueId(String uuid) {
		return Context.getService(CohortService.class).getCohortEncounterByUuid(uuid);
	}
	
	@Override
	public void purge(CohortEncounter cohortEncounter, RequestContext context) throws ResponseException {
		Context.getService(CohortService.class).purgeCohortEncounter(cohortEncounter);
	}
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		String cohort = context.getParameter("cohort");
		String q = context.getParameter("q");
		
		CohortM cohorto = Context.getService(CohortService.class).getCohortByName(cohort);
		if (cohorto == null) {
			cohorto = Context.getService(CohortService.class).getCohortByUuid(cohort);
		}
		
		if (cohorto == null) {
			throw new IllegalArgumentException("No valid value specified for param cohort");
		}
		List<CohortEncounter> list = Context.getService(CohortService.class).getEncountersByCohort(q, cohorto.getCohortId(),
		    true);
		return new NeedsPaging<CohortEncounter>(list, context);
	}
}
