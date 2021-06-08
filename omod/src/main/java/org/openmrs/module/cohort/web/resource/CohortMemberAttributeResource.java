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
import org.openmrs.module.cohort.CohortMemberAttribute;
import org.openmrs.module.cohort.api.CohortMemberService;
import org.openmrs.module.cohort.rest.v1_0.resource.CohortRest;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;

@Resource(name = RestConstants.VERSION_1 + CohortRest.COHORT_NAMESPACE + "/cohort-member-attribute",
        supportedClass = CohortMemberAttribute.class, supportedOpenmrsVersions = {"2.0.*", "2.1.*", "2.2.*, 2.3.*, 2.4.*"})
public class CohortMemberAttributeResource extends DataDelegatingCrudResource<CohortMemberAttribute> {

    @Autowired
    private CohortMemberService cohortMemberService;

    @Override
    public CohortMemberAttribute getByUniqueId(String uuid) {
        return cohortMemberService.getCohortMemberAttributeByUuid(uuid);
    }

    @Override
    protected void delete(CohortMemberAttribute cohortMemberAttribute, String s, RequestContext requestContext) throws ResponseException {
        cohortMemberAttribute.setVoided(true);
        cohortMemberAttribute.setVoidReason(s);
        cohortMemberAttribute.setVoidedBy(Context.getAuthenticatedUser());
        cohortMemberService.saveCohortMemberAttribute(cohortMemberAttribute);
    }

    @Override
    public CohortMemberAttribute newDelegate() {
        return new CohortMemberAttribute();
    }

    @Override
    public CohortMemberAttribute save(CohortMemberAttribute cohortMemberAttribute) {
        return cohortMemberService.saveCohortMemberAttribute(cohortMemberAttribute);
    }

    @Override
    public void purge(CohortMemberAttribute cohortMemberAttribute, RequestContext requestContext) throws ResponseException {
        cohortMemberService.purgeCohortMemberAttribute(cohortMemberAttribute);
    }

    @Override
    public DelegatingResourceDescription getCreatableProperties() {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addRequiredProperty("cohortMember");
        description.addRequiredProperty("value");
        description.addRequiredProperty("cohortMemberAttributeType");
        return description;
    }

    @Override
    public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
        return getCreatableProperties();
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        if (representation instanceof DefaultRepresentation) {
            description.addProperty("uuid");
            description.addProperty("value");
            description.addProperty("cohortMemberAttributeType");
            description.addProperty("cohortMember", Representation.REF);
            description.addSelfLink();
        } else if (representation instanceof FullRepresentation) {
            description.addProperty("uuid");
            description.addProperty("value");
            description.addProperty("cohortMemberAttributeType");
            description.addProperty("cohortMember", Representation.REF);
            description.addProperty("auditInfo");
            description.addSelfLink();
        }
        return description;
    }
}
