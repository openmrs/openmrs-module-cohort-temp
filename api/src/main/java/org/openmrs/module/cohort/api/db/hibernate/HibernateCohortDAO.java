/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cohort.api.db.hibernate;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.cohort.CohortAttribute;
import org.openmrs.module.cohort.CohortAttributeType;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.CohortMember;
import org.openmrs.module.cohort.CohortType;
import org.openmrs.module.cohort.api.db.CohortDAO;

/**
 * It is a default implementation of {@link CohortDAO}.
 */
@SuppressWarnings("unchecked")
public class HibernateCohortDAO implements CohortDAO {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public CohortAttribute saveCohortAttributes(CohortAttribute att) {
		getCurrentSession().saveOrUpdate(att);
		return att;
	}
	
	@Override
	public List<CohortAttribute> findCohortAttribute(String name) {
		Criteria criteria = getCurrentSession().createCriteria(CohortAttribute.class);
		criteria.add(Restrictions.ilike("value", name, MatchMode.START));
		return criteria.list();
	}
	
	@Override
	public CohortMember saveCPatient(CohortMember cohortMember) {
		getCurrentSession().saveOrUpdate(cohortMember);
		return cohortMember;
	}
	
	@Override
	public CohortType saveCohortType(CohortType cohorttype) {
		getCurrentSession().saveOrUpdate(cohorttype);
		return cohorttype;
	}
	
	@Override
	public List<CohortType> getAllCohortTypes() {
		Query queryResult = getCurrentSession().createQuery("from CohortType");
		return queryResult.list();
	}
	
	@Override
	public void purgeCohortType(CohortType cohort) {
		getCurrentSession().delete(cohort);
	}
	
	@Override
	public void purgeCohortAtt(CohortAttribute att) {
		getCurrentSession().delete(att);
	}
	
	@Override
	public List<CohortAttributeType> getAllCohortAttributes() {
		Query queryResult = getCurrentSession().createQuery("from CohortAttributeType");
		return queryResult.list();
	}
	
	@Override
	public CohortAttributeType findCohortAttributes(String attribute_type_name) {
		Criteria criteria = getCurrentSession().createCriteria(CohortAttributeType.class);
		criteria.add(Restrictions.eq("name", attribute_type_name));
		return (CohortAttributeType) criteria.uniqueResult();
	}
	
	@Override
	public void purgeCohortAttributes(CohortAttributeType attributes) {
		getCurrentSession().delete(attributes);
	}
	
	@Override
	public CohortType getCohortType(Integer id) {
		return (CohortType) getCurrentSession().get(CohortType.class, id);
	}
	
	@Override
	public CohortAttributeType saveCohortAttributes(CohortAttributeType attributes) {
		getCurrentSession().saveOrUpdate(attributes);
		return attributes;
	}
	
	@Override
	public CohortAttributeType getCohortAttributes(Integer cohort_attribute_type_id) {
		return (CohortAttributeType) getCurrentSession().get(CohortAttributeType.class, cohort_attribute_type_id);
	}
	
	@Override
	public CohortM saveCohort(CohortM cohort) {
		getCurrentSession().saveOrUpdate(cohort);
		return cohort;
	}
	
	@Override
	public void purgeCohort(CohortM cohort) {
		getCurrentSession().delete(cohort);
	}
	
	@Override
	public CohortM getCohort(Integer id) {
		return (CohortM) getCurrentSession().get(CohortM.class, id);
	}
	
	@Override
	public List<CohortM> findCohorts() {
		return (List<CohortM>) getCurrentSession().createQuery("from CohortM").list();
	}
	
