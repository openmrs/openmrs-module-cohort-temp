package org.openmrs.module.cohort.api.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.cohort.CohortMember;
import org.openmrs.module.cohort.api.SpringTestConfiguration;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = SpringTestConfiguration.class, inheritLocations = false)
public class CohortMemberGenericDaoTest extends BaseModuleContextSensitiveTest {
	
	//Order of the array is salient
	private static final String[] COHORT_MEMBER_INITIAL_TEST_DATA_XML = {
	        "org/openmrs/module/cohort/api/hibernate/db/CohortDaoTest_initialTestData.xml",
	        "org/openmrs/module/cohort/api/hibernate/db/CohortMemberDaoTest_initialTestData.xml" };
	
	private static final String COHORT_MEMBER_UUID = "23517bf9-d8d7-4726-b4f1-a2dff6b36w32";
	
	@Autowired
	@Qualifier("cohort.genericDao")
	private IGenericDao<CohortMember> dao;
	
	@Before
	public void setup() throws Exception {
		dao.setClazz(CohortMember.class);
		for (String data : COHORT_MEMBER_INITIAL_TEST_DATA_XML) {
			executeDataSet(data);
		}
	}
	
	@Test
	public void shouldGetCohortMemberByUuid() {
		CohortMember cohortMember = dao.get(COHORT_MEMBER_UUID);
		
		assertThat(cohortMember, notNullValue());
		assertThat(cohortMember.getUuid(), is(COHORT_MEMBER_UUID));
	}
}
