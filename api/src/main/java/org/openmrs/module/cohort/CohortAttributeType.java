package org.openmrs.module.cohort;

import lombok.NoArgsConstructor;
import org.openmrs.attribute.BaseAttributeType;

public class CohortAttributeType extends BaseAttributeType<CohortM> {

	private static final long serialVersionUID = 1L;

	private Integer cohortAttributeTypeId;

	public Integer getCohortAttributeTypeId() {
		return cohortAttributeTypeId;
	}

	public void setCohortAttributeTypeId(Integer cohortAttributeTypeId) {
		this.cohortAttributeTypeId = cohortAttributeTypeId;
	}

	@Override
	public Integer getId() {
		return getCohortAttributeTypeId();
	}

	@Override
	public void setId(Integer id) {
		setCohortAttributeTypeId(id);
	}

}