	@Override
	public List<CohortM> findCohorts(String nameMatching, Map<String, String> attributes, CohortType cohortType) {
		Criteria criteria = getCurrentSession().createCriteria(CohortM.class);
		
		if (StringUtils.isNotBlank(nameMatching)) {
			criteria.add(Restrictions.ilike("name", nameMatching, MatchMode.ANYWHERE));
		}
		
		if (attributes != null && !attributes.isEmpty()) {
			Criteria cri = criteria.createCriteria("attributes").add(Restrictions.eq("voided", false))
			        .createAlias("cohortAttributeType", "attrType");
			
			Disjunction dis = Restrictions.disjunction();
			for (String k : attributes.keySet()) {
				dis.add(Restrictions.conjunction(Restrictions.eq("attrType.name", k),
				    Restrictions.like("value", attributes.get(k), MatchMode.ANYWHERE)));
			}
			
			cri.add(dis);
		}
		
		if (cohortType != null) {
			criteria.add(Restrictions.eq("cohortType.cohortTypeId", cohortType.getCohortTypeId()));
		}
		
		criteria.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}
	
	@Override
	public List<CohortMember> getCohortMembersByPatientId(int patientId) {
		return (List<CohortMember>) getCurrentSession().createQuery("from CohortMember t where t.patient.patientId = :id")
		        .setInteger("id", patientId).list();
	}
	
	@Override
	public List<CohortAttribute> getCohortAttributesByAttributeType(Integer attributeTypeId) {
		return (List<CohortAttribute>) getCurrentSession()
		        .createQuery("from CohortAttribute t where t.cohortAttributeType.cohortAttributeTypeId = :attributeTypeId")
		        .setInteger("attributeTypeId", attributeTypeId).list();
	}
	
	@Override
	public CohortM getCohortByName(String name) {
		return (CohortM) getCurrentSession().createQuery("from CohortM t where t.name = :name").setString("name", name)
		        .uniqueResult();
	}
	
	@Override
	public CohortM getCohortUuid(String uuid) {
		return (CohortM) getCurrentSession().createCriteria(CohortM.class).add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}
	
	@Override
	public CohortMember getCohortMemUuid(String uuid) {
		return (CohortMember) getCurrentSession().createQuery("from CohortMember t where t.uuid = :uuid")
		        .setString("uuid", uuid).uniqueResult();
	}
	
	@Override
	public CohortType getCohortTypeByName(String name) {
		return (CohortType) getCurrentSession().createQuery("from CohortType t where t.name = :name").setString("name", name)
		        .uniqueResult();
	}
	
	@Override
	public CohortAttributeType getCohortAttributeTypeUuid(String uuid) {
		return (CohortAttributeType) getCurrentSession().createQuery("from CohortAttributeType t where t.uuid = :uuid")
		        .setString("uuid", uuid).uniqueResult();
	}
	
	@Override
	public CohortAttribute getCohortAttribute(Integer id) {
		return (CohortAttribute) getCurrentSession().get(CohortAttribute.class, id);
	}
	
	@Override
	public CohortAttributeType findCohortAttributeType(Integer id) {
		return (CohortAttributeType) getCurrentSession().get(CohortAttributeType.class, id);
	}
	
	@Override
	public Long getCount(String name) {
		return (Long) getCurrentSession()
		        .createQuery("select count(*) from CohortMember c left outer join c.cohort m where m.name=:name")
		        .setParameter("name", name).uniqueResult();
	}
	
	@Override
	public CohortMember getCohortMember(Integer id) {
		return (CohortMember) getCurrentSession().get(CohortMember.class, id);
	}
	
	@Override
	// FIXME Does this work?
	public List<CohortMember> findCohortMember(String name) {
		Session session = getCurrentSession();
		Query queryResult = session
		        .createQuery("from CohortMember where patient=(select patientId from Person where names=:name")
		        .setParameter("name", name);
		return queryResult.list();
	}
	
	@Override
	public List<CohortMember> findCohortMembersByCohortId(Integer cohortId) {
		return getCurrentSession().createQuery("from CohortMember where cohort = :cohortId")
		        .setParameter("cohortId", cohortId).list();
	}
	
