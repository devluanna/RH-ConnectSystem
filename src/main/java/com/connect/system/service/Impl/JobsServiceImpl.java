package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.JobsDetailsDTO;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.JobsDetailsGetDTO;
import com.connect.system.domain.model.Jobs.JobsDetails;
import com.connect.system.domain.repository.JobDetailsRepository;
import com.connect.system.service.JobsService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class JobsServiceImpl implements JobsService {

    @Autowired
    JobDetailsRepository jobDetailsRepository;

    private final ModelMapper modelMapper;

    public JobsServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public JobsDetailsGetDTO getJobById() {
        return jobDetailsRepository.findJobsById();
    }

    @Override
    public JobsDetails findJobById(Long id_jobs_details) {
        return jobDetailsRepository.findById(id_jobs_details).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public JobsDetailsDTO updateJobsDetails(JobsDetails jobsDetails, Long id_jobs_details, JobsDetailsDTO jobsDetailsDTO) {
        JobsDetails jobsDetailsUser = findJobById(jobsDetails.getId_job_details());

        modelMapper.map(jobsDetailsDTO, jobsDetailsUser);

        modelMapper.map(jobsDetailsUser, jobsDetails);

        JobsDetails updatedJob = jobDetailsRepository.save(jobsDetails);

        JobsDetailsDTO updatedDTO = modelMapper.map(updatedJob, JobsDetailsDTO.class);

        return updatedDTO;

       // if(jobsDetailsDTO.getName_squad() != null) {
        //    jobsDetailsUser.setName_squad(jobsDetailsDTO.getName_squad());
        //}

        //JobsDetails updatedJob = jobDetailsRepository.save(jobsDetailsUser);
        //BeanUtils.copyProperties(updatedJob, jobsDetailsDTO);

        //return jobsDetailsDTO;

    }


}
