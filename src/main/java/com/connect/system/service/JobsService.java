package com.connect.system.service;

import com.connect.system.domain.model.Account.ResponseDTO.JobsDetailsDTO;
import com.connect.system.domain.model.Account.Jobs.JobsDetails;
import org.springframework.stereotype.Service;

@Service
public interface JobsService  {

    JobsDetails findJobById(Long id_jobs_details);

    JobsDetailsDTO updateJobsDetails(JobsDetails jobsDetails, Long id_jobs_details, JobsDetailsDTO jobsDetailsDTO);

    JobsDetailsDTO getJobById(Long id_jobs_details);

}
