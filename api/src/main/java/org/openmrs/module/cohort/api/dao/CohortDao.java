package org.openmrs.module.cohort.api.dao;

import org.hibernate.SessionFactory;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.api.dao.search.SearchQueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CohortDao extends AbstractGenericDao<CohortM> {
	
	@Autowired
	public CohortDao(@Qualifier("sessionFactory") SessionFactory sessionFactory,
	    @Qualifier("cohort.search.cohortSearchHandler") SearchQueryHandler searchHandler) {
		super(sessionFactory, searchHandler);
	}
}
