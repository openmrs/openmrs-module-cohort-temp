package org.openmrs.module.cohort.web.resource;

import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortType;
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
        + "/cohorttype", supportedClass = CohortType.class, supportedOpenmrsVersions = { "1.8 - 2.*" })
public class CohortTypeRequestResource extends DataDelegatingCrudResource<CohortType> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		
		DelegatingResourceDescription description = null;
		
		if (Context.isAuthenticated()) {
			description = new DelegatingResourceDescription();
			if (rep instanceof DefaultRepresentation) {
				description.addProperty("name");
				description.addProperty("description");
				description.addProperty("uuid");
				description.addSelfLink();
			} else if (rep instanceof FullRepresentation) {
				description.addProperty("name");
				description.addProperty("description");
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
		description.addRequiredProperty("name");
		description.addProperty("description");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
		return getCreatableProperties();
	}
	
	@Override
	public CohortType save(CohortType cohortType) {
		return Context.getService(CohortService.class).saveCohort(cohortType);
	}
	
	@Override
	protected void delete(CohortType cohortType, String reason, RequestContext request) throws ResponseException {
		cohortType.setVoided(true);
		cohortType.setVoidReason(reason);
		Context.getService(CohortService.class).saveCohort(cohortType);
	}
	
	@Override
	public void purge(CohortType cohortType, RequestContext request) throws ResponseException {
		Context.getService(CohortService.class).purgeCohortType(cohortType);
	}
	
	@Override
	public CohortType newDelegate() {
		return new CohortType();
	}
	
	@Override
	public CohortType getByUniqueId(String uuid) {
		CohortType obj = Context.getService(CohortService.class).getCohortTypeByUuid(uuid);
		if (obj == null) {
			obj = Context.getService(CohortService.class).getCohortTypeByName(uuid);
		}
		
		return obj;
	}
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		List<CohortType> cohortTypes = Context.getService(CohortService.class).getAllCohortTypes();
		return new NeedsPaging<CohortType>(cohortTypes, context);
	}
}
