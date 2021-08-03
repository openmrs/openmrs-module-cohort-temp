package org.openmrs.module.cohort.web.resource;

import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortAttributeType;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.module.cohort.rest.v1_0.resource.CohortRest;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_9.BaseAttributeTypeCrudResource1_9;

import java.util.List;

@Resource(name = RestConstants.VERSION_1 + CohortRest.COHORT_NAMESPACE
		+ "/cohortattributetype", supportedClass = CohortAttributeType.class, supportedOpenmrsVersions = { "1.8 - 2.*" })
public class CohortAttributesTypeRequestResource extends BaseAttributeTypeCrudResource1_9<CohortAttributeType> {

	@Override
	public CohortAttributeType save(CohortAttributeType cohortAttributeType) {
		return Context.getService(CohortService.class).saveCohort(cohortAttributeType);
	}

	@Override
	public void delete(CohortAttributeType cohortAttributeType, String reason, RequestContext context)
			throws ResponseException {
		cohortAttributeType.setRetired(true);
		cohortAttributeType.setRetireReason(reason);
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
		return Context.getService(CohortService.class).getCohortAttributeTypeByUuid(id);
	}

	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		List<CohortAttributeType> list = Context.getService(CohortService.class).getAllCohortAttributeTypes();
		return new NeedsPaging<>(list, context);
	}
}