	/**
	 * Gets the current hibernate session while taking care of the hibernate 3 and 4 differences.
	 *
	 * @return the current hibernate session.
	 */
	private org.hibernate.Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public CohortAttribute getCohortAttributeById(Integer id) {
		return (CohortAttribute) getCurrentSession().get(CohortAttribute.class, id);
	}
	
	@Override
	public CohortAttribute getCohortAttributeByUuid(String uuid) {
		return (CohortAttribute) getCurrentSession().createQuery("from CohortAttribute t where t.uuid = :uuid")
		        .setString("uuid", uuid).uniqueResult();
	}
	
	@Override
	public CohortAttributeType getCohortAttributeTypeById(Integer id) {
		return (CohortAttributeType) getCurrentSession().get(CohortAttributeType.class, id);
	}
	
	@Override
	public CohortAttributeType getCohortAttributeTypeByUuid(String uuid) {
		return (CohortAttributeType) getCurrentSession().createQuery("from CohortAttributeType t where t.uuid = :uuid")
		        .setString("uuid", uuid).uniqueResult();
	}
	
	@Override
	public CohortM getCohortMById(Integer id) {
		return (CohortM) getCurrentSession().get(CohortM.class, id);
	}
	
	@Override
	public CohortM getCohortMByUuid(String uuid) {
		return (CohortM) getCurrentSession().createQuery("from CohortM t where t.uuid = :uuid").setString("uuid", uuid)
		        .uniqueResult();
	}
	
	@Override
	public CohortType getCohortTypeById(Integer id) {
		return (CohortType) getCurrentSession().get(CohortType.class, id);
	}
	
	@Override
	public CohortType getCohortTypeByUuid(String uuid) {
		return (CohortType) getCurrentSession().createQuery("from CohortType t where t.uuid = :uuid").setString("uuid", uuid)
		        .uniqueResult();
	}
	
	@Override
	public List<CohortM> getCohortsByLocationId(Integer id) {
		return (List<CohortM>) getCurrentSession().createQuery("from CohortM t where t.location.locationId = :id")
		        .setString("id", id.toString()).list();
	}
	
	@Override
	public List<CohortMember> getCohortMembersByCohortRoleId(Integer id) {
		return (List<CohortMember>) getCurrentSession().createQuery("from CohortM t where t.role.cohortProgramId = :id")
		        .setString("id", id.toString()).list();
	}
	
	@Override
	public CohortM getCohort(Integer locationId, Integer programId, Integer typeId) {
		Criteria criteria = getCurrentSession().createCriteria(CohortM.class, "cohort");
		if (locationId != null) {
			criteria.createAlias("cohort.location", "loc");
			criteria.add(Restrictions.eq("loc.locationId", locationId));
		}
		if (programId != null) {
			criteria.createAlias("cohort.cohortProgram", "program");
			criteria.add(Restrictions.eq("program.programId", programId));
		}
		if (typeId != null) {
			criteria.createAlias("cohort.cohortType", "type");
			criteria.add(Restrictions.eq("type.cohortTypeId", typeId));
		}
		return (CohortM) criteria.uniqueResult();
	}
	
	@Override
	public List<CohortMember> getCohortMembersByCohortId(Integer id) {
		Criteria criteria = getCurrentSession().createCriteria(CohortMember.class, "cohortMember")
		        .createAlias("cohortMember.cohort", "cohort");
		criteria.add(Restrictions.eq("cohort.cohortId", id));
		return criteria.list();
	}
	
	@Override
	public List<CohortAttribute> findCohortAttributes(Integer cohortId, Integer attributeTypeId) {
		Criteria cri = getCurrentSession().createCriteria(CohortAttribute.class);
		if (cohortId != null) {
			cri.createAlias("cohort", "c");
			cri.add(Restrictions.eq("c.cohortId", cohortId));
		}
		
		if (attributeTypeId != null) {
			cri.createAlias("cohortAttributeType", "a");
			cri.add(Restrictions.eq("a.cohortAttributeTypeId", attributeTypeId));
		}
		return cri.list();
	}
}
