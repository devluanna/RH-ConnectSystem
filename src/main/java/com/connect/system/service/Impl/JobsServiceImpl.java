package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.DashboardStudies.Certificates;
import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.ResponseDTO.JobsDetailsDTO;
import com.connect.system.domain.model.Account.Jobs.JobsDetails;
import com.connect.system.domain.repository.User.CertificatesRepository;
import com.connect.system.domain.repository.User.JobDetailsRepository;
import com.connect.system.service.AccountService;
import com.connect.system.service.JobsService;
import com.connect.system.utils.Utils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class JobsServiceImpl implements JobsService {

    @Autowired
    JobDetailsRepository jobDetailsRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    CertificatesRepository certificatesRepository;

    private final ModelMapper modelMapper;

    public JobsServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public JobsDetailsDTO getJobById(Integer id_jobs_details) {
        return jobDetailsRepository.findJobsById(id_jobs_details);
    }


    @Override
    public JobsDetails findJobById(Integer id_jobs_details) {
        return jobDetailsRepository.findById(id_jobs_details).orElseThrow(NoSuchElementException::new);
    }



    @Override
    public JobsDetails toUpdateJobsDetails(JobsDetails jobsDetails, Integer id_jobs_details  ) {

       JobsDetails existingJobs = findJobById(id_jobs_details);

     //   Person authenticatedUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //if (!authenticatedUser.getJobsDetails().getId_job_details().equals(id_jobs_details)) {
       // throw new IllegalArgumentException("Access denied!");
       //  }

        if (existingJobs != null) {
            Utils.copyNonNullProperties(jobsDetails, existingJobs);
        }
        return jobDetailsRepository.save(existingJobs);
    }

}
