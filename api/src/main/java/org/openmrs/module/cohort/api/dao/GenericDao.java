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

import java.util.Collection;

import org.hibernate.criterion.Criterion;
import org.openmrs.Auditable;
import org.openmrs.OpenmrsObject;
import org.openmrs.module.cohort.api.dao.search.ISearchQuery;
import org.openmrs.module.cohort.api.dao.search.PropValue;
import org.springframework.transaction.annotation.Transactional;

public interface GenericDao<W extends OpenmrsObject & Auditable> {
	
	ISearchQuery getSearchHandler();
	
	@Transactional(readOnly = true)
	W get(final int id);
	
	@Transactional(readOnly = true)
	W get(final String uuid);
	
	W createOrUpdate(W object);
	
	void delete(W object);
	
	void delete(String uuid);
	
	@Transactional(readOnly = true)
	Collection<W> findAll();
	
	@Transactional(readOnly = true)
	Collection<W> findAll(boolean includeRetired);
	
	@Transactional(readOnly = true)
	Collection<W> findBy(PropValue propValue);
	
	@Transactional(readOnly = true)
	Collection<W> findBy(PropValue propValue, boolean includeRetired);
	
	@Transactional(readOnly = true)
	W findByUniqueProp(PropValue propValue);
	
	@Transactional(readOnly = true)
	W findByUniqueProp(PropValue propValue, boolean includeRetired);
	
	@Transactional(readOnly = true)
	Collection<W> findByOr(Criterion... predicates);
	
	@Transactional(readOnly = true)
	Collection<W> findByAnd(Criterion... predicates);
	
}
