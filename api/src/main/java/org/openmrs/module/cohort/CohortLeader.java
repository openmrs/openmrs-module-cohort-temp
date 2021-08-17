
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 **/

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

import java.util.Date;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Person;

@Entity
@Table(name = "cohort_leader")
public class CohortLeader extends BaseOpenmrsData {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cohort_leader_id")
	private Integer cohortLeaderId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "person_id")
	private Person person;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cohort_id")
	private CohortM cohort;
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "end_date")
	private Date endDate;
	
	public CohortLeader() {
		
	}
	
	public CohortLeader(Person person) {
		if (person != null) {
			this.person = person;
			if (person.getUuid() != null) {
				this.setUuid(person.getUuid());
			}
		}
	}
	
	@Override
	public Integer getId() {
		return cohortLeaderId;
	}
	
	@Override
	public void setId(Integer id) {
		this.cohortLeaderId = id;
	}
	
	public int getCohortLeaderId() {
		return cohortLeaderId;
	}
	
	public void setCohortLeaderId(int cohortLeaderId) {
		this.cohortLeaderId = cohortLeaderId;
	}
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public CohortM getCohort() {
		return cohort;
	}
	
	public void setCohort(CohortM cohort) {
		this.cohort = cohort;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
