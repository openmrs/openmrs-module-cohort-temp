/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cohort.api.dao;

import org.openmrs.Auditable;
import org.openmrs.OpenmrsObject;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value = "cohort.genericDao")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GenericDao<W extends OpenmrsObject & Auditable> extends AbstractHibernateDao<W> implements IGenericDao<W> {
	// Reusable Data Access Layer for any openmrs entity
}
