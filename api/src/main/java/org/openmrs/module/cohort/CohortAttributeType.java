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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openmrs.BaseOpenmrsData;

@Entity
@Table(name = "cohort_attributes_type")
public class CohortAttributeType extends BaseOpenmrsData {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cohort_attribute_type_id")
	private Integer cohortAttributeTypeId;
	
	private String name;
	
	private String description;
	
	private String format;
	
	public Integer getCohortAttributeTypeId() {
		return cohortAttributeTypeId;
	}
	
	public void setCohortAttributeTypeId(Integer cohortAttributeTypeId) {
		this.cohortAttributeTypeId = cohortAttributeTypeId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	@Override
	public Integer getId() {
		return getCohortAttributeTypeId();
	}
	
	@Override
	public void setId(Integer cohortAttributeTypeId) {
		setCohortAttributeTypeId(cohortAttributeTypeId);
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
