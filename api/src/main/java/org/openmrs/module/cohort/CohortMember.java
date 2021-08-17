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

import org.openmrs.BaseCustomizableData;
import org.openmrs.Patient;

@Entity
@Table(name = "cohort_member")
public class CohortMember extends BaseCustomizableData<CohortMemberAttribute> {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cohort_member_id")
	private Integer cohortMemberId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "patient_id")
	private Patient patient;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cohort_id")
	private CohortM cohort;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cohort_role_id")
	private CohortRole role;
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "end_date")
	private Date endDate;
	
	@Column(name = "is_head")
	private Boolean head;
	
	public CohortMember() {
		
	}
	
	public CohortMember(Patient patient) {
		if (patient != null) {
			this.patient = patient;
			if (patient.getUuid() != null) {
				this.setUuid(patient.getUuid());
			}
		}
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
	
	@Override
	public Integer getId() {
		return getCohortMemberId();
	}
	
	@Override
	public void setId(Integer arg0) {
		setCohortMemberId(arg0);
	}
	
	public Integer getCohortMemberId() {
		return cohortMemberId;
	}
	
	public void setCohortMemberId(Integer cohortMemberId) {
		this.cohortMemberId = cohortMemberId;
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public CohortM getCohort() {
		return cohort;
	}
	
	public void setCohort(CohortM cohort) {
		this.cohort = cohort;
	}
	
	public CohortRole getRole() {
		return role;
	}
	
	public void setRole(CohortRole role) {
		this.role = role;
	}
	
	@Deprecated
	public Boolean isHead() {
		return head;
	}
	
	public void setHead(Boolean head) {
		this.head = head;
	}
	
	public Boolean getHead() {
		return head;
	}
}
