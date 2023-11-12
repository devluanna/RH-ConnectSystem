package com.connect.system.domain.repository.User;

import com.connect.system.domain.model.Account.DashboardStudies.Certificates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificatesRepository extends JpaRepository<Certificates, Long> {

}
