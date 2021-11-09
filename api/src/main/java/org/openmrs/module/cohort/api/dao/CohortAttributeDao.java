package org.openmrs.module.cohort.api.dao;

import org.hibernate.SessionFactory;
import org.openmrs.module.cohort.CohortAttribute;
import org.openmrs.module.cohort.api.dao.search.SearchQueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CohortAttributeDao extends AbstractGenericDao<CohortAttribute> {
	
	@Autowired
	public CohortAttributeDao(@Qualifier("sessionFactory") SessionFactory sessionFactory,
	    @Qualifier("cohort.search.cohortSearchHandler") SearchQueryHandler searchHandler) {
		super(sessionFactory, searchHandler);
	}
}
