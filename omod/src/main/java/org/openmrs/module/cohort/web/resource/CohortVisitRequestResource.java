package org.openmrs.module.cohort.web.resource;

import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortVisit;
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
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1 + CohortRest.COHORT_NAMESPACE
        + "/cohortvisit", supportedClass = CohortVisit.class, supportedOpenmrsVersions = { "1.8 - 2.*" })
public class CohortVisitRequestResource extends DataDelegatingCrudResource<CohortVisit> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		
		DelegatingResourceDescription description = null;
		
		if (Context.isAuthenticated()) {
			description = new DelegatingResourceDescription();
			if (rep instanceof DefaultRepresentation) {
				description.addProperty("visitType");
				description.addProperty("location");
				description.addProperty("startDate");
				description.addProperty("endDate");
				description.addProperty("cohortMemberVisits");
				description.addProperty("uuid");
				description.addSelfLink();
			} else if (rep instanceof FullRepresentation) {
				description.addProperty("cohort");
				description.addProperty("visitType");
				description.addProperty("location");
				description.addProperty("startDate");
				description.addProperty("endDate");
				description.addProperty("cohortMemberVisits");
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
		description.addRequiredProperty("visitType");
		description.addRequiredProperty("location");
		description.addRequiredProperty("startDate");
		description.addProperty("endDate");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
		return getCreatableProperties();
	}
	
	@Override
	public CohortVisit save(CohortVisit cohortVisit) {
		return Context.getService(CohortService.class).saveCohortVisit(cohortVisit);
	}
	
	@Override
	protected void delete(CohortVisit cohortVisit, String reason, RequestContext context) throws ResponseException {
		cohortVisit.setVoided(true);
		cohortVisit.setVoidReason(reason);
		Context.getService(CohortService.class).saveCohortVisit(cohortVisit);
	}
	
	@Override
	public void purge(CohortVisit cohortVisit, RequestContext context) throws ResponseException {
		Context.getService(CohortService.class).purgeCohortVisit(cohortVisit);
	}
	
	@Override
	public CohortVisit newDelegate() {
		return new CohortVisit();
	}
	
	@Override
	public CohortVisit getByUniqueId(String uuid) {
		return Context.getService(CohortService.class).getCohortVisitByUuid(uuid);
	}
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		// TODO Auto-generated method stub
		return super.doSearch(context);
	}
}
