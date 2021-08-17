package org.openmrs.module.cohort.web.resource;

import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortEncounter;
import org.openmrs.module.cohort.CohortObs;
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
        + "/cohortobs", supportedClass = CohortObs.class, supportedOpenmrsVersions = { "1.8 - 2.*" })
public class CohortObsRequestResource extends DataDelegatingCrudResource<CohortObs> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		
		DelegatingResourceDescription description = null;
		
		if (Context.isAuthenticated()) {
			description = new DelegatingResourceDescription();
			if (rep instanceof DefaultRepresentation) {
				description.addProperty("cohort");
				description.addProperty("concept");
				description.addProperty("encounter");
				description.addProperty("location");
				description.addProperty("obsDateTime");
				description.addProperty("groupMembers");
				description.addProperty("obsGroup");
				description.addProperty("valueCoded");
				description.addProperty("valueDatetime");
				description.addProperty("valueNumeric");
				description.addProperty("valueText");
				description.addProperty("accessionNumber");
				description.addProperty("uuid");
				description.addSelfLink();
			} else if (rep instanceof FullRepresentation) {
				description.addProperty("cohort");
				description.addProperty("concept", Representation.FULL);
				description.addProperty("encounter", Representation.DEFAULT);
				description.addProperty("location", Representation.FULL);
				description.addProperty("obsDateTime");
				description.addProperty("groupMembers", Representation.FULL);
				description.addProperty("obsGroup", Representation.FULL);
				description.addProperty("comment");
				description.addProperty("valueCoded", Representation.FULL);
				description.addProperty("valueDatetime");
				description.addProperty("valueNumeric");
				description.addProperty("valueModifier");
				description.addProperty("valueText");
				description.addProperty("accessionNumber");
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
		description.addRequiredProperty("concept");
		description.addRequiredProperty("encounter");
		description.addProperty("location");
		description.addRequiredProperty("obsDateTime");
		description.addProperty("groupMembers");
		description.addProperty("comment");
		description.addProperty("valueCoded");
		description.addProperty("valueDatetime");
		description.addProperty("valueNumeric");
		description.addProperty("valueModifier");
		description.addProperty("valueText");
		description.addProperty("accessionNumber");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("location");
		description.addRequiredProperty("obsDateTime");
		description.addProperty("groupMembers");
		description.addProperty("comment");
		description.addProperty("valueCoded");
		description.addProperty("valueDatetime");
		description.addProperty("valueNumeric");
		description.addProperty("valueModifier");
		description.addProperty("valueText");
		description.addProperty("accessionNumber");
		return description;
	}
	
	@Override
	public CohortObs save(CohortObs cohortObs) {
		return Context.getService(CohortService.class).saveCohortObs(cohortObs);
	}
	
	@Override
	protected void delete(CohortObs cohortObs, String reason, RequestContext context) throws ResponseException {
		cohortObs.setVoided(true);
		cohortObs.setVoidReason(reason);
		Context.getService(CohortService.class).saveCohortObs(cohortObs);
	}
	
	@Override
	public void purge(CohortObs cohortObs, RequestContext context) throws ResponseException {
		Context.getService(CohortService.class).purgeCohortObs(cohortObs);
	}
	
	@Override
	public CohortObs newDelegate() {
		return new CohortObs();
	}
	
	@Override
	public CohortObs getByUniqueId(String id) {
		return Context.getService(CohortService.class).getCohortObsByUuid(id);
	}
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		String encounter = context.getParameter("encounter");
		
		CohortEncounter encountero = Context.getService(CohortService.class).getCohortEncounterByUuid(encounter);
		if (encountero == null) {
			throw new IllegalArgumentException("No valid value specified for param encounter");
		}
		
		List<CohortObs> list = Context.getService(CohortService.class)
		        .getCohortObsByEncounterId(encountero.getEncounterId());
		return new NeedsPaging<CohortObs>(list, context);
	}
}
