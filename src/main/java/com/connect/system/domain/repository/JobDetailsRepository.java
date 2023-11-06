package com.connect.system.domain.repository;

import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.JobsDetailsGetDTO;
import com.connect.system.domain.model.Jobs.JobsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDetailsRepository extends JpaRepository<JobsDetails, Long> {

    @Query("SELECT NEW com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.JobsDetailsGetDTO(j.id_job_details, j.id_account, j.identity, j.description, j.name_squad, j.type_of_record, j.seniority, j.office, j.occupancy_area, j.time_experience, j.hard_skills, j.soft_skills, j.language_primary, j.report_me, j.admission_date) FROM JobsDetails j")
    JobsDetailsGetDTO findJobsById();
}
