package com.connect.system.rest.controller;


import com.connect.system.domain.model.Account.ResponseDTO.JobsDetailsDTO;
import com.connect.system.domain.model.Account.Jobs.JobsDetails;
import com.connect.system.service.AccountService;
import com.connect.system.service.JobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/jobs")
@RestController
public class JobsDetailsController {

    @Autowired
    JobsService jobsService;

    @Autowired
    AccountService accountService;


    @PutMapping("/{id_jobs_details}")
    public ResponseEntity updateJobs(@PathVariable Long id_jobs_details, @RequestBody JobsDetailsDTO jobsDetailsDTO) {

        JobsDetails jobsDetails = jobsService.findJobById(id_jobs_details);

        JobsDetailsDTO updatedJob = jobsService.updateJobsDetails(jobsDetails, id_jobs_details, jobsDetailsDTO);
        return ResponseEntity.ok(updatedJob);

    }

    @GetMapping("/get/{id_jobs_details}")
    public ResponseEntity<JobsDetailsDTO> getJobById(@PathVariable Long id_jobs_details) {
        JobsDetailsDTO jobsDetailsGetDTO = jobsService.getJobById(id_jobs_details);

        if(jobsDetailsGetDTO == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(jobsDetailsGetDTO);

    }



}
