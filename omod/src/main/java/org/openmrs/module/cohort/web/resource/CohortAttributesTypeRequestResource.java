package org.openmrs.module.cohort.web.resource;

import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortAttributeType;
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
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1 + CohortRest.COHORT_NAMESPACE
        + "/cohortattributetype", supportedClass = CohortAttributeType.class, supportedOpenmrsVersions = { "1.8 - 2.*" })
public class CohortAttributesTypeRequestResource extends DataDelegatingCrudResource<CohortAttributeType> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		
		DelegatingResourceDescription description = null;
		
		if (Context.isAuthenticated()) {
			description = new DelegatingResourceDescription();
			if (rep instanceof DefaultRepresentation) {
				description.addProperty("name");
				description.addProperty("description");
				description.addProperty("format");
				description.addProperty("uuid");
				description.addSelfLink();
			} else if (rep instanceof FullRepresentation) {
				description.addProperty("name");
				description.addProperty("description");
				description.addProperty("format");
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
		description.addRequiredProperty("format");
		return description;
	}
	
	@Override
	public CohortAttributeType save(CohortAttributeType cohortAttributeType) {
		return Context.getService(CohortService.class).saveCohort(cohortAttributeType);
	}
	
	@Override
	protected void delete(CohortAttributeType cohortAttributeType, String reason, RequestContext context)
	        throws ResponseException {
		cohortAttributeType.setVoided(true);
		cohortAttributeType.setVoidReason(reason);
		Context.getService(CohortService.class).saveCohort(cohortAttributeType);
	}
	
	@Override
	public void purge(CohortAttributeType cohortAttributeType, RequestContext context) throws ResponseException {
		Context.getService(CohortService.class).purgeCohortAttributes(cohortAttributeType);
	}
	
	@Override
	public CohortAttributeType newDelegate() {
		return new CohortAttributeType();
	}
	
	@Override
	public CohortAttributeType getByUniqueId(String id) {
		CohortAttributeType obj = Context.getService(CohortService.class).getCohortAttributeTypeByUuid(id);
		if (obj == null) {
			obj = Context.getService(CohortService.class).getCohortAttributeTypeByName(id);
		}
		return obj;
	}
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		List<CohortAttributeType> list = Context.getService(CohortService.class).getAllCohortAttributeTypes();
		return new NeedsPaging<CohortAttributeType>(list, context);
	}
}
