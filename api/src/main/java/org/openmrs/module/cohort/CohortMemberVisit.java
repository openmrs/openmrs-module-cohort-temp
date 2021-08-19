/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cohort;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Visit;

@Entity
@Table(name = "cohort_member_visit")
public class CohortMemberVisit extends BaseOpenmrsData {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cohort_member_visit_id")
	private Integer cohortMemberVisitId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "visit_id")
	private Visit visit;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cohort_visit_id")
	private CohortVisit cohortVisit;
	
	@Override
	public Integer getId() {
		return this.getCohortMemberVisitId();
	}
	
	@Override
	public void setId(Integer id) {
		this.setCohortMemberVisitId(id);
	}
	
	public int getCohortMemberVisitId() {
		return cohortMemberVisitId;
	}
	
	public void setCohortMemberVisitId(int cohortMemberVisitId) {
		this.cohortMemberVisitId = cohortMemberVisitId;
	}
	
	public Visit getVisit() {
		return visit;
	}
	
	public void setVisit(Visit visit) {
		this.visit = visit;
	}
	
	public CohortVisit getCohortVisit() {
		return cohortVisit;
	}
	
	public void setCohortVisit(CohortVisit cohortVisit) {
		this.cohortVisit = cohortVisit;
	}
}
