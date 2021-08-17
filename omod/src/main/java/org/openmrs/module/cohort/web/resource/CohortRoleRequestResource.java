package org.openmrs.module.cohort.web.resource;

import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortRole;
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
        + "/cohortrole", supportedClass = CohortRole.class, supportedOpenmrsVersions = { "1.8 - 2.*" })
public class CohortRoleRequestResource extends DataDelegatingCrudResource<CohortRole> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		
		DelegatingResourceDescription description = null;
		
		if (Context.isAuthenticated()) {
			description = new DelegatingResourceDescription();
			if (rep instanceof DefaultRepresentation) {
				description.addProperty("name");
				description.addProperty("cohortType");
				description.addProperty("uuid");
			} else if (rep instanceof FullRepresentation) {
				description.addProperty("name");
				description.addProperty("cohortType");
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
		description.addRequiredProperty("cohortType");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
		return getCreatableProperties();
	}
	
	@Override
	public CohortRole newDelegate() {
		return new CohortRole();
	}
	
	@Override
	public CohortRole save(CohortRole cohortRole) {
		return Context.getService(CohortService.class).saveCohortRole(cohortRole);
	}
	
	@Override
	protected void delete(CohortRole cohortRole, String reason, RequestContext context) throws ResponseException {
		cohortRole.setVoided(true);
		cohortRole.setVoidReason(reason);
		Context.getService(CohortService.class).saveCohortRole(cohortRole);
	}
	
	@Override
	public void purge(CohortRole cohortRole, RequestContext context) throws ResponseException {
		Context.getService(CohortService.class).purgeCohortRole(cohortRole);
	}
	
	@Override
	public CohortRole getByUniqueId(String id) {
		CohortRole obj = Context.getService(CohortService.class).getCohortRoleByUuid(id);
		if (obj == null) {
			obj = Context.getService(CohortService.class).getCohortRoleByName(id);
		}
		return obj;
	}
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		List<CohortRole> list = Context.getService(CohortService.class).getAllCohortRoles();
		return new NeedsPaging<CohortRole>(list, context);
	}
}
