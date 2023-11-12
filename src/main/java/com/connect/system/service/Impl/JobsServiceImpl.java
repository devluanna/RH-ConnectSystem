package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.ResponseDTO.JobsDetailsDTO;
import com.connect.system.domain.model.Account.Jobs.JobsDetails;
import com.connect.system.domain.repository.User.CertificatesRepository;
import com.connect.system.domain.repository.User.JobDetailsRepository;
import com.connect.system.service.AccountService;
import com.connect.system.service.JobsService;
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
    public JobsDetailsDTO getJobById(Long id_jobs_details) {
        return jobDetailsRepository.findJobsById(id_jobs_details);
    }


    @Override
    public JobsDetails findJobById(Long id_jobs_details) {
        return jobDetailsRepository.findById(id_jobs_details).orElseThrow(NoSuchElementException::new);
    }



    @Override
    public JobsDetailsDTO updateJobsDetails(JobsDetails jobsDetails, Long id_jobs_details, JobsDetailsDTO jobsDetailsDTO ) {
        Person authenticatedUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!authenticatedUser.getJobsDetails().getId_job_details().equals(id_jobs_details)) {
            throw new IllegalArgumentException("Access denied!");
        }

       JobsDetails jobsDetailsUser = findJobById(jobsDetails.getId_job_details());

        modelMapper.map(jobsDetailsDTO, jobsDetailsUser);
        modelMapper.map(jobsDetailsUser, jobsDetails);

       JobsDetails updatedJob = jobDetailsRepository.save(jobsDetails);

        return modelMapper.map(updatedJob, JobsDetailsDTO.class);

    }


}
