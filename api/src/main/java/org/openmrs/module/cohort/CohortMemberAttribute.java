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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.openmrs.BaseOpenmrsData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cohort_member_attribute")
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CohortMemberAttribute extends BaseOpenmrsData {

    private static final long serialVersionUID = 2L;

    @Id
    @Column(name = "cohort_member_attribute_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer cohortMemberAttributeId;

    @ManyToOne
    @JoinColumn(name = "cohort_member_id")
    private CohortMember cohortMember;

    private String value;

    @ManyToOne
    @JoinColumn(name = "cohort_member_attribute_type_id")
    private CohortMemberAttributeType cohortMemberAttributeType;

    @Override
    public Integer getId() {
        return this.cohortMemberAttributeId;
    }

    @Override
    public void setId(Integer id) {
        this.cohortMemberAttributeId = id;
    }
}
