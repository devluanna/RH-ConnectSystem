package com.connect.system.domain.repository;

import com.connect.system.domain.model.Jobs.JobsDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobDetailsRepository extends JpaRepository<JobsDetails, Long> {
}
