package org.openmrs.module.cohort;

import org.openmrs.BaseCustomizableData;
import org.openmrs.Location;
import org.openmrs.util.OpenmrsUtil;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name ="cohort")
public class CohortM extends BaseCustomizableData<CohortAttribute> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cohort_id")
	private Integer cohortId;

	private String name;

	private String description;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "location_id")
	private Location location;

	private Date startDate;

	private Date endDate;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cohort_type_id")
	private CohortType cohortType;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cohort_program_id")
	private CohortProgram cohortProgram;

	@OneToMany(mappedBy = "cohort", cascade = CascadeType.ALL)
	private List<CohortLeader> cohortLeaders = new ArrayList<>();

	@OneToMany(mappedBy = "cohort", cascade = CascadeType.ALL)
	private List<CohortMember> cohortMembers = new ArrayList<>();

	@OneToMany(mappedBy = "cohort", cascade = CascadeType.ALL)
	private List<CohortVisit> cohortVisits = new ArrayList<>();

	@Column(name = "is_group_cohort", nullable = false)
	private Boolean groupCohort;

	public Integer getCohortId() {
		return cohortId;
	}

	public void setCohortId(Integer cohortId) {
		this.cohortId = cohortId;
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

	public CohortType getCohortType() {
		return cohortType;
	}

	public void setCohortType(CohortType cohortType) {
		this.cohortType = cohortType;
	}

	public CohortProgram getCohortProgram() {
		return cohortProgram;
	}

	public void setCohortProgram(CohortProgram cohortProgram) {
		this.cohortProgram = cohortProgram;
	}

	public void setCohortMembers(List<CohortMember> cohortMembers) {
		this.cohortMembers = cohortMembers;
	}

	public List<CohortMember> getCohortMembers() {
		if (cohortMembers == null) {
			cohortMembers = new ArrayList<>();
		}
		return cohortMembers;
	}

	public List<CohortVisit> getCohortVisits() {
		if (cohortVisits == null) {
			cohortVisits = new ArrayList<>();
		}
		return cohortVisits;
	}

	public void setCohortVisits(List<CohortVisit> cohortVisits) {
		this.cohortVisits = cohortVisits;
	}

	public void setCohortLeaders(List<CohortLeader> leaders) {
		this.cohortLeaders = leaders;
	}

	public List<CohortMember> getActiveCohortMembers() {
		List<CohortMember> members = new ArrayList<>();
		for (CohortMember member : getCohortMembers()) {
			if (!member.getVoided()) {
				members.add(member);
			}
		}
		return members;
	}

	public List<CohortLeader> getCohortLeaders() {
		if (cohortLeaders == null) {
			cohortLeaders = new ArrayList<>();
		}
		return cohortLeaders;
	}

	public List<CohortLeader> getActiveCohortLeaders() {
		List<CohortLeader> leaders = new ArrayList<>();
		for (CohortLeader leader : getCohortLeaders()) {
			if (!leader.getVoided()) {
				leaders.add(leader);
			}
		}
		return leaders;
	}

	public void addCohortLeader(CohortLeader leader) {
		leader.setCohort(this);

		for (CohortLeader currentLeader : getActiveCohortLeaders()) {
			if (currentLeader.equals(leader)) {
				// if we have the same CohortLeader, don't add the new leader
				return;
			}
		}
		CohortLeader currentLeader = getActiveCohortLeaders().get(0);
		// there can only be one active leader at a time
		currentLeader.setVoided(true);
		currentLeader.setEndDate(new Date());
		cohortLeaders.remove(currentLeader);
		if (!OpenmrsUtil.collectionContains(cohortLeaders, leader)) {
			cohortLeaders.add(leader);
		}
	}

	public CohortMember getMember(String uuid) {
		if (uuid != null) {
			for (CohortMember member : getCohortMembers()) {
				if (uuid.equals(member.getUuid()) && !member.getVoided()) {
					return member;
				}
			}
		}
		return null;
	}

	/**
	 * Returns whether this cohort is a group
	 *
	 * @deprecated since 3.0.0
	 * @see {{@link #getGroupCohort()}}
	 */
	@Deprecated
	public Boolean isGroupCohort() {
		return groupCohort;
	}

	public void setGroupCohort(Boolean groupCohort) {
		this.groupCohort = groupCohort;
	}

	public Boolean getGroupCohort() {
		return this.groupCohort;
	}

	@Override
	public Integer getId() {
		return getCohortId();
	}

	@Override
	public void setId(Integer id) {
		setCohortId(id);
	}

	@Override
	public String toString() {
		return this.name;
	}
}
	
	
	
