package com.connect.system.domain.repository.User;

import com.connect.system.domain.model.Account.ResponseDTO.JobsDetailsDTO;
import com.connect.system.domain.model.Account.Jobs.JobsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDetailsRepository extends JpaRepository<JobsDetails, Long> {

    @Query("SELECT NEW com.connect.system.domain.model.Account.ResponseDTO.JobsDetailsDTO(j.id_job_details, j.id_account, j.identity, j.id_squad, j.id_memberOfSquad, j.name_squad, j.description, j.type_of_record, j.office, j.occupancy_area, j.seniority, j.time_experience, j.hard_skills, j.soft_skills, j.language_primary, j.admission_date) FROM JobsDetails j WHERE j.id = :id_job_details")
    JobsDetailsDTO findJobsById(@Param("id_job_details") Long id_job_details);
}
