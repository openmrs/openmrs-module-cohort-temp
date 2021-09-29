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
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortMember;
import org.openmrs.module.cohort.CohortMemberAttribute;
import org.openmrs.module.cohort.api.CohortMemberService;
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
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@SuppressWarnings("unused")
@Resource(name = RestConstants.VERSION_1 + CohortMainRestController.COHORT_NAMESPACE
        + "/cohortmember", supportedClass = CohortMember.class, supportedOpenmrsVersions = { "1.8 - 2.*" })
public class CohortMemberResource extends DataDelegatingCrudResource<CohortMember> {
	
	private final CohortMemberService cohortMemberService;
	
	public CohortMemberResource() {
		this.cohortMemberService = Context.getRegisteredComponent("cohort.cohortMemberService", CohortMemberService.class);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		if (Context.isAuthenticated()) {
			if (rep instanceof DefaultRepresentation) {
				final DelegatingResourceDescription description = new DelegatingResourceDescription();
				description.addProperty("patient", Representation.REF);
				description.addProperty("startDate");
				description.addProperty("endDate");
				description.addProperty("uuid");
				description.addProperty("voided");
				description.addProperty("attributes", "activeAttributes", Representation.REF);
				description.addProperty("cohort", Representation.REF);
				description.addSelfLink();
				description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
				return description;
			} else if (rep instanceof FullRepresentation) {
				final DelegatingResourceDescription description = new DelegatingResourceDescription();
				description.addProperty("display");
				description.addProperty("startDate");
				description.addProperty("endDate");
				description.addProperty("uuid");
				description.addProperty("voided");
				description.addProperty("patient", Representation.FULL);
				description.addProperty("cohort", Representation.DEFAULT);
				description.addProperty("attributes", "activeAttributes", Representation.DEFAULT);
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
		description.addProperty("endDate");
		description.addRequiredProperty("patient");
		description.addRequiredProperty("startDate");
		description.addProperty("voided");
		description.addProperty("attributes");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("startDate");
		description.addProperty("endDate");
		description.addProperty("voided");
		return description;
	}
	
	@Override
	public CohortMember newDelegate() {
		return new CohortMember();
	}
	
	@Override
	public CohortMember save(CohortMember cohortMember) throws ResponseException {
		CohortM cohort = cohortMember.getCohort();
		Patient newPatient = cohortMember.getPatient();
		if (cohort.getVoided()) {
			throw new RuntimeException("Cannot add patient to ended group.");
		}
		for (CohortMember member : cohort.getCohortMembers()) {
			if (member.getPatient().getUuid().equals(newPatient.getUuid()) && !cohortMember.getVoided()) {
				if (member.getEndDate() == null) {
					throw new RuntimeException("Patient already exists in group.");
				} else {
					member.setVoided(false);
					member.setEndDate(null);
					cohortMember = member;
				}
			}
		}
		return cohortMemberService.createOrUpdate(cohortMember);
	}
	
	@Override
	public void delete(CohortMember cohortMember, String reason, RequestContext context) throws ResponseException {
		cohortMemberService.delete(cohortMember, reason);
	}
	
	@Override
	public CohortMember getByUniqueId(String uuid) {
		return cohortMemberService.getByUuid(uuid);
	}
	
	@Override
	public void purge(CohortMember cohortMember, RequestContext context) throws ResponseException {
		boolean purge = Boolean.getBoolean(context.getParameter("purge"));
		if (purge) {
			cohortMemberService.purge(cohortMember);
		} else {
			cohortMemberService.delete(cohortMember, "");
		}
	}
	
	@PropertySetter("attributes")
	public static void setAttributes(CohortMember cohortMember, Set<CohortMemberAttribute> attributes) {
		for (CohortMemberAttribute attribute : attributes) {
			attribute.setOwner(cohortMember);
		}
		cohortMember.setAttributes(attributes);
	}
	
	@PropertyGetter("display")
	public String getDisplayString(CohortMember cohortMember) {
		Patient patient = cohortMember.getPatient();
		if (patient != null) {
			PatientIdentifier identifier = patient.getPatientIdentifier();
			return identifier + "-" + patient.getPersonName().getFullName();
		}
		
		return null;
	}
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		String cohortUuid = context.getParameter("cohort");
		String patientUuid = context.getParameter("patient");
		
		Collection<CohortMember> cohortMemberCollection;
		if (StringUtils.isNotBlank(cohortUuid) && StringUtils.isNotBlank(patientUuid)) {
			throw new IllegalArgumentException(
			        "Patient and Cohort Parameters can't both be declared in the url, search by either cohort or patient, not both");
		} else if (StringUtils.isNotBlank(cohortUuid)) {
			cohortMemberCollection = cohortMemberService.findCohortMembersByCohortUuid(cohortUuid);
		} else if (StringUtils.isNotBlank(patientUuid)) {
			cohortMemberCollection = cohortMemberService.findCohortMembersByPatientUuid(patientUuid);
		} else {
			throw new IllegalArgumentException("No valid value specified for param cohort and/or patient");
		}
		
		return new NeedsPaging<>(new ArrayList<>(cohortMemberCollection), context);
	}
}
