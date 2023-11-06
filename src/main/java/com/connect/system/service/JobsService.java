package com.connect.system.service;

import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.JobsDetailsDTO;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.JobsDetailsGetDTO;
import com.connect.system.domain.model.Jobs.JobsDetails;
import org.springframework.stereotype.Service;

@Service
public interface JobsService  {

    JobsDetails findJobById(Long id_jobs_details);

    JobsDetailsDTO updateJobsDetails(JobsDetails jobsDetails, Long id_jobs_details, JobsDetailsDTO jobsDetailsDTO);

    JobsDetailsGetDTO getJobById();
}
