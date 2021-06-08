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

import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortMember;
import org.openmrs.module.cohort.CohortMemberAttribute;
import org.openmrs.module.cohort.CohortMemberAttributeType;
import org.openmrs.module.cohort.api.CohortMemberService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.SubResource;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_9.BaseAttributeCrudResource1_9;

import java.util.List;

@SuppressWarnings("unused")
@SubResource( parent = CohortMemberRequestResource.class, path = "attribute",
        supportedClass = CohortMemberAttribute.class, supportedOpenmrsVersions = {"1.8 - 2.*"})
public class CohortMemberAttributeResource extends BaseAttributeCrudResource1_9<CohortMemberAttribute, CohortMember, CohortMemberRequestResource> {

    private final CohortMemberService cohortMemberService;

    public CohortMemberAttributeResource() {
        this.cohortMemberService = Context.getRegisteredComponent("cohortMemberService", CohortMemberService.class);
    }

    @Override
    public CohortMember getParent(CohortMemberAttribute cohortMemberAttribute) {
        return cohortMemberAttribute.getCohortMember();
    }

    @Override
    public void setParent(CohortMemberAttribute cohortMemberAttribute, CohortMember cohortMember) {
        cohortMemberAttribute.setCohortMember(cohortMember);
    }

    @Override
    public PageableResult doGetAll(CohortMember cohortMember, RequestContext requestContext) throws ResponseException {
        return new NeedsPaging<>((List<CohortMemberAttribute>) cohortMember.getActiveAttributes(), requestContext);
    }

    @Override
    public CohortMemberAttribute getByUniqueId(String uuid) {
        return cohortMemberService.getCohortMemberAttributeByUuid(uuid);
    }

    @Override
    public CohortMemberAttribute save(CohortMemberAttribute cohortMemberAttribute) {
        return cohortMemberService.saveCohortMemberAttribute(cohortMemberAttribute);
    }

    @Override
    protected void delete(CohortMemberAttribute cohortMemberAttribute, String reason, RequestContext requestContext) throws ResponseException {
        cohortMemberAttribute.setVoided(true);
        cohortMemberAttribute.setVoidReason(reason);
        cohortMemberService.saveCohortMemberAttribute(cohortMemberAttribute);
    }

    @Override
    public CohortMemberAttribute newDelegate() {
        return new CohortMemberAttribute();
    }

    @Override
    public void purge(CohortMemberAttribute cohortMemberAttribute, RequestContext requestContext) throws ResponseException {
        cohortMemberService.purgeCohortMemberAttribute(cohortMemberAttribute);
    }

    @PropertySetter("attributeType")
    public static void setAttributeType(CohortMemberAttribute cohortMemberAttribute, CohortMemberAttributeType attributeType) {
        cohortMemberAttribute.setAttributeType(attributeType);
    }
}
