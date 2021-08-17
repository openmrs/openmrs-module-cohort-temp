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
	public void setId(Integer id) {
		setCohortAttributeTypeId(id);
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
