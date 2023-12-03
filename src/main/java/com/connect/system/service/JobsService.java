package com.connect.system.service;

import com.connect.system.domain.model.Account.ResponseDTO.JobsDetailsDTO;
import com.connect.system.domain.model.Account.Jobs.JobsDetails;
import org.springframework.stereotype.Service;

@Service
public interface JobsService  {

    JobsDetails findJobById(Integer id_jobs_details);

    JobsDetails toUpdateJobsDetails(JobsDetails jobsDetails, Integer id_jobs_details);

    JobsDetailsDTO getJobById(Integer id_jobs_details);

}
