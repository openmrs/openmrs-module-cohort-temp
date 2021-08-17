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

@Entity
@Table(name = "cohort_role")
public class CohortRole extends BaseOpenmrsData {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cohort_role_id")
	private Integer cohortRoleId;
	
	private String name;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cohort_type_id")
	private CohortType cohortType;
	
	@Override
	public Integer getId() {
		return getCohortRoleId();
	}
	
	@Override
	public void setId(Integer id) {
		setCohortRoleId(id);
	}
	
	public Integer getCohortRoleId() {
		return cohortRoleId;
	}
	
	public void setCohortRoleId(int cohortRoleId) {
		this.cohortRoleId = cohortRoleId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public CohortType getCohortType() {
		return cohortType;
	}
	
	public void setCohortType(CohortType cohortType) {
		this.cohortType = cohortType;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
