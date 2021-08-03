package org.openmrs.module.cohort.web.resource;

import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortAttribute;
import org.openmrs.module.cohort.CohortAttributeType;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.SubResource;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_9.BaseAttributeCrudResource1_9;

import java.util.List;
@SuppressWarnings("unused")
@SubResource(parent = CohortRequestResource.class, path = "attribute",
		supportedClass = CohortAttribute.class, supportedOpenmrsVersions = { "1.8 - 2.*" })
public class CohortAttributesRequestResource extends BaseAttributeCrudResource1_9<CohortAttribute, CohortM, CohortRequestResource> {

	@Override
	public CohortM getParent(CohortAttribute cohortAttribute) {
		return cohortAttribute.getCohort();
	}

	@Override
	public void setParent(CohortAttribute cohortAttribute, CohortM cohortM) {
		cohortAttribute.setCohort(cohortM);
	}

	@Override
	public CohortAttribute newDelegate() {
		return new CohortAttribute();
	}

	@Override
	public CohortAttribute getByUniqueId(String uuid) {
		return Context.getService(CohortService.class).getCohortAttributeByUuid(uuid);
	}

	@Override
	public PageableResult doGetAll(CohortM cohortM, RequestContext requestContext) throws ResponseException {
		return new NeedsPaging<>((List<CohortAttribute>) cohortM.getActiveAttributes(), requestContext);
	}

	@Override
	public CohortAttribute save(CohortAttribute cohortAttribute) {
		return Context.getService(CohortService.class).saveCohortAttribute(cohortAttribute);
	}

	@Override
	protected void delete(CohortAttribute cohortAttribute, String reason, RequestContext request) throws ResponseException {
		cohortAttribute.setVoided(true);
		cohortAttribute.setVoidReason(reason);
		Context.getService(CohortService.class).saveCohortAttribute(cohortAttribute);
	}

	@PropertySetter("attributeType")
	public static void setAttributeType(CohortAttribute cohortAttribute, CohortAttributeType attributeType) {
		cohortAttribute.setAttributeType(attributeType);
	}

	@Override
	public void purge(CohortAttribute cohortAttribute, RequestContext request) throws ResponseException {
		Context.getService(CohortService.class).purgeCohortAtt(cohortAttribute);
	}


}
