package org.openmrs.module.cohort;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Location;
import org.openmrs.VisitType;
import org.openmrs.annotation.DisableHandlers;
import org.openmrs.api.handler.VoidHandler;

@Entity
@Table(name = "cohort_visit")
public class CohortVisit extends BaseOpenmrsData {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cohort_visit_id")
	private Integer cohortVisitId;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "cohort_id")
	private CohortM cohort;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "visit_type_id")
	private VisitType visitType;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "location_id")
	private Location location;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_Date")
	private Date endDate;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cohortVisit")
	@OrderBy("cohort_visit_id")
	@DisableHandlers(handlerTypes = { VoidHandler.class })
	private List<CohortMemberVisit> cohortMemberVisits = new ArrayList<CohortMemberVisit>();

	public Integer getCohortVisitId() {
		return cohortVisitId;
	}

	public void setCohortVisitId(Integer cohortVisitId) {
		this.cohortVisitId = cohortVisitId;
	}

	public CohortM getCohort() {
		return cohort;
	}

	public void setCohort(CohortM cohort) {
		this.cohort = cohort;
	}

	public VisitType getVisitType() {
		return visitType;
	}

	public void setVisitType(VisitType visitType) {
		this.visitType = visitType;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
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
		return getCohortVisitId();
	}

	@Override
	public void setId(Integer id) {
		setCohortVisitId(id);
	}

	public void addMemberVisit(CohortMemberVisit cohortMemberVisit) {
		this.cohortMemberVisits.add(cohortMemberVisit);
	}

	public List<CohortMemberVisit> getCohortMemberVisits() {
		if (cohortMemberVisits == null) {
			cohortMemberVisits = new ArrayList<>();
		}
		return this.cohortMemberVisits;
	}

	public void setCohortMemberVisits(List<CohortMemberVisit> cohortMemberVisits) {
		this.cohortMemberVisits = cohortMemberVisits;
	}
}
