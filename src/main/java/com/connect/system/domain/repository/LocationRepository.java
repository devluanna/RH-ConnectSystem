package com.connect.system.domain.repository;

import com.connect.system.domain.model.AccountInformation.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {
}
