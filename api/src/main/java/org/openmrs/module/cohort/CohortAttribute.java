package org.openmrs.module.cohort;

import lombok.NoArgsConstructor;
import org.openmrs.attribute.Attribute;
import org.openmrs.attribute.BaseAttribute;

public class CohortAttribute extends BaseAttribute<CohortAttributeType, CohortM> implements Attribute<CohortAttributeType, CohortM> {

	private static final long serialVersionUID = 1L;

	private Integer cohortAttributeId;

	public Integer getCohortAttributeId() {
		return cohortAttributeId;
	}

	public void setCohortAttributeId(Integer cohortAttributeId) {
		this.cohortAttributeId = cohortAttributeId;
	}

	public void setCohort(CohortM cohort) {this.setOwner(cohort);}

	public CohortM getCohort() {return this.getOwner();};

	@Override
	public Integer getId() {
		return getCohortAttributeId();
	}

	@Override
	public void setId(Integer arg0) {
		setCohortAttributeId(arg0);

	}

}
