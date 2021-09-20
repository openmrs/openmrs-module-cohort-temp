## Migrate data from cohort_leader table to cohort_member_attribute
## Get Cohort Leader cohortMemberAttributeTypeID
set @cohort_member_attribute_type_id = (select cohort_member_attribute_type_id
                                        from cohort_member_attribute_type
                                        where uuid = 'fac24350-c855-4c32-bdcf-36c4c439f538');

## Copy data
insert into cohort_member_attribute (cohort_member_id, value_reference, cohort_member_attribute_type_id, date_created,
                                     creator, changed_by, date_changed, voided_by, date_voided, void_reason, uuid)
select cm.cohort_member_id,
       cl.person_id,
       @cohort_member_attribute_type_id,
       cl.date_created,
       cl.creator,
       cl.changed_by,
       cl.date_changed,
       cl.voided_by,
       cl.date_voided,
       cl.void_reason,
       cl.uuid
from cohort_leader cl
         left join cohort_member cm on cm.cohort_id = cl.cohort_id
