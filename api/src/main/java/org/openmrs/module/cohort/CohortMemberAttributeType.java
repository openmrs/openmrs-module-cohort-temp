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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.openmrs.attribute.AttributeType;
import org.openmrs.attribute.BaseAttributeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cohort_member_attribute_type")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CohortMemberAttributeType extends BaseAttributeType<CohortMember> implements AttributeType<CohortMember>{

    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "attribute_type_id")
    private Integer cohortMemberAttributeTypeId;

    @Override
    public Integer getId() {
        return this.cohortMemberAttributeTypeId;
    }

    @Override
    public void setId(Integer id) {
        this.cohortMemberAttributeTypeId = id;
    }
}
