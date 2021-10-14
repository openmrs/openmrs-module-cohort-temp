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
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortAttribute;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortMember;
import org.openmrs.module.cohort.CohortType;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.module.cohort.api.CohortTypeService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
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
        + "/cohort", supportedClass = CohortM.class, supportedOpenmrsVersions = { "1.8 - 2.*" })
public class CohortResource extends DataDelegatingCrudResource<CohortM> {
	
	private final CohortService cohortService;
	
	private final CohortTypeService cohortTypeService;
	
	public CohortResource() {
		this.cohortService = Context.getRegisteredComponent("cohort.cohortService", CohortService.class);
		this.cohortTypeService = Context.getRegisteredComponent("cohort.cohortTypeService", CohortTypeService.class);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		if (Context.isAuthenticated()) {
			if (rep instanceof DefaultRepresentation) {
				final DelegatingResourceDescription defaultDescription = getSharedDelegatingResourceDescription();
				defaultDescription.addProperty("uuid");
				defaultDescription.addProperty("location", Representation.REF);
				defaultDescription.addProperty("cohortType", Representation.REF);
				defaultDescription.addProperty("voided");
				defaultDescription.addProperty("voidReason");
				defaultDescription.addProperty("display");
				defaultDescription.addSelfLink();
				defaultDescription.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
				return defaultDescription;
			} else if (rep instanceof FullRepresentation) {
				final DelegatingResourceDescription description = getSharedDelegatingResourceDescription();
				description.addProperty("location", Representation.FULL);
				description.addProperty("cohortMembers", Representation.FULL);
				description.addProperty("cohortType", Representation.FULL);
				description.addProperty("voided");
				description.addProperty("voidReason");
				description.addProperty("uuid");
				description.addProperty("auditInfo");
				description.addProperty("display");
				description.addSelfLink();
				return description;
			}
			return null;
		}
		throw new APIAuthenticationException("Unauthorized");
	}
	
	private DelegatingResourceDescription getSharedDelegatingResourceDescription() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("name");
		description.addProperty("description");
		description.addProperty("startDate");
		description.addProperty("endDate");
		description.addProperty("attributes");
		description.addProperty("groupCohort");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		addSharedDelegatingResourceProperties(description);
		description.addProperty("voided");
		description.addProperty("groupCohort");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		addSharedDelegatingResourceProperties(description);
		description.addProperty("groupCohort");
		description.addProperty("voided");
		description.addProperty("voidReason");
		return description;
	}
	
	private void addSharedDelegatingResourceProperties(DelegatingResourceDescription description) {
		description.addRequiredProperty("name");
		description.addProperty("description");
		description.addRequiredProperty("location");
		description.addRequiredProperty("startDate");
		description.addProperty("endDate");
		description.addRequiredProperty("cohortType");
		description.addRequiredProperty("definitionHandlerClassname");
		description.addProperty("attributes");
		description.addProperty("cohortMembers");
	}
	
	@Override
	public CohortM save(CohortM cohort) {
		if (cohort.getVoided()) {
			//end memberships if cohort is voided.
			for (CohortMember cohortMember : cohort.getCohortMembers()) {
				cohortMember.setVoided(true);
				cohortMember.setVoidReason("Cohort Ended");
				cohortMember.setEndDate(cohort.getEndDate());
			}
		}
		return cohortService.createOrUpdate(cohort);
	}
	
	@Override
	protected void delete(CohortM cohort, String reason, RequestContext request) throws ResponseException {
		cohortService.delete(cohort, reason);
	}
	
	@Override
	public void purge(CohortM cohort, RequestContext request) throws ResponseException {
		cohortService.purge(cohort);
	}
	
	@Override
	public CohortM newDelegate() {
		return new CohortM();
	}
	
	@Override
	public CohortM getByUniqueId(String uuid) {
		return cohortService.getByUuid(uuid);
	}
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		Collection<CohortM> cohort = cohortService.findAll();
		return new NeedsPaging<>(new ArrayList<>(cohort), context);
	}
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		String attributeQuery = context.getParameter("attributes");
		String cohortType = context.getParameter("cohortType");
		String location = context.getParameter("location");
		
		Map<String, String> attributes = null;
		CohortType type = null;
		
		if (StringUtils.isNotBlank(attributeQuery)) {
			try {
				attributes = new ObjectMapper().readValue("{" + attributeQuery + "}",
				    new TypeReference<Map<String, String>>() {
				    
				    });
			}
			catch (Exception e) {
				throw new RuntimeException("Invalid format for parameter 'attributes'", e);
			}
		}
		if (StringUtils.isNotBlank(cohortType)) {
			type = cohortTypeService.getByName(cohortType);
			if (type == null) {
				type = cohortTypeService.getByUuid(cohortType);
			}
			if (type == null) {
				throw new RuntimeException("No Cohort Type By Name/Uuid Found Matching The Supplied Parameter");
			}
		}
		
		if (StringUtils.isNotBlank(location)) {
			Collection<CohortM> cohorts = cohortService.findByLocationUuid(location);
			return new NeedsPaging<>(new ArrayList<>(cohorts), context);
		}
		
		List<CohortM> cohort = cohortService.findMatchingCohorts(context.getParameter("q"), attributes, type,
		    context.getIncludeAll());
		return new NeedsPaging<>(cohort, context);
		
	}
	
	/**
	 * Sets attributes on the given cohort.
	 *
	 * @param cohort The current cohort
	 * @param attributes Cohort attributes to be set
	 */
	@PropertySetter("attributes")
	public void setAttributes(CohortM cohort, List<CohortAttribute> attributes) {
		for (CohortAttribute attribute : attributes) {
			CohortAttribute existingAttribute = cohort
			        .getAttribute(cohortService.getAttributeTypeByUuid(attribute.getCohortAttributeType().getUuid()));
			if (existingAttribute != null) {
				if (attribute.getValue() == null) {
					cohort.removeAttribute(existingAttribute);
				} else {
					existingAttribute.setValue(attribute.getValue());
				}
			} else {
				cohort.addAttribute(attribute);
			}
		}
	}
	
	@PropertyGetter("display")
	public String getDisplay(CohortM cohort) {
		return cohort.getName();
	}
}
